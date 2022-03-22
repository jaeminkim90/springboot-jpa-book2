package jpabook2.jpashop2;

import jpabook2.jpashop2.domain.*;
import jpabook2.jpashop2.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 주문 1 -> userA : JPA1 BOOK / JPA2 BOOK
 * 주문 2 -> userB : SPRING1 BOOK / SPRING2 BOOK
 */
@Component // 스프링의 Component Sacn 대상이 된다
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct // 생성 직후 실행되는 메서드 Annotation
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {

            Member member = new Member();
            member.setName("userA");
            member.setAddress(new Address("서울", "1번가", "111-222"));
            em.persist(member); // 새로 생성한 Member를 영속성 컨텍스트에 추가

            Book book1 = new Book();
            book1.setName("JPA1 BOOK");
            book1.setPrice(10000);
            book1.setStockQuantity(100);
            em.persist(book1);

            Book book2 = new Book();
            book2.setName("JPA2 BOOK");
            book2.setPrice(20000);
            book2.setStockQuantity(100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);


        }
    }
}

