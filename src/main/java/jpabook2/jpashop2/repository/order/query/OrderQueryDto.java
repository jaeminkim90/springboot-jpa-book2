package jpabook2.jpashop2.repository.order.query;

import java.time.LocalDateTime;
import java.util.List;
import jpabook2.jpashop2.domain.Address;
import jpabook2.jpashop2.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderQueryDto {

	private Long orderId;
	private String name;
	private LocalDateTime localDateTime;
	private OrderStatus orderStatus;
	private Address address;
	private List<OrderItemQueryDto> orderItems;

	public OrderQueryDto(Long orderId, String name, LocalDateTime localDateTime, OrderStatus orderStatus, Address address) {
		this.orderId = orderId;
		this.name = name;
		this.localDateTime = localDateTime;
		this.orderStatus = orderStatus;
		this.address = address;
	}
}
