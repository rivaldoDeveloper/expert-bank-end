package br.com.rivaldo.helpdeskbff.controller.impl;

import br.com.rivaldo.helpdeskbff.controller.OrderController;
import br.com.rivaldo.helpdeskbff.service.OrderService;
import br.com.rivaldo.models.requests.CreateOrderRequest;
import br.com.rivaldo.models.requests.UpdateOrderRequest;
import br.com.rivaldo.models.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService service;

    @Override
    public ResponseEntity<OrderResponse> findById(Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @Override
    public ResponseEntity<List<OrderResponse>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @Override
    public ResponseEntity<Void> save(CreateOrderRequest request) {
        service.save(request);
        return ResponseEntity.status(CREATED).build();
    }

    @Override
    public ResponseEntity<OrderResponse> update(Long id, UpdateOrderRequest request) {
        return ResponseEntity.ok().body(service.update(id, request));
    }

    @Override
    public ResponseEntity<Void> deleteById(final Long id) {
        service.deleteBtyId(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Page<OrderResponse>> findAllPaged(Integer page, Integer linesPerPage, String direction, String orderBy) {
        return ResponseEntity.ok().body(service.findAllPaged(page, linesPerPage, direction, orderBy));
    }
}
