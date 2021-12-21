package com.github.mimsic.base.persistence.repository;

import com.github.mimsic.base.persistence.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

    @Query("SELECT o FROM Order o WHERE o.accId = ?1")
    List<Order> findByAccId(Long accId);

    Slice<Order> findByAccId(Long accId, Pageable pageable);
}
