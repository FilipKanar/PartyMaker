package com.gpch.login.repository;

import com.gpch.login.model.Gift;
import com.gpch.login.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftRepository extends JpaRepository<Gift,Integer> {
    Gift findById(int id);
    List<Gift> findAllByParty(Party party);
}
