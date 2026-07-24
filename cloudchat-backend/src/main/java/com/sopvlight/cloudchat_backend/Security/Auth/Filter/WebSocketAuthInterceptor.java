package com.sopvlight.cloudchat_backend.Security.Auth.Filter;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.sopvlight.cloudchat_backend.Security.Auth.Service.JWTService;
import com.sopvlight.cloudchat_backend.Users.Service.UserService;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JWTService jwtService;
    private final UserService userService;

    public WebSocketAuthInterceptor(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // Only intercept the initial CONNECT frame
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            
            // Extract the JWT from the STOMP headers
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtService.extractUserName(token);

                if (username != null) {
                    UserDetails userDetails = userService.loadUserByUsername(username);
                    
                    if (jwtService.isValidToken(token, userDetails)) {
                        // Token is valid! Assign the user to this WebSocket session
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        accessor.setUser(authentication);
                    }
                }
            }
        }
        return message;
    }
}