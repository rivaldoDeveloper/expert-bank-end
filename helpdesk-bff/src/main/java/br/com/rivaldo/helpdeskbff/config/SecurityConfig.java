package br.com.rivaldo.helpdeskbff.config;

import br.com.rivaldo.helpdeskbff.security.JWTAuthorizationFilter;
import br.com.rivaldo.helpdeskbff.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authConfig;
    private final JWTUtil jwtUtil;

    // Ajustado para casar perfeitamente com o .startsWith() do Filtro JWT
    public static final String[] SWAGGER_WHITELIST = {"/swagger-ui", "/v3/api-docs", "/swagger-resources", "/webjars"};
    public static final String[] POST_WHITELIST = {"/api/auth/login", "/api/auth/refresh-token"};
    public static final String[] PUBLIC_ROUTES = {"/api/auth/login", "/api/auth/refresh-token", "/swagger-ui", "/v3/api-docs", "/swagger-resources", "/webjars"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // CORREÇÃO: Alinhado para rodar antes do UsernamePasswordAuthenticationFilter do Spring padrão
                .addFilterBefore(new JWTAuthorizationFilter(authConfig.getAuthenticationManager(), jwtUtil, PUBLIC_ROUTES), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        .requestMatchers(POST, POST_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}