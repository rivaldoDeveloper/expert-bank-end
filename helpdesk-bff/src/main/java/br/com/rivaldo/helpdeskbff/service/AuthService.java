package br.com.rivaldo.helpdeskbff.service;

import br.com.rivaldo.helpdeskbff.cliente.AuthFeignClient;
import br.com.rivaldo.models.requests.AuthenticateRequest;
import br.com.rivaldo.models.requests.RefreshTokenRequest;
import br.com.rivaldo.models.responses.AuthenticationResponse;
import br.com.rivaldo.models.responses.RefreshTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthFeignClient authFeignClient;

    public AuthenticationResponse authenticate(AuthenticateRequest request) throws Exception{
        return authFeignClient.authenticate(request).getBody();
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        return authFeignClient.refreshToken(request).getBody();
    }
}
