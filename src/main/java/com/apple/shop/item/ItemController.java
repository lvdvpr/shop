package com.apple.shop.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

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
    String addItem(@RequestParam(name="title") String title, @RequestParam(name="price", defaultValue = "0") String price, Authentication auth) {
        itemService.saveItem(title, price, auth.getName());
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

    @GetMapping("/edit/{id}")
    String getEditForm(@PathVariable Long id, Model model) {
        Optional<Item> item = itemService.findItemById(id);
        if (item.isPresent()) {
            model.addAttribute("item", item.get());
            return "edit.html";
        } else {
            return "redirect:/list";
        }
    }

    @PostMapping("/edit")
    String editItem(@RequestParam(name="title") String title, @RequestParam(name="price", defaultValue = "0") String price, Long id) {
        itemService.editItem(title, price, id);
        return "redirect:/list";
    }

    @GetMapping("/test1")
    String test1() {
        System.out.println("요청들어옴");
        return "redirect:/list";
    }

    @PostMapping("/test1")
    String test1(@RequestBody Map<String, Object> body) {
        System.out.println(body.get("name"));
        return "redirect:/list";
    }

    @DeleteMapping("/delete")
    ResponseEntity<String> deleteItem(@RequestParam Long id) {
        itemService.deleteItemById(id);
        return ResponseEntity.status(200).body("삭제완료");
    }



}
