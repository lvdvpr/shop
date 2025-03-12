package com.apple.shop.sales;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SalesDto {
    private String itemName;
    private Integer price;
    private String username;
}
