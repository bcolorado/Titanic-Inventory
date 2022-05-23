package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @RequestMapping("/home_admin")
    public String Home(@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model){
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {
            //System.out.println("en userAcc queda el objeto usuario que inicio sesion" + userAcc.toString());
            model.addAttribute("logged_user", userAcc);
            return "home_admin";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }
}
