package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @RequestMapping("/admin")
    public String Home(@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model){
        if (userAcc == null || userAcc.getRol() == null){
            return "redirect:";
        }else if (userAcc.getRol().equals("vendedor")) {
            return "redirect:";
        }else if (userAcc.getRol().equals("administrador")) {
            model.addAttribute("logged_user", userAcc);
            return "admin";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:";
        }
    }
}
