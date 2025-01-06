package com.apple.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping("/list")
    String getItemList(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "list.html";
    }

    @GetMapping("/write")
    String getWriteForm() {

        return "write.html";
    }


    @PostMapping("/add")
    String addItem(@RequestParam(name="title") String title, @RequestParam(name="price", defaultValue = "0") String price) {
        // Object 만들기
        Item item = new Item();
        // title 값 넣기
        item.setTitle(title);
        // price 값 넣기
        String numberWithCommas = price;
        String cleanedNumber = numberWithCommas.replace(",", "");
        item.setPrice(Integer.valueOf(cleanedNumber));
        itemRepository.save(item);
        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String detail(@PathVariable Long id, Model model) {
        Optional<Item> item = itemRepository.findById(id);
        if ( item.isPresent() ) {
            model.addAttribute("item", item.get());
        } else {
            model.addAttribute("errorMessage", "상품이 없습니다.");
        }
        return "detail.html";
    }

}
