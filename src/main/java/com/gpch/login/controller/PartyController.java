package com.gpch.login.controller;


import com.gpch.login.model.Party;
import com.gpch.login.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PartyController {

    @Autowired
    PartyRepository partyRepository;

    @PostMapping("/addParty")
    public String addParty(@RequestParam (value = "name",required = false) String name,
                           ModelMap modelMap){
        if(name != null) {
            partyRepository.save(new Party(name));
        }
        return "redirect:/addParty";
    }

    @GetMapping("/addParty")
    public String addPartyGet(ModelMap modelMap){
        List<Party> myParties = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        for (Party party : partyRepository.findAll()) {
            if (party.getOrganizerName().equals(authentication.getName())) {
                myParties.add(party);
            }
        }
        modelMap.addAttribute("myParties", myParties);
        return "admin/home";
    }

    @PostMapping("/partySettings")
    public String partySettings(ModelMap modelMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser=authentication.getName();
        modelMap.put("name", currentUser);
        return "party/partySettings";
    }

    @PostMapping("/update")
    public String updateParty(@RequestParam("partyId") int partyID,
                              ModelMap modelMap){
        modelMap.addAttribute("party",partyRepository.findById(partyID));
        return "party/update";
    }

    @PostMapping("/updateEntity")
    public String updateEntity(@RequestParam("id") int id,
                               @RequestParam("name") String name){
        Party party = partyRepository.findById(id);
        party.setName(name);
        partyRepository.save(party);
        return "redirect:/addParty";
    }


}
