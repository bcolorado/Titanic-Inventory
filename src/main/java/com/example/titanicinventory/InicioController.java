package com.example.titanicinventory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class InicioController {
    @GetMapping("")
    public String Inicio(){
        System.out.println("INICIO!");
        return "login";
    }

    @RequestMapping("")
    public String Inicio(@RequestParam("user") String user, @RequestParam("password") String password){
        System.out.println("User: " + user);
        System.out.println("Password: " + password);
        return "redirect:home_admin";
    }

    @RequestMapping("/home_admin")
    public String Home(){
        return "home_admin";
    }

    @RequestMapping("/home_admin_users")
    public String Users(){
        return "home_admin_users";
    }
}
