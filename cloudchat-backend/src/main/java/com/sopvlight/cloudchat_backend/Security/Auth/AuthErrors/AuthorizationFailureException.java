package com.sopvlight.cloudchat_backend.Security.Auth.AuthErrors;

public class AuthorizationFailureException extends Exception {
    public AuthorizationFailureException(String msg){
        super(msg);
    }
}
