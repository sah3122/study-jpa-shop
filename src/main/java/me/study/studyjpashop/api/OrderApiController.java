package me.study.studyjpashop.api;

import lombok.RequiredArgsConstructor;
import me.study.studyjpashop.domain.Order;
import me.study.studyjpashop.domain.OrderItem;
import me.study.studyjpashop.repository.OrderRepository;
import me.study.studyjpashop.repository.OrderSearch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

}
