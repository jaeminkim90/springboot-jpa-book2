package jpabook2.jpashop2.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    private Long id; // 상품 수정이 있기 때문에 id 값이 필요하다

    private String name;
    private int price;
    private int stockQuantity;

    private String author; // 저자
    private String isbn;
}
