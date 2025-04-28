package org.fm.backend.dto;

public class LoginToken {
    public String token ;
    public String message ;

    public LoginToken(String _token, String _message)
    {
        token = _token;
        message = _message;
    }

    @Override
    public String toString() {
        return "LoginToken{" +
                "token='" + token + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
