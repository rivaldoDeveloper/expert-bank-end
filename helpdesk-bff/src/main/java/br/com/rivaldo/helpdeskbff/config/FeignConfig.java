package br.com.rivaldo.helpdeskbff.config;

import br.com.rivaldo.helpdeskbff.decoder.RetrieveMessageErrorDecoder;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    ErrorDecoder getDecoder() {
        return new RetrieveMessageErrorDecoder();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

                if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
                    requestTemplate.header(HttpHeaders.AUTHORIZATION);
                    requestTemplate.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
                }
            }
        };
    }
}