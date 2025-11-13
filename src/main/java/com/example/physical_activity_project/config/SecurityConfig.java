package com.example.physical_activity_project.config;

import com.example.physical_activity_project.security.CustomUserDetailsService;
import com.example.physical_activity_project.security.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    // Seguridad para MVC (Form Login + Roles)
    @Bean
    @Order(1)
    public SecurityFilterChain mvcSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/mvc/**", "/user/**")
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/mvc/login", "/mvc/signup/form", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/mvc/users/").hasRole("Admin")
                        .requestMatchers("/mvc/users/add", "/mvc/users/edit/**", "/mvc/users/delete/**").hasRole("Admin")
                        .requestMatchers("/mvc/trainer/**").hasRole("Trainer")
                        .requestMatchers("/mvc/roles/**", "/mvc/permissions/**").hasRole("Admin")
                        .requestMatchers("/user/**").permitAll()  // ✅ Todos los roles pueden acceder a /user/**
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/mvc/login")
                        .loginProcessingUrl("/mvc/authenticate")
                        .successHandler((request, response, authentication) -> {
                            String redirectUrl = "/mvc/login?error";
                            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

                            boolean roleMatched = false;
                            for (GrantedAuthority authority : authorities) {
                                String authorityName = authority.getAuthority();
                                System.out.println("DEBUG - Authority: " + authorityName); // Para depuración

                                if (authorityName.equals("ROLE_Admin")) {
                                    redirectUrl = "/mvc/admin";
                                    roleMatched = true;
                                    break;
                                } else if (authorityName.equals("ROLE_User")) {
                                    redirectUrl = "/user/dashboard";
                                    roleMatched = true;
                                    break;
                                } else if (authorityName.equals("ROLE_Trainer")) {
                                    redirectUrl = "/mvc/trainer/dashboard";
                                    roleMatched = true;
                                    break;
                                }
                            }

                            // ✅ CASO POR DEFECTO: Si no coincide ningún rol, va a /user/dashboard
                            if (!roleMatched) {
                                redirectUrl = "/user/dashboard";
                            }

                            response.sendRedirect(request.getContextPath() + redirectUrl);
                        })
                        .failureUrl("/mvc/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/mvc/logout")
                        .logoutSuccessUrl("/mvc/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .build();
    }

    // Seguridad para REST (JWT)
    @Bean
    @Order(2)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}