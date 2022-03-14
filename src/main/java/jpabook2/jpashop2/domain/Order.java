package jpabook2.jpashop2.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "orders") // 별도로 지정해주지 않으면 "order"로 테이블을 생성한다
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne // order와 member는 다대일 관계
    @JoinColumn(name = "member_id")
    private Member member;

}
