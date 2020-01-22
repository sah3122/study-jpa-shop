package me.study.studyjpashop.repository;

import lombok.RequiredArgsConstructor;
import me.study.studyjpashop.domain.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {

        String jpql = "select o from Order o join 0.member m";
        boolean isFristCondition = true;

        //주문 상태 검색
        if(orderSearch.getOrderStatus() != null) {
            if(isFristCondition) {
                jpql += " where";
                isFristCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            if(isFristCondition) {
                jpql += " where";
                isFristCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery("select o from Order o join o.member m", Order.class)
                .setMaxResults(1000);

        if(orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();

//        return em.createQuery("select o from Order o join o.member m" +
//                    " where o.status = :status " +
//                    "and m.name like :name", Order.class)
//        .setParameter("status", orderSearch.getOrderStatus())
//        .setParameter("name", orderSearch.getMemberName())
//        .setMaxResults(1000)
//        .getResultList();
    }
    
    //fetch join은 쿼리에서 데이터를 다 가져오기때문에 lazy loading 과 같은 이슈가 발생하지 않는다.
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).getResultList();
    }
}
