package br.com.rivaldo.helpdeskbff.cliente;

import br.com.rivaldo.helpdeskbff.config.FeignConfig;
import br.com.rivaldo.models.requests.AuthenticateRequest;
import br.com.rivaldo.models.requests.RefreshTokenRequest;
import br.com.rivaldo.models.responses.AuthenticationResponse;
import br.com.rivaldo.models.responses.RefreshTokenResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "auth-service-api",
        path = "/api/auth",
        url = "http://localhost:8082",
        configuration = FeignConfig.class
)
public interface AuthFeignClient {
    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> authenticate( @RequestBody final AuthenticateRequest request) throws Exception;

    @PostMapping("/refresh-token")
    ResponseEntity<RefreshTokenResponse>  refreshToken(@RequestBody final RefreshTokenRequest request);
}
