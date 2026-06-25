package br.com.rivaldo.helpdeskbff.cliente;

import br.com.rivaldo.helpdeskbff.config.FeignConfig;
import br.com.rivaldo.models.requests.CreateUserRequest;
import br.com.rivaldo.models.requests.UpdateUserRequest;
import br.com.rivaldo.models.responses.UserResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "user-service-api",
        path = "/api/users",
        url = "${USER_SERVICE_URL:http://user-service-api:8088}",
        configuration = FeignConfig.class
)
public interface UserFeignClient {

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(@PathVariable(name = "id") final String id);

    @PostMapping
    ResponseEntity<Void> save(@Valid @RequestBody final CreateUserRequest createUserRequest);

    @GetMapping
    ResponseEntity<List<UserResponse>> findAll();

    @PutMapping("/{id}")
    ResponseEntity<UserResponse> update(
            @PathVariable(name = "id") final String id,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    );
}