package com.apple.shop.item;

import com.apple.shop.comment.Comment;
import com.apple.shop.comment.CommentRepository;
import com.apple.shop.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final S3Service s3Service;
    private final CommentService commentService;

    @GetMapping("/write")
    String getWriteForm() {
        return "write.html";
    }

    @PostMapping("/add")
    String addItem(@RequestParam(name="title") String title, @RequestParam(name="price", defaultValue = "0") String price, String quantity, String image_url, Authentication auth) {
        if (image_url.trim().isEmpty()) {
            image_url = null; // 빈 문자열을 null로 변환
        }
        itemService.saveItem(title, price, quantity, image_url, auth.getName());
        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String detail(@PathVariable Long id, Model model) {
        Optional<Item> item = itemService.findItemById(id);
        List<Comment> commentList = commentService.findAllCommentById(id);
        if ( item.isPresent()) {
            model.addAttribute("item", item.get());
            model.addAttribute("commentList", commentList);
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
    String editItem(@RequestParam(name="title") String title, @RequestParam(name="price", defaultValue = "0") String price, Integer quantity, Long id,  String image_url, Authentication auth, LocalDateTime regTime) {
        if (image_url.trim().isEmpty()) {
            image_url = null;
        }
        itemService.editItem(title, price, quantity, id, image_url, auth.getName(), regTime);
        return "redirect:/list";
    }

    @DeleteMapping("/delete")
    ResponseEntity<String> deleteItem(@RequestParam Long id) {
        itemService.deleteItemById(id);
        return ResponseEntity.status(200).body("삭제완료");
    }

    @GetMapping({"/list", "/list/{pageNum}"})
    String getListPage(Model model, @PathVariable(required = false) Optional<Integer> pageNum) {
        int page;
        if (pageNum.isPresent()) {
            page = pageNum.get();
        } else {
            page = 1;
        }
        Page<Item> result = itemRepository.findPageBy(PageRequest.of(page-1, 10));
        model.addAttribute("itemList", result);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("hasNext", result.hasNext());
        model.addAttribute("hasPrevious", result.hasPrevious());

        int blockSize = 5;
        int currentBlock = (int) Math.ceil((double) page / blockSize);
        int startPage = (currentBlock - 1) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, result.getTotalPages());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "list.html";
    }

    @GetMapping("/presigned-url")
    @ResponseBody
    String getURL(@RequestParam String filename) {
        var presignedURL = s3Service.createPresignedUrl("test/" + filename);

        return presignedURL;
    }

    @GetMapping({"/search", "/search/{pageNum}"})
    String searchPost(@RequestParam(name="searchText") String searchText, Model model, @PathVariable(required = false) Optional<Integer> pageNum) {
        int searchPage;
        if (pageNum.isPresent()) {
            searchPage = pageNum.get();
        } else {
            searchPage = 1;
        }
        Page<Item> result = itemRepository.fullTextSearch(searchText, PageRequest.of(searchPage-1, 10));
        model.addAttribute("itemList", result);
        model.addAttribute("currentPage", searchPage);
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("hasNext", result.hasNext());
        model.addAttribute("hasPrevious", result.hasPrevious());
        model.addAttribute("searchText", searchText);

        int blockSize = 5;
        int currentBlock = (int) Math.ceil((double) searchPage / blockSize);
        int startPage = (currentBlock - 1) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, result.getTotalPages());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "searchList.html";
    }

    @GetMapping("/presigned-new-url")
    @ResponseBody
    String getNewURL(@RequestParam String filename) {
        var presignedNewURL = s3Service.createPresignedUrl("test/" + filename);

        return presignedNewURL;
    }



}
