package com.apple.shop.item;

import com.apple.shop.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@Table(indexes = @Index(columnList = "title", name = "titleIndex"))
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer price;
    private Integer quantity;
    private String username;
    private String image_url;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    public void removeStock(int count) {
        int restStock = this.quantity - count;
        if (restStock < 0) {
            throw new OutOfStockException("재고부족");
        }
        this.quantity = restStock;
    }
}

