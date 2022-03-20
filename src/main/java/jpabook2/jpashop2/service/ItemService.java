package jpabook2.jpashop2.service;

import jpabook2.jpashop2.domain.item.Book;
import jpabook2.jpashop2.domain.item.Item;
import jpabook2.jpashop2.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional // class가 readOnly이기 때문에 DB에 반영하기 위해서는 @Transactional이 필요하다
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId); // Id를 기반으로 실제 영속 상태에 있는 엔티티를 찾아 온다.
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        // 이렇게 엔티티매니저를 통해 가져온 엔티티는 수정이 되어도 따로 업데이트 처리를 할 필요가 없다. 변경을 자동으로 감지한다.



    }

    // 전체 Item 찾기
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    // 단일 Item 찾기
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
