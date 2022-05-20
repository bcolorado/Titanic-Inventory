package com.example.titanicinventory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class InicioController {
    @GetMapping("")
    public String Inicio(){
        System.out.println("INICIO!");
        return "inicio-sesion";
    }

    @RequestMapping("")
    public String Inicio(@RequestParam("user") String user, @RequestParam("password") String password){
        System.out.println("User: " + user);
        System.out.println("Password: " + password);
        return "redirect:home";
    }

    @RequestMapping("/home")
    public String Home(){
        return "home";
    }
}
