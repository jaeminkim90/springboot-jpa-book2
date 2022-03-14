package jpabook2.jpashop2.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders") // 별도로 지정해주지 않으면 "order"로 테이블을 생성한다
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY) // order와 member는 다대일 관계
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = ALL) // cascade를 All로 설정하면 order가 영속성 관리에 들어갈 때 orderItems에도 반영된다.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = ALL) // order를 저장할 때 delivery도 persist 처리한다.
    @JoinColumn(name = "delivery_id") // 일대일 관계에서는 비즈니스 주도권이 있는 쪽을 주인으로 매핑하는 것이 좋다
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) // enum 타입을 명시해야 하는데, STRING을 쓰는 것이 좋다.  ORDINAL은 숫자가 들어가므로, 사용해선 안된다.
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]
}
