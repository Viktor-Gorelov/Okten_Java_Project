package com.okten.okten_project_java.config;

import com.okten.okten_project_java.entities.UserRole;
import com.okten.okten_project_java.filter.JwtAuthFilter;
import com.okten.okten_project_java.services.UserService;
import com.okten.okten_project_java.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return daoAuthenticationProvider;
    }

    @SneakyThrows
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }

    @SneakyThrows
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        return httpSecurity
                .csrf(CsrfConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/**", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/advertisements", "/advertisements/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/advertisements").hasAnyRole(UserRole.ADMIN, UserRole.SELLER)
                        .requestMatchers(HttpMethod.PUT, "/advertisements/{id}").hasAnyRole(UserRole.ADMIN, UserRole.SELLER)
                        .requestMatchers(HttpMethod.DELETE, "/advertisements/{id}").hasAnyRole(UserRole.ADMIN, UserRole.MANAGER, UserRole.SELLER)
                        .requestMatchers("/users/**").hasAnyRole(UserRole.ADMIN, UserRole.MANAGER)
                        .requestMatchers("/managers/**").hasRole(UserRole.ADMIN)
                        .requestMatchers("/admins/**").hasRole(UserRole.ADMIN)
                        .requestMatchers("/seller/upgrade-to-vip").hasRole(UserRole.SELLER)
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .addFilterBefore(new JwtAuthFilter(jwtUtil,userService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
}
