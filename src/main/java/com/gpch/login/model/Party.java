package com.gpch.login.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "party_id")
    private int id;

    private String name;

    private String organizerName;

    @OneToMany
    private List<User> guestList = new ArrayList<>();


    public Party(String name) {
        this.name = name;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.organizerName=authentication.getName();
    }

    public void addGuest(User user){
        guestList.add(user);
    }
}
