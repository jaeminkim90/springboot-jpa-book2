package jpabook2.jpashop2.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jpabook2.jpashop2.domain.Address;
import jpabook2.jpashop2.domain.Order;
import jpabook2.jpashop2.domain.OrderStatus;
import jpabook2.jpashop2.repository.OrderRepository;
import jpabook2.jpashop2.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;

	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1() {
		List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
		for (Order order : all) {
			order.getMember().getName(); //Lazy 강제 초기화
			order.getDelivery().getAddress(); //Lazy 강제 초기화
		}
		return all;
	}

	/**
	 * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
	 * 단점: 지연로딩으로 쿼리 N번 호출
	 */
	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderDto> orderV2() {

		// 결과 주문 수가 2개 -> 루프가 2번 돈다
		// 1 + N 문제 -> 첫번째 실행의 결과로 N개 만큼의 주문이 있을 시 N번의 쿼리가 실행된다
		return orderRepository.findAllbyString(new OrderSearch()).stream()
			.map(SimpleOrderDto::new)
			.collect(Collectors.toList());
	}

	@Data
	static class SimpleOrderDto {

		private Long orderId;
		private String name;
		private LocalDateTime orderDate; // 주문 시간
		private OrderStatus orderStatus;
		private Address address;


		// 생성자에서 order의 데이터를 옮긴다
		public SimpleOrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName(); // LAZY 초기화
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress(); // LAZY 초기화
		}
	}
}
