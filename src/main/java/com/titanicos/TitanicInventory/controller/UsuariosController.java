package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.model.User;
import com.titanicos.TitanicInventory.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class UsuariosController {

    @Autowired
    UserRepository userRepo;

    @RequestMapping("/home_admin_users")
    public String admin_users(@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model){
        if (userAcc == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else {
            System.out.println("en userAcc queda el objeto usuario que inicio sesion" + userAcc.toString());
            return "home_admin_users";
        }
    }

    // READ and SHOW ALL USERS IN CONSOLE
    public void showAllUsers() {
        System.out.println("-----------------------------------------------");
        System.out.println("SHOWING ALL USERS");
        userRepo.findAll().forEach(user -> System.out.println(user.toString()));
        System.out.println("-----------------------------------------------");
    }
}
