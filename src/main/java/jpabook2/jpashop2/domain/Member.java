package jpabook2.jpashop2.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // mappedBy를 이용하면, order 테이블에 있는 Member 필드에 의해서 맵핑된 것을 명시할 수 있다
    private List<Order> orders = new ArrayList<>();
}
