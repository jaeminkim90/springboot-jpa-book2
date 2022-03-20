package jpabook2.jpashop2.repository;

import jpabook2.jpashop2.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item); // merge는 전체 필드를 교체하기 때문에 null을 주의해야 한다
        }
    }

    // 단건 조회는 PK로 해당하는 엔티티를 find 한다.
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    // 전체 조회
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
