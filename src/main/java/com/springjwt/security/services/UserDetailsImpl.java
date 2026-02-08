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
    
    private Integer id;
    private String nom;
    private String prenom;
    private String type;
    private String telephone;
    private String email;
    private String photo;
    private String motdepasse;
    private String ville;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Integer id, String nom, String prenom, String type, String telephone, 
                        String email, String ville, String photo, String motdepasse, 
                        Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.type = type;
        this.telephone = telephone;
        this.email = email;
        this.ville = ville;
        this.photo = photo;
        this.motdepasse = motdepasse;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getType(),
                user.getTelephone(),
                user.getEmail(),
                user.getVille(),
                user.getPhoto(),
                user.getMotdepasse(),
                Collections.emptyList()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return motdepasse;
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
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String getUsername() {
        return nom + " " + prenom; // Return full name as username
    }
}