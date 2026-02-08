package com.springjwt.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type;
    private Integer id;
    private String email;
    private String username;

    public JwtResponse(String token, Integer id, String username, String email, String type) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.type = type;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return "Bearer";
    }
}
