package me.study.studyjpashop.service;

import lombok.RequiredArgsConstructor;
import me.study.studyjpashop.domain.item.Book;
import me.study.studyjpashop.domain.item.Item;
import me.study.studyjpashop.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dongchul on 2019-10-07.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional // dirty checking를 사용하자, Merge를 사용하면 모든 필드가 변경되기때문에 값이 없는 필드는 Null이 업데이트 된다.
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity); // 최대한 setter사용하지말고 method를 만들어서 사용하자.
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
