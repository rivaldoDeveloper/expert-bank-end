package br.com.rivaldo.helpdeskbff.cliente;

import br.com.rivaldo.helpdeskbff.config.FeignConfig;
import br.com.rivaldo.models.requests.CreateOrderRequest;
import br.com.rivaldo.models.requests.UpdateOrderRequest;
import br.com.rivaldo.models.responses.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "order-service-api",
        path = "/api/orders",
        url = "${ORDER_SERVICE_URL:http://order-service-api:8100}",
        configuration = FeignConfig.class
)
public interface OrderFeignClient {

    @GetMapping("/{id}")
    ResponseEntity<OrderResponse> findById(@PathVariable(name = "id") final Long id);

    @GetMapping
    ResponseEntity<List<OrderResponse>> findAll();

    @PostMapping
    ResponseEntity<Void> save(@Valid @RequestBody final CreateOrderRequest request);

    @PutMapping("/{id}")
    ResponseEntity<OrderResponse> update(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody final UpdateOrderRequest request
    );

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable(name = "id") final Long id);

    @GetMapping("/pages")
    ResponseEntity<Page<OrderResponse>> findAllPaged(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy
    );
}