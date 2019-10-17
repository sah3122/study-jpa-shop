package me.study.studyjpashop.service;

import lombok.RequiredArgsConstructor;
import me.study.studyjpashop.domain.Delivery;
import me.study.studyjpashop.domain.Member;
import me.study.studyjpashop.domain.Order;
import me.study.studyjpashop.domain.OrderItem;
import me.study.studyjpashop.domain.item.Item;
import me.study.studyjpashop.repository.ItemRepository;
import me.study.studyjpashop.repository.MemberRepository;
import me.study.studyjpashop.repository.OrderRepository;
import me.study.studyjpashop.repository.OrderSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dongchul on 2019-10-08.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order); // cascade option 때문에 order만 persist해도 연관된 orderitem과 delivery가 저장된다.

        return order.getId();
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }

    //검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
}
