package com.example.titanicinventory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InicioController {

    @RequestMapping("/inicio-sesion")
    public String Inicio(@RequestParam(name="name", required=false, defaultValue="World")
        String name, Model model){model.addAttribute("name", name);
        return "inicio-sesion";
    }
}
