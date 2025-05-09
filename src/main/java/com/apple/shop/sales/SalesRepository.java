package com.apple.shop.sales;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SalesRepository extends JpaRepository<Sales,Integer> {

    @Query(value = "SELECT s FROM Sales s JOIN FETCH s.member")
    Page<Sales> customFindAll(Pageable pageable);

    Page<Sales> findByMemberId(Long memberId, Pageable pageable);
}
