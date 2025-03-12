package com.apple.shop.sales;

import com.apple.shop.member.CustomUser;
import com.apple.shop.member.Member;
import com.apple.shop.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String order(@RequestParam String title,
                        @RequestParam Integer price,
                        @RequestParam Integer count,
                        Authentication auth) {
        salesService.saveSales(title, price, count, auth);
        return "redirect:/list";
    }

    @GetMapping("/order/all")
    @ResponseBody
    public List<SalesDto> getOrderAll(){
        List<Sales> salesList = salesRepository.customFindAll();
        List<SalesDto> salesDtos = new ArrayList<>();
        for (Sales sale : salesList) {
            SalesDto dto = new SalesDto();
            dto.setItemName(sale.getItemName());
            dto.setPrice(sale.getPrice());
            dto.setUsername(sale.getMember().getUsername());
            salesDtos.add(dto);
        }
        return salesDtos;
    }

}
