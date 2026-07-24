// Configuration
const API_BASE_URL = 'http://localhost:8080'; // Change to your Spring Boot port if different

// Application State
let authState = {
    accessToken: localStorage.getItem('accessToken') || null,
    username: localStorage.getItem('username') || null
};
let activeTab = 'PRIVATE';
let activeThreadId = null;
let threadsData = [];
let openedThreadData = null;

// DOM Elements
const loginOverlay = document.getElementById('login-overlay');
const loginForm = document.getElementById('login-form');
const loginError = document.getElementById('login-error');

const tabsContainer = document.getElementById('tabs');
const threadListEl = document.getElementById('thread-list');
const chatHeaderEl = document.getElementById('chat-header');
const messagesContainerEl = document.getElementById('messages-container');
const messageInputEl = document.getElementById('message-input');
const btnSendEl = document.getElementById('btn-send');

// --- 1. CORE API FETCHER (Handles JWT Auth automatically) ---
async function apiFetch(endpoint, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        ...(options.headers || {})
    };

    if (authState.accessToken) {
        headers['Authorization'] = `Bearer ${authState.accessToken}`;
    }

    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        ...options,
        headers
    });

    if (response.status === 401 || response.status === 403) {
        handleLogout();
        throw new Error("Authentication failed. Please log in again.");
    }

    if (!response.ok) {
        throw new Error(`API Error: ${response.status}`);
    }

    // Return JSON if there's content, otherwise null
    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

// --- 2. AUTHENTICATION (Maps to UserController) ---
loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    loginError.textContent = 'Logging in...';

    const payload = {
        email: document.getElementById('login-email').value,
        username: document.getElementById('login-username').value,
        password: document.getElementById('login-password').value
    };

    try {
        // Maps to @PostMapping("/login") accepting UserDTO
        const data = await apiFetch('/user/login', {
            method: 'POST',
            body: JSON.stringify(payload)
        });

        // Store TokenDTO
        authState.accessToken = data.accessToken;
        authState.username = payload.username;
        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('username', payload.username);

        loginOverlay.classList.add('hidden');
        initChat(); // Boot the app

    } catch (err) {
        loginError.textContent = "Login failed. Check your credentials.";
        console.error(err);
    }
});

function handleLogout() {
    authState.accessToken = null;
    authState.username = null;
    localStorage.removeItem('accessToken');
    localStorage.removeItem('username');
    loginOverlay.classList.remove('hidden');
}

// --- 3. CHAT INITIALIZATION ---
function init() {
    if (authState.accessToken) {
        loginOverlay.classList.add('hidden');
        initChat();
    }
}

function initChat() {
    setupEventListeners();
    fetchThreads(activeTab);
}

function setupEventListeners() {
    // Tab switching (Private, Group, Channel)
    tabsContainer.addEventListener('click', (e) => {
        const tabBtn = e.target.closest('.tab');
        if (!tabBtn) return;
        
        document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
        tabBtn.classList.add('active');
        
        activeTab = tabBtn.dataset.tab;
        fetchThreads(activeTab);
    });

    // Thread selection
    threadListEl.addEventListener('click', (e) => {
        const threadEl = e.target.closest('.thread-item');
        if (!threadEl) return;

        activeThreadId = parseInt(threadEl.dataset.id);
        renderThreadsList(); // Update highlight
        fetchThreadData(activeThreadId);
    });

    // Auto-resize textarea
    messageInputEl.addEventListener('input', function() {
        this.style.height = 'auto';
        this.style.height = (this.scrollHeight) + 'px';
        btnSendEl.disabled = this.value.trim().length === 0;
    });

    // Send Message
    btnSendEl.addEventListener('click', sendApiMessage);
    messageInputEl.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendApiMessage();
        }
    });
}

// --- 4. API CALLS (Maps to ThreadController & MessageController) ---

async function fetchThreads(type) {
    try {
        // Maps to GET /api/threads/all/{type}
        const endpointType = type.toLowerCase(); 
        const threads = await apiFetch(`/api/threads/all/${endpointType}`);
        threadsData = threads;
        renderThreadsList();
    } catch (err) {
        console.error("Failed to fetch threads", err);
    }
}

async function fetchThreadData(threadId) {
    try {
        // Maps to GET /api/threads/open/{id} returning OpenedThreadDTO
        openedThreadData = await apiFetch(`/api/threads/open/${threadId}`);
        renderChatArea();
    } catch (err) {
        console.error("Failed to fetch thread data", err);
    }
}

async function sendApiMessage() {
    const content = messageInputEl.value.trim();
    if (!content || !activeThreadId) return;

    try {
        // Maps to POST /api/messages/send accepting SendMessageDTO
        await apiFetch('/api/messages/send', {
            method: 'POST',
            body: JSON.stringify({
                threadId: activeThreadId,
                content: content
            })
        });

        // Clear input
        messageInputEl.value = '';
        messageInputEl.style.height = 'auto';
        btnSendEl.disabled = true;

        // Re-fetch the thread to show the new message
        fetchThreadData(activeThreadId);

    } catch (err) {
        console.error("Failed to send message", err);
        alert("Failed to send message.");
    }
}

// --- 5. RENDER FUNCTIONS ---

function renderThreadsList() {
    if (threadsData.length === 0) {
        threadListEl.innerHTML = `<div style="text-align:center; padding: 2rem; color: var(--text-muted); font-size: 0.875rem;">No threads found.</div>`;
        return;
    }

    threadListEl.innerHTML = threadsData.map(thread => `
        <div class="thread-item ${thread.id === activeThreadId ? 'active' : ''}" data-id="${thread.id}">
            <div class="avatar">
                ${thread.name.charAt(0).toUpperCase()}
                ${thread.type === 'PRIVATE' ? '<div class="online-dot"></div>' : ''}
            </div>
            <div class="thread-info">
                <div class="thread-name">${thread.name}</div>
                <div class="thread-preview">Tap to view</div>
            </div>
        </div>
    `).join('');
}

function renderChatArea() {
    if (!openedThreadData) return;

    // Header
    let iconClass = 'ph-user';
    if(openedThreadData.type === 'GROUP') iconClass = 'ph-users';
    if(openedThreadData.type === 'CHANNEL') iconClass = 'ph-hash';

    chatHeaderEl.innerHTML = `
        <div class="chat-title">
            <i class="ph ${iconClass}" style="color: var(--text-muted);"></i>
            ${openedThreadData.name}
        </div>
    `;

    // Messages array inside OpenedThreadDTO
    messagesContainerEl.innerHTML = openedThreadData.messages.map(msg => {
        const isMine = msg.sender_username === authState.username;
        
        // Format timestamp safely
        const timeString = new Date(msg.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

        return `
            <div class="message ${isMine ? 'mine' : 'others'}">
                <div class="message-bubble">
                    ${!isMine && openedThreadData.type !== 'PRIVATE' ? `<div class="message-sender">${msg.sender_username}</div>` : ''}
                    ${msg.content}
                </div>
                <div class="message-meta">
                    ${timeString} ${msg.isEdited ? '(edited)' : ''}
                </div>
            </div>
        `;
    }).join('');

    // Scroll to bottom
    messagesContainerEl.scrollTop = messagesContainerEl.scrollHeight;
}

// Boot up
init();