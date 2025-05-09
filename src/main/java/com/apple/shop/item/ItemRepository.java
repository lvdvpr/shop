package com.apple.shop.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findPageBy(Pageable page);

    @Query(value="SELECT * FROM item WHERE(CHAR_LENGTH(?1)=1 AND title LIKE CONCAT('%', ?1, '%')) OR (CHAR_LENGTH(?1)>1 AND MATCH(title) AGAINST(?1))", nativeQuery=true)
    Page<Item> fullTextSearch(String title, Pageable pageable);
}
