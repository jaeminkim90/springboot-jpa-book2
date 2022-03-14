package jpabook2.jpashop2.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery") // 거울 참조 매핑
    private Order order;

    @Embedded // 내장 타입
    private Address address;

    @Enumerated(EnumType.STRING) // enum 타입을 명시해야 하는데, STRING을 쓰는 것이 좋다.  ORDINAL은 숫자가 들어가므로, 사용해선 안된다.
    private DeliveryStatus status; // READY, COMPLETE

}
