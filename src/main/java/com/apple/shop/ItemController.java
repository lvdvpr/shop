package com.apple.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;

    @GetMapping("/list")
    String getItemList(Model model) {
        List<Item> itemList = itemService.findAllItem();
        model.addAttribute("itemList", itemList);
        return "list.html";
    }

    @GetMapping("/write")
    String getWriteForm() {
        return "write.html";
    }


    @PostMapping("/add")
    String addItem(@RequestParam(name="title") String title, @RequestParam(name="price", defaultValue = "0") String price) {
        itemService.saveItem(title, price);
        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String detail(@PathVariable Long id, Model model) {
        Optional<Item> item = itemService.findItemById(id);
        if ( item.isPresent()) {
            model.addAttribute("item", item.get());
            return "detail.html";
        } else {
            return "redirect:/list";
        }
    }

}
