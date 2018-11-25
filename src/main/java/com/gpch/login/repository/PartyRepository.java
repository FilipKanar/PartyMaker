package com.gpch.login.repository;

import com.gpch.login.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRepository extends JpaRepository<Party,Integer> {
    Party findById(int id);
}
