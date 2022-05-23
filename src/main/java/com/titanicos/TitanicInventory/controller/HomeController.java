package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.model.User;
import com.titanicos.TitanicInventory.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @RequestMapping("/home_admin")
    public String Home(@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model){
        if (userAcc == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else {
            //System.out.println("en userAcc queda el objeto usuario que inicio sesion" + userAcc.toString());
            return "home_admin";
        }
    }
}
