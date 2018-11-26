package com.gpch.login.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
public class Gift {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gift_id")
    private int id;

    private String name;

    private Double price;

    @ManyToOne
    private User user;

    @ManyToOne
    private Party party;

    public Gift(String name, Double price,Party party) {
        this.name = name;
        this.price = price;
        this.party=party;
    }
}
