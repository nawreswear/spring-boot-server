package com.springjwt.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequest {

    @Size(max = 50)
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;
    private String type;

    public LoginRequest(@NotBlank String email, @NotBlank String password,String type) {
        super();
        this.email = email;
        this.password = password;
        this.type=type;
    }



}
