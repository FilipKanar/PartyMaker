package com.gpch.login.controller;


import com.gpch.login.model.Gift;
import com.gpch.login.model.Party;
import com.gpch.login.model.User;
import com.gpch.login.repository.GiftRepository;
import com.gpch.login.repository.PartyRepository;
import com.gpch.login.repository.UserRepository;
import com.gpch.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PartyController {

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    UserService userService;

    @Autowired
    GiftRepository giftRepository;

    @PostMapping("/addParty")
    public String addParty(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "description", required = false) String description,
                           @RequestParam(value = "date", required = false) String date,
                           ModelMap modelMap) {
        if (name != null) {
            partyRepository.save(new Party(name, date, description));
        }
        return "redirect:/addParty";
    }

    @GetMapping("/addParty")
    public ModelAndView addPartyGet() {
        ModelAndView modelAndView = new ModelAndView();
        List<Party> myParties = new ArrayList<>();
        List<Party> iAmInvitedParties = new ArrayList<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        for (Party party : partyRepository.findAll()) {
            if (party.getOrganizerName().equals(authentication.getName())) {
                myParties.add(party);
            }
            for (User tempUser : party.getGuestList()) {
                if (tempUser.getEmail().equals(authentication.getName())) {
                    iAmInvitedParties.add(party);
                }

            }
        }
        modelAndView.addObject("myParties", myParties);
        modelAndView.addObject("invitedToParties", iAmInvitedParties);
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @PostMapping("/partySettings")
    public String partySettings(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        modelMap.put("name", currentUser);
        return "party/partySettings";
    }

    @PostMapping("/update")
    public String updateParty(@RequestParam("id") int partyID,
                              ModelMap modelMap) {
        modelMap.addAttribute("party", partyRepository.findById(partyID));
        return "party/update";
    }

    @PostMapping("/updateEntity")
    public String updateEntity(@RequestParam("id") int id,
                               @RequestParam("name") String name,
                               @RequestParam("description") String description,
                               @RequestParam("date") String date) {
        Party party = partyRepository.findById(id);
        party.setName(name);
        party.setDate(date);
        party.setDescription(description);
        partyRepository.save(party);
        return "redirect:/addParty";
    }


    @PostMapping("/addGuest")
    public String addGuest(@RequestParam("email") String email,
                           @RequestParam("id") int id,
                           RedirectAttributes redirectAttributes) {
        if (userService.findUserByEmail(email) != null) {
            Party party = partyRepository.findById(id);
            party.addGuest(userService.findUserByEmail(email));
            partyRepository.save(party);
            redirectAttributes.addFlashAttribute("message", "Guest added.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Guest NOT added.");
        }
        return "redirect:/addParty";
    }

    @GetMapping("/gifts")
    public String gifts(@RequestParam ("id") int id,
                        ModelMap modelMap){
        modelMap.addAttribute("partyId",id);
        return "party/giftadd";
    }

    @PostMapping("/addGift")
    public String addGift(@RequestParam("name") String name,
                          @RequestParam("price") Double price,
                          @RequestParam("id") int id){
        Gift gift = new Gift(name,price,partyRepository.findById(id));
        giftRepository.save(gift);
        return "redirect:/addParty";
    }


    @PostMapping("/giftList")
    public String giftList(@RequestParam("partyId") int id,
                           ModelMap modelMap){
        modelMap.addAttribute("gifts",giftRepository.findAllByParty(partyRepository.findById(id)));
        return "party/giftslist";
    }

    @PostMapping("/chooseGift")
    public String chooseGift(@RequestParam ("id") int id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Gift gift = giftRepository.findById(id);
        gift.setUser(userService.findUserByEmail(authentication.getName()));
        giftRepository.save(gift);
        return "redirect:/addParty";
    }


}
