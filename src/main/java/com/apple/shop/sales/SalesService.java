package com.apple.shop.sales;

import com.apple.shop.item.Item;
import com.apple.shop.item.ItemRepository;
import com.apple.shop.member.CustomUser;
import com.apple.shop.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesService {
    private final SalesRepository salesRepository;
    private final ItemRepository itemRepository;


    public void saveSales(Long itemId,
                          String title,
                          Integer price,
                          Integer count,
                          Authentication auth) {

        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            item.get().removeStock(count);
            Sales sales = new Sales();
            sales.setCount(count);
            sales.setPrice(price);
            sales.setItemName(title);
            CustomUser user = (CustomUser) auth.getPrincipal();
            Member member = new Member();
            member.setId(user.getId());
            sales.setMember(member);
            salesRepository.save(sales);
        }
    }
}
