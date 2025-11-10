package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.dto.auth.LoginDTO;
import com.example.physical_activity_project.dto.auth.TokenResponseDTO;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.security.CustomUserDetails;
import com.example.physical_activity_project.services.IAuthService;
import com.example.physical_activity_project.services.IJwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final IJwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponseDTO login(LoginDTO request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        if (userDetails == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        CustomUserDetails customUD = (CustomUserDetails) userDetails;
        User user = customUD.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        String token = jwtService.generateToken(user, auth);
        return new TokenResponseDTO(token);
    }

}
