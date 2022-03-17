package jpabook2.jpashop2.service;

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

    // 전체 Item 찾기
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    // 단일 Item 찾기
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
