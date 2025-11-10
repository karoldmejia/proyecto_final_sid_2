package com.example.physical_activity_project.security;

import org.springframework.security.core.GrantedAuthority;
import com.example.physical_activity_project.model.Permission;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SecurityAuthority implements GrantedAuthority {

    private final Permission permission;

    @Override
    public String getAuthority() {
        return permission.getName();
    }
}
