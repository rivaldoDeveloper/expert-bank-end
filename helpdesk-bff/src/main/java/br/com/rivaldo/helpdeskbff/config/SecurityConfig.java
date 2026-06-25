package br.com.rivaldo.helpdeskbff.config;

import br.com.rivaldo.helpdeskbff.security.JWTAuthorizationFilter;
import br.com.rivaldo.helpdeskbff.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authConfig;
    private final JWTUtil jwtUtil;

    // Constantes de rotas e whitelists casando perfeitamente com o .startsWith() do seu Filtro JWT
    public static final String[] SWAGGER_WHITELIST = {"/swagger-ui", "/v3/api-docs", "/swagger-resources", "/webjars"};
    public static final String[] POST_WHITELIST = {"/api/auth/login", "/api/auth/refresh-token", "/api/users"};
    public static final String[] PUBLIC_ROUTES = {"/api/auth/login", "/api/auth/refresh-token", "/api/users", "/swagger-ui", "/v3/api-docs", "/swagger-resources", "/webjars"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // Adiciona o seu filtro customizado antes do filtro padrão do Spring Security
                .addFilterBefore(new JWTAuthorizationFilter(authConfig.getAuthenticationManager(), jwtUtil, PUBLIC_ROUTES), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Libera as rotas de documentação do Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        // Libera as rotas públicas de POST (Login, Refresh e criação de Usuário)
                        .requestMatchers(HttpMethod.POST, POST_WHITELIST).permitAll()
                        // Garante que o GET /api/users exija autenticação explícita no BFF
                        .requestMatchers("/api/users/**").authenticated()
                        // Bloqueia qualquer outra rota por padrão
                        .anyRequest().authenticated()
                )
                .build();
    }
}