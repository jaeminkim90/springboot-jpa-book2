package jpabook2.jpashop2.domain.item;

import jpabook2.jpashop2.domain.Category;
import jpabook2.jpashop2.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 부모 클래스 전략 설정
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items") // 반대편 사이드는 mappedBy로 잡아준
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직
    /**
     * 재고 수량 증가 로직
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 수량 감소 로직
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            // 재고가 0보다 작을 경우 예외 발생
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
