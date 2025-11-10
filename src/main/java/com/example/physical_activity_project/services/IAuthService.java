package com.example.physical_activity_project.services;


import com.example.physical_activity_project.dto.auth.LoginDTO;
import com.example.physical_activity_project.dto.auth.TokenResponseDTO;

public interface IAuthService {
    TokenResponseDTO login(LoginDTO request);
}