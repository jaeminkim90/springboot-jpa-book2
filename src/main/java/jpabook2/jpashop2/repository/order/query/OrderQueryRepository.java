package jpabook2.jpashop2.repository.order.query;


import java.util.List;
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
