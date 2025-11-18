package com.springjwt.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type;
    private Long id;
    private String password;
    private String email;

    public JwtResponse(String accessToken, Long id, String password, String email, String type) {
        this.token = accessToken;
        this.id = id;
        this.password = password;
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
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }


}
