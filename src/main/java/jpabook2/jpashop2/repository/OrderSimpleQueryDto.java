package jpabook2.jpashop2.repository;

import java.time.LocalDateTime;
import jpabook2.jpashop2.domain.Address;
import jpabook2.jpashop2.domain.Order;
import jpabook2.jpashop2.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderSimpleQueryDto {

	private Long orderId;
	private String name;
	private LocalDateTime orderDate; // 주문 시간
	private OrderStatus orderStatus;
	private Address address;

	// 생성자에서 order의 데이터를 옮긴다
	public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
		this.orderId = orderId;
		this.name = name;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address;
	}
}
