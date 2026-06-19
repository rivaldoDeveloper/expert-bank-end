package br.com.rivaldo.helpdeskbff.service;

import br.com.rivaldo.helpdeskbff.cliente.OrderFeignClient;
import br.com.rivaldo.models.dtos.OrderCreatedMessage;
import br.com.rivaldo.models.requests.CreateOrderRequest;
import br.com.rivaldo.models.requests.UpdateOrderRequest;
import br.com.rivaldo.models.responses.OrderResponse;
import br.com.rivaldo.models.responses.UserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.rivaldo.models.enums.OrderStatusEnum.CLOSED;
import static java.time.LocalDateTime.now;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderFeignClient orderFeignClient;

    public OrderResponse findById(final Long id) {
        return orderFeignClient.findById(id).getBody();
    }

    public void save(CreateOrderRequest request) {
        orderFeignClient.save(request);
    }

    public OrderResponse update(Long id, UpdateOrderRequest request) {
        return orderFeignClient.update(id, request).getBody();
    }

    public void deleteBtyId(final Long id) {
        orderFeignClient.deleteById(id);
    }

    public List<OrderResponse> findAll() {
        return orderFeignClient.findAll().getBody();
    }

    public Page<OrderResponse> findAllPaged(Integer page, Integer linesPerPage, String direction, String orderBy) {
        return orderFeignClient.findAllPaged(page, linesPerPage, direction, orderBy).getBody();
    }
}
