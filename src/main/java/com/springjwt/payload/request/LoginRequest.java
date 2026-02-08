package com.springjwt.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequest {

    @Size(max = 100)
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(max = 255)
    private String motdepasse;
    
    private String userType;

    public LoginRequest(@NotBlank String email, @NotBlank String motdepasse, String userType) {
        super();
        this.email = email;
        this.motdepasse = motdepasse;
        this.userType = userType;
    }

    public LoginRequest() {
    }
}
