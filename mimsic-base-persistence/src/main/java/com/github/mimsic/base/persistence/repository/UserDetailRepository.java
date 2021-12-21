package com.github.mimsic.base.persistence.repository;

import com.github.mimsic.base.persistence.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

    List<UserDetail> findByUserId(Long userId);
}
