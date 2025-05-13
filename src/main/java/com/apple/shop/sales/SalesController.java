package com.apple.shop.sales;

import com.apple.shop.member.CustomUser;
import com.apple.shop.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
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
public class SalesController {

    private final SalesRepository salesRepository;
    private final SalesService salesService;
    private final MemberRepository memberRepository;

    @PostMapping("/order")
    public String order(@RequestParam Long itemId,
                        @RequestParam String title,
                        @RequestParam String price,
                        @RequestParam Integer count,
                        Authentication auth) {

        int parsedPrice = Integer.parseInt(price.replaceAll("[,원]",""));
        salesService.saveSales(itemId, title, parsedPrice, count, auth);
        return "redirect:/list";
    }

    @GetMapping({"/order/all", "/order/all/{pageNum}"})
    public String getOrderAll(Model model, @PathVariable(required = false) Optional<Integer> pageNum) {
        int page;
        if (pageNum.isPresent()) {
            page = pageNum.get();
        } else {
            page = 1;
        }
        Page<Sales> salesList = salesRepository.customFindAll(PageRequest.of(page-1, 10));
        List<SalesDto> salesDtos = new ArrayList<>();
        for (Sales sale : salesList) {
            SalesDto dto = new SalesDto();
            dto.setItemName(sale.getItemName());
            dto.setPrice(sale.getPrice());
            dto.setUsername(sale.getMember().getUsername());
            dto.setCount(sale.getCount());
            dto.setCreated(sale.getCreated());
            salesDtos.add(dto);
        }
        model.addAttribute("salesList", salesDtos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", salesList.getTotalPages());
        model.addAttribute("hasNext", salesList.hasNext());
        model.addAttribute("hasPrevious", salesList.hasPrevious());

        int blockSize = 5;
        int currentBlock = (int) Math.ceil((double) page / blockSize);
        int startPage = (currentBlock - 1) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, salesList.getTotalPages());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "salesList.html";
    }

    @GetMapping({"/myOrder/all", "/myOrder/all/{pageNum}"})
    public String getMyOrderAll(Model model, Authentication auth, @PathVariable(required = false) Optional<Integer> pageNum) {
        int page;
        if (pageNum.isPresent()) {
            page = pageNum.get();
        } else {
            page = 1;
        }
        CustomUser user = (CustomUser) auth.getPrincipal();
        Page<Sales> orderList = salesRepository.findByMemberId(user.getId(), PageRequest.of(page-1, 10));
        List<SalesDto> orderDtos = new ArrayList<>();
        for (Sales sale : orderList) {
            SalesDto dto = new SalesDto();
            dto.setItemName(sale.getItemName());
            dto.setPrice(sale.getPrice());
            dto.setCount(sale.getCount());
            dto.setCreated(sale.getCreated());
            orderDtos.add(dto);
        }
        model.addAttribute("orderList", orderDtos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderList.getTotalPages());
        model.addAttribute("hasNext", orderList.hasNext());
        model.addAttribute("hasPrevious", orderList.hasPrevious());

        int blockSize = 5;
        int currentBlock = (int) Math.ceil((double) page / blockSize);
        int startPage = (currentBlock - 1) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, orderList.getTotalPages());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "myOrderList.html";
    }

}
