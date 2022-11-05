package com.utn.bolsadetrabajo.security.authentication;

import com.utn.bolsadetrabajo.model.Role;

import java.io.Serializable;

public class AuthenticationResponseByFlutter implements Serializable {

    private final String jwt;
    private Long id;
    private String username;
    private String role;

    public AuthenticationResponseByFlutter(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}