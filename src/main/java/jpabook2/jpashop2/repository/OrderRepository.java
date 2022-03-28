package jpabook2.jpashop2.repository;

import jpabook2.jpashop2.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

	private final EntityManager em;

	public void save(Order order) {
		em.persist(order);
	}

	public Order findOne(Long id) {
		return em.find(Order.class, id);
	}

	// 검색용으로 나중에 구현 예정
	public List<Order> findAllbyString(OrderSearch orderSearch) {

		//language=JPAQL
		String jpql = "select o From Order o join o.member m";
		boolean isFirstCondition = true;

		//주문 상태 검색
		if (orderSearch.getOrderStatus() != null) { // status가 존재하면
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " o.status = :status";
		}

		//회원 이름 검색
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " m.name like :name";
		}

		TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000); //최대 1000건

		if (orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}

		if (StringUtils.hasText(orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}

		return query.getResultList();
	}

	/**
	 * JPA Criteria: 권장하는 방식은 아니다
	 */
	public List<Order> findAllByCriteria(OrderSearch orderSearch) {
		CriteriaBuilder cb = em.getCriteriaBuilder(); // 엔티티매니저에서 CriteriaBuilder를 얻어온다.
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);// 응답 타입 세팅
		Root<Order> o = cq.from(Order.class);
		Join<Object, Object> m = o.join("member", JoinType.INNER);

		List<Predicate> criteria = new ArrayList<>();

		// 주문 상태 검색
		if (orderSearch.getOrderStatus() != null) {
			Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
			criteria.add(status);
		}

		// 회원 이름 검색
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			Predicate name =
				cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
			criteria.add(name);
		}
		cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
		TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
		return query.getResultList();

	}

	// 엔티티를 페치 조인(fetch join)을 사용해서 쿼리 1번에 조회
	// 페치 조인으로 order -> member , order -> delivery 는 이미 조회 된 상태 이므로 지연로딩X
	public List<Order> findAllWithMemberDelivery() {
		return em.createQuery(
			"select o from Order o"
				+ " join fetch o.member m"
				+ " join fetch o.delivery d", Order.class).getResultList();
	}

	public List<Order> findAllWithItem() {
		return em.createQuery(
				"select distinct o from Order o"
					+ " join fetch o.member m"
					+ " join fetch o.delivery d"

					+ " join fetch o.orderItems oi"
					+ " join fetch oi.item i", Order.class)
			.getResultList();
	}

//        return em.createQuery("select o from Order o join o.member m" +
//                        " where o.status = :status" +
//                        " and m.name like :name", Order.class)
//                .setParameter("status", orderSearch.getOrderStatus())
//                .setParameter("name", orderSearch.getMemberName())
//                .setFirstResult(100) // 페이징을 사용할 때 시작 지점이 된다
//                .setMaxResults(1000) // 조회결과를 최대 1000개로 제한한다
//                .getResultList();
}

