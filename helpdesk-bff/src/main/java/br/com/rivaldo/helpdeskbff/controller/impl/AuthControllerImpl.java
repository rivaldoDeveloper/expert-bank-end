package br.com.rivaldo.helpdeskbff.controller.impl;

import br.com.rivaldo.helpdeskbff.controller.AuthController;
import br.com.rivaldo.helpdeskbff.service.AuthService;
import br.com.rivaldo.models.requests.AuthenticateRequest;
import br.com.rivaldo.models.requests.RefreshTokenRequest;
import br.com.rivaldo.models.responses.AuthenticationResponse;
import br.com.rivaldo.models.responses.RefreshTokenResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticateRequest request) throws Exception {
        return ResponseEntity.ok().body(authService.authenticate(request));
    }

    @Override
    public ResponseEntity<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {
        return ResponseEntity.ok().body(authService.refreshToken(request));
    }
}
