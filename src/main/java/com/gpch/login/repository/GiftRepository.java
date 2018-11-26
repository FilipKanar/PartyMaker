package com.gpch.login.repository;

import com.gpch.login.model.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftRepository extends JpaRepository<Gift,Integer> {
    Gift findById(int id);
}
