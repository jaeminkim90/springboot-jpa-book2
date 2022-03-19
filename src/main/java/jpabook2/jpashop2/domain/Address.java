package jpabook2.jpashop2.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;

@Embeddable // 어딘가에 내장이 될 수 있다
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // 기본 생성자도 필요하다. 리플렉션이나 프록시에 사용된다. protected를 쓰는게 그나마 더 안전하다.
    protected Address(String city) {
        this.city = city;
    }

    // @Setter 제공하지 않고, 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만드는 것이 좋은 설계 방법이다.
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
