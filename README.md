# CloudChats

CloudChats is a full-stack real-time chat application built with a Spring Boot backend and a React + Vite frontend. It supports authenticated users, private/group/channel conversations, instant message delivery through WebSockets, and message actions such as edit, delete, and pin.

## Overview

CloudChats combines modern web technologies to deliver a lightweight but practical messaging experience:

- Secure user authentication with JWT access and refresh tokens
- Real-time message updates using STOMP over WebSocket
- Thread-based conversations for private, group, and channel use cases
- Message management features such as edit, delete, and pin/unpin
- PostgreSQL persistence with Redis support for caching
- Swagger/OpenAPI documentation for backend endpoints

## Tech Stack

### Backend
- Java 21
- Spring Boot 4.1.0
- Spring Security
- Spring Data JPA
- WebSocket/STOMP messaging
- PostgreSQL
- Redis
- JWT (jjwt)
- Springdoc OpenAPI

### Frontend
- React 18
- Vite 5
- Tailwind CSS
- Lucide icons
- STOMP client for WebSocket communication

## Project Structure

- cloudchat-backend: Spring Boot REST API, security layer, persistence, and WebSocket config
- cloudchat-frontend: React UI and frontend API client
- startapp.bat: convenience script for launching both services on Windows
- setenv.bat: environment variables used for local development

## Features

### Authentication
- Register new users
- Log in with email/password
- Refresh access tokens
- Log out and clear session data

### Threads
- Create private threads
- Create group threads with multiple participants
- Create channel threads
- Open an existing thread and view its messages
- Add members to a thread
- Delete a thread

### Messaging
- Send messages in real time
- Edit existing messages
- Delete messages
- Pin or unpin messages
- Receive new messages instantly through WebSocket subscriptions

## Prerequisites

Before running the project locally, make sure you have:

- JDK 21 or newer
- Node.js and npm
- PostgreSQL running locally
- Redis running locally

## Environment Configuration

The backend reads environment variables from a local environment file or the provided Windows batch script.

Create a .env file inside the backend project or update the values in setenv.bat:

```env
DB_URL=jdbc:postgresql://localhost:5432/projectdb
DB_USERNAME=postgres
DB_PASSWORD=123
JWT_ACCESS_SECRET=your-access-secret
JWT_ACCESS_LIFE=3600000
JWT_REFRESH_SECRET=your-refresh-secret
JWT_REFRESH_LIFE=36000000
```

The backend uses Spring Boot configuration from application.yaml, and the existing setup expects PostgreSQL and Redis to be available locally.

## Running the Application Locally

### Option 1: Use the Windows startup script

```bat
startapp.bat
```

### Option 2: Run each part manually

#### Backend

```bash
cd cloudchat-backend
./mvnw spring-boot:run
```

On Windows PowerShell or Command Prompt:

```bat
cd cloudchat-backend
mvnw.cmd spring-boot:run
```

#### Frontend

```bash
cd cloudchat-frontend
npm install
npm run dev
```

Then open:

- Frontend: http://localhost:5173
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html

## API Notes

The backend exposes REST endpoints under the following areas:

- Authentication: /user/register, /user/login, /user/refresh, /user/logout
- Threads: /api/threads/...
- Messages: /api/messages/...

The frontend currently points to a hosted backend URL in the API client. For local development, update the API base URL in cloudchat-frontend/src/api.js if you want the UI to call your local backend instead of the remote endpoint.

## Notes

- The backend uses Hibernate ddl-auto update, so the database schema is created or updated automatically.
- JWT-based authentication is enforced for most API routes, while auth and Swagger endpoints remain public.
- WebSocket communication is enabled for real-time chat updates per thread.

## License

This project is available under the MIT-style licensing terms included in the repository.

