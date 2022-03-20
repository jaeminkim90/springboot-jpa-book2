package jpabook2.jpashop2.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {
    // member 정보를 담는 form

    @NotEmpty(message = "회원 이름은 필수 입니다.") // 스프링이 자동으로 validation 한다. 빈값 방지
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
