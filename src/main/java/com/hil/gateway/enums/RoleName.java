package com.hil.gateway.enums;

import org.springframework.security.core.GrantedAuthority;

public enum  RoleName implements GrantedAuthority {
    USER("USER"),
    PM("PM"),
    ADMIN("ADMIN");

    private String authority;

    RoleName(String authority) {
        this.authority=authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
