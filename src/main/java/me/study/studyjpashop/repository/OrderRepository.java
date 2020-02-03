package me.study.studyjpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.study.studyjpashop.domain.Order;
import me.study.studyjpashop.domain.OrderStatus;
import me.study.studyjpashop.domain.QMember;
import me.study.studyjpashop.domain.QOrder;
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

    public List<Order> findAllOld(OrderSearch orderSearch) {

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

    public List<Order> findAll(OrderSearch orderSearch) {
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        JPAQueryFactory query = new JPAQueryFactory(em);
        return query
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }


    private BooleanExpression nameLike(String membername) {
        if (!StringUtils.hasText(membername)) {
            return null;
        }
        return QMember.member.name.like(membername);
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        if (statusCond == null) {
            return null;
        }
        return QOrder.order.status.eq(statusCond);
    }
    
    //fetch join은 쿼리에서 데이터를 다 가져오기때문에 lazy loading 과 같은 이슈가 발생하지 않는다.
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).getResultList();
    }

    // join 으로 인해 중복된 데이터가 발생하기 때문에 distinct 를 활용해서 중복을 제거 해준다.
    // DataBase에서는 중복 제거가 되지 않으나 JPA가 Collection을 만들때 id값을 비교하여 제거함.
    public List<Order> findAllWitItem() {
        return em.createQuery(
                "select distinct o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d" +
                " join fetch  o.orderItems oi" +
                " join  fetch oi.item i", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
