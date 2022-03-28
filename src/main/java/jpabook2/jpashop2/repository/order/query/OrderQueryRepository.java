package jpabook2.jpashop2.repository.order.query;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

	private final EntityManager em;

	public List<OrderQueryDto> findOrderQueryDtos() {
		List<OrderQueryDto> result = findOrders(); // 컬렉션을 제외하고 OrderQueryDto의 값을 가져오는 쿼리 실행

		// OrderQueryDto의 컬렉션 필드룰 직접 채운다
		result.forEach(o -> {
			List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
			o.setOrderItems(orderItems);

		});
		return result;
	}

	public List<OrderQueryDto> findAllByDto_optimization() {
		// 루트 조회(toOne 코드를 모두 한번에 조회)
		List<OrderQueryDto> result = findOrders();

		// OrderItem 정보가 한 번에 조회된다
		Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));

		// result를 돌면서 OrderQueryDto에 OrderItems에 OrderId기 일치하는 OrderItemQueryDto을 set한다
		result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

		return result; // 쿼리를 한 번만 날리고 메모리에서 map으로 가져와서 값을 세팅하기 때문에 쿼리가 총 2번만 나감
	}

	private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
		// OrderQueryDto의 orderId를 모두 모아온다 -> user 4번, 11번
		List<OrderItemQueryDto> orderItems = em.createQuery(
				"select new jpabook2.jpashop2.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
					" from OrderItem oi" +
					" join oi.item i" +
					" where oi.order.id in :orderIds", OrderItemQueryDto.class)
			.setParameter("orderIds", orderIds)
			.getResultList();

		// orderItems를 map으로 변환(key = orderId / value = OrderItemQueryDto )
		Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
			.collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
		return orderItemMap;
	}

	private List<Long> toOrderIds(List<OrderQueryDto> result) {
		List<Long> orderIds = result.stream()
			.map(OrderQueryDto::getOrderId)
			.collect(Collectors.toList());
		return orderIds;
	}

	private List<OrderItemQueryDto> findOrderItems(Long orderId) {
		return em.createQuery(
				"select new jpabook2.jpashop2.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
					" from OrderItem oi" +
					" join oi.item i" +
					" where oi.order.id= :orderId", OrderItemQueryDto.class)
			.setParameter("orderId", orderId)
			.getResultList();
	}

	private List<OrderQueryDto> findOrders() {
		return em.createQuery(
				"select new jpabook2.jpashop2.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
					" from Order o" +
					" join o.member m" +
					" join o.delivery d", OrderQueryDto.class)
			.getResultList();
	}


}
