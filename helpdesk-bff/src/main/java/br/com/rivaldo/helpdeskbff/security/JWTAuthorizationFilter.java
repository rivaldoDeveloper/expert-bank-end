package br.com.rivaldo.helpdeskbff.security;

import br.com.rivaldo.models.exceptions.StandardError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final JWTUtil jwtUtil;
    private final String[] publicRoutes;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, String[] publicRoutes) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.publicRoutes = publicRoutes;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // AJUSTADO: Passa o request completo para avaliar a rota E o método HTTP de forma inteligente
        if (isPublicRoute(request)) {
            chain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(AUTHORIZATION);

        // Se não houver token Bearer, delega para a cadeia do Spring Security decidir nas rotas privadas
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            UsernamePasswordAuthenticationToken auth = getAuthentication(request);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            handleException(request.getRequestURI(), e.getMessage(), response);
            return;
        }

        chain.doFilter(request, response);
    }

    private void handleException(String requestURI, String message, HttpServletResponse response) throws IOException {
        StandardError error = StandardError.builder()
                .timeStamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(message)
                .path(requestURI)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = mapper.writeValueAsString(error);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(json);
    }

    // AJUSTADO: Método robusto para diferenciar requisições públicas de privadas por Método HTTP
    private boolean isPublicRoute(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // Rota de usuários SÓ é pública se for criação de conta (POST)
        if (uri.startsWith("/api/users") && method.equalsIgnoreCase("POST")) {
            return true;
        }

        // Para as demais constantes configuradas no SecurityConfig (Logins, Swagger, etc.)
        for (String route : this.publicRoutes) {
            // Ignora o mapeamento genérico do array se bater com a rota de usuários por outro método (ex: GET)
            if (route.equals("/api/users")) {
                continue;
            }
            if (uri.startsWith(route)) {
                return true;
            }
        }
        return false;
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (header == null) {
            return null;
        }

        // 1. Limpa espaços nas pontas do cabeçalho
        String token = header.trim();

        // 2. Remove o primeiro "Bearer "
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        // 3. CORREÇÃO CRUCIAL: Se o Swagger duplicou e enviou "Bearer Bearer ...", remove o segundo!
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        // 4. Remove aspas residuais que o Swagger UI às vezes joga no cache local
        token = token.replace("\"", "");

        // Faz o parse seguro com a string do token perfeitamente limpa
        Claims claims = jwtUtil.getClaims(token);
        if (claims != null) {
            String username = claims.getSubject();
            List<GrantedAuthority> authorities = jwtUtil.getAuthorities(claims);

            if (username != null) {
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }
        }
        return null;
    }
}