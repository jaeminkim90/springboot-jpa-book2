package jpabook2.jpashop2.api;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jpabook2.jpashop2.domain.Address;
import jpabook2.jpashop2.domain.Order;
import jpabook2.jpashop2.domain.OrderItem;
import jpabook2.jpashop2.domain.OrderStatus;
import jpabook2.jpashop2.repository.OrderRepository;
import jpabook2.jpashop2.repository.OrderSearch;
import jpabook2.jpashop2.repository.order.query.OrderQueryDto;
import jpabook2.jpashop2.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * V1. 엔티티 직접 노출 - 엔티티가 변하면 API 스펙이 변한다. - 트랜잭션 안에서 지연 로딩 필요 - 양방향 연관관계 문제 * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X) - 트랜잭션 안에서 지연 로딩 필요 V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O) - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경 가능)
 * * V4.JPA에서 DTO로 바로 조회, 컬렉션 N 조회 (1+NQuery) - 페이징 가능 V5.JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1+1Query) - 페이징 가능 V6. JPA에서 DTO로 바로 조회, 플랫 데이터(1Query) (1 Query) - 페이징 불가능... *
 */

@RestController
@RequiredArgsConstructor
public class OrderApiController {

	private final OrderRepository orderRepository;
	private final OrderQueryRepository orderQueryRepository;


	/**
	 * V1. 엔티티 직접 노출 - Hibernate5Module 모듈 등록, LAZY=null 처리 * - 양방향 관계 문제 발생 -> @JsonIgnore
	 */
	@GetMapping("/api/v1/orders")
	public List<Order> ordersV1() {
		List<Order> all = orderRepository.findAllbyString(new OrderSearch());
		for (Order order : all) {
			order.getMember().getName();
			order.getDelivery().getAddress();
			List<OrderItem> orderItems = order.getOrderItems();
			orderItems.stream().forEach(o -> o.getItem().getName());
		}
		return all;
	}

	@GetMapping("/api/v2/orders")
	public List<OrderDto> orderV2() {
		List<Order> orders = orderRepository.findAllbyString(new OrderSearch());
		List<OrderDto> collect = orders.stream()
			.map(OrderDto::new)
			.collect(toList());
		return collect;
	}

	@GetMapping("/api/v3/orders")
	public List<OrderDto> orderV3() {
		List<Order> orders = orderRepository.findAllWithItem();

		List<OrderDto> collect = orders.stream()
			.map(OrderDto::new)
			.collect(toList());
		return collect;
	}

	@GetMapping("/api/v3.1/orders")
	public List<OrderDto> orderV3_page(
		@RequestParam(value = "offset", defaultValue = "0") int offset,
		@RequestParam(value = "limit", defaultValue = "100") int limit) {
		List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

		List<OrderDto> result = orders.stream()
			.map(OrderDto::new)
			.collect(toList());
		return result;
	}

	@GetMapping("/api/v4/orders")
	public List<OrderQueryDto> orderV4() {
		return orderQueryRepository.findOrderQueryDtos();
	}



	@Data
	static class OrderDto {

		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		private List<OrderItemDto> orderItems; // 외부로 나갈때는 OrderItemDto로 wrapping해서 나간다

		public OrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName();
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress();
			orderItems = order.getOrderItems().stream()
				.map(OrderItemDto::new)
				.collect(toList());
		}
	}

	@Data
	static class OrderItemDto {

		// orderItem에서 노출하고 싶은 것을 필드로 포함한다
		private String itemName; // 상품명
		private int orderPrice; // 주문 가격
		private int count; // 주문 수량

		public OrderItemDto(OrderItem orderItem) {
			itemName = orderItem.getItem().getName();
			orderPrice = orderItem.getOrderPrice();
			count = orderItem.getCount();
		}
	}
}
