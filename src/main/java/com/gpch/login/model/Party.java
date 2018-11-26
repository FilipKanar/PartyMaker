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
    private Integer id;

    private String name;

    private String organizerName;

    private String description;

    private String date;

    @ManyToMany
    private List<User> guestList = new ArrayList<>();

    @OneToMany
    private List<Gift> giftList = new ArrayList<>();

    public Party(String name, String date, String description) {
        this.name = name;
        this.date=date;
        this.description=description;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.organizerName=authentication.getName();
    }

    public void addGuest(User user){
        guestList.add(user);
    }
}
