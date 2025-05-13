package com.apple.shop;

import com.apple.shop.exception.OutOfStockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handler() {
        return ResponseEntity.status(400).body("아이고");
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<String> handleOutOfStockException(OutOfStockException ex) {
        log.error("재고부족 에러발생", ex);
        return ResponseEntity.status(400).body("재고부족");
    }
}
