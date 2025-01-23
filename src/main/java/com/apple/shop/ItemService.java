package com.apple.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public void saveItem(String title, String price) {
        // Object 만들기
        Item item = new Item();
        // title 값 넣기
        item.setTitle(title);
        // price 값 넣기
        String numberWithCommas = price;
        String cleanedNumber = numberWithCommas.replace(",", "");
        item.setPrice(Integer.valueOf(cleanedNumber));
        itemRepository.save(item);
    }

    public List<Item> findAllItem() {
        List<Item> itemList = itemRepository.findAll();
        return itemList;
    }

    public Optional<Item> findItemById (Long id) {
        Optional<Item> item = itemRepository.findById(id);
        return item;
    }
}
