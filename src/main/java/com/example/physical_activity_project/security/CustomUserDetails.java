package com.example.physical_activity_project.security;

import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.model.RolePermission;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.model.UserRole;
import com.example.physical_activity_project.repository.IUserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;
    private final IUserRoleRepository userRoleRepository;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Obtenemos todos los roles del usuario
        List<UserRole> userRoles = userRoleRepository.findByUser(user);

        for (UserRole ur : userRoles) {
            Role role = ur.getRole();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

            // Agregamos los permisos específicos del rol
            if (role.getRolePermissions() != null) {
                for (RolePermission rp : role.getRolePermissions()) {
                    authorities.add(new SimpleGrantedAuthority(rp.getPermission().getName()));
                }
            }
        }
        return authorities;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
