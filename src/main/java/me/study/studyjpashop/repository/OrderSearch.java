package me.study.studyjpashop.repository;

import lombok.Getter;
import lombok.Setter;
import me.study.studyjpashop.domain.OrderStatus;

@Getter
@Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;

}
