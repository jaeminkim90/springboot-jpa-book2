package jpabook2.jpashop2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore // JSON 응답 시, 해당 필드가 제외된다.
    @OneToMany(mappedBy = "member") // mappedBy를 이용하면, order 테이블에 있는 Member 필드에 의해서 맵핑된 것을 명시할 수 있다
    // mappedBy를 적는 순간 member 필드에 의해 매핑된 거울일 뿐이라는 것을 명시한다
    private List<Order> orders = new ArrayList<>(); // 컬렉션은 필드에서 바로 초기화하는 것이 안전하다
}
