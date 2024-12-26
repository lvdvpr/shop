package com.apple.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping("/list")
    String getItemList(Model model) {
        List<Item> result = itemRepository.findAll();
        model.addAttribute("items", result);
        return "list.html";
    }

    @GetMapping("/write")
    String getWriteForm() {
        return "write.html";
    }

    @PostMapping("/add")
    String addItem(@RequestParam(name="title") String title, @RequestParam(name="price", defaultValue = "0") String price) {
        Item item = new Item();
        item.setTitle(title);
        String numberWithCommas = price;
        String cleanedNumber = numberWithCommas.replace(",", "");
        item.setPrice(Integer.valueOf(cleanedNumber));
        itemRepository.save(item);
        return "redirect:/list";
    }

}
