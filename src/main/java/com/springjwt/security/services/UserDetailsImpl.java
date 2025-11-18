package com.springjwt.security.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import com.springjwt.models.User;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private User user;
    private Long id;

    private String nom;

    private String prenom;
    @Getter
    private String type;
    private int Tel;

    private String email;
    @Getter
    String photo;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String nom, String prenom,String type, int Tel, String email, String photo,String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.type=type;
        this.Tel = Tel;
        this.email = email;
        this.password = password;
        this.photo=photo;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(String type, User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getType(),
                user.getTel(),
                user.getEmail(),
                user.getPhoto(),
                user.getPassword(),
                Collections.emptyList()
        );
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public String getUsername() {
        return nom;
    }



}