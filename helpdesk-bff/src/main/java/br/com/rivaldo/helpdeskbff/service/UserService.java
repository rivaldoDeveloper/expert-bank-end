package br.com.rivaldo.helpdeskbff.service;

import br.com.rivaldo.helpdeskbff.cliente.UserFeignClient;
import br.com.rivaldo.models.requests.CreateUserRequest;
import br.com.rivaldo.models.requests.UpdateUserRequest;
import br.com.rivaldo.models.responses.UserResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserFeignClient userFeignClient;

    public UserResponse findById(final String id) {
        return userFeignClient.findById(id).getBody();
    }

    public void save(CreateUserRequest request) {
        userFeignClient.save(request);
    }

    public List<UserResponse> findAll() {
        return userFeignClient.findAll().getBody();
    }

    public UserResponse update(final String id, final UpdateUserRequest request) {
        return userFeignClient.update(id, request).getBody();
    }

}
