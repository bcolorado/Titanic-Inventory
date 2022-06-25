package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.model.User;
import com.titanicos.TitanicInventory.repositories.SaleRepository;
import com.titanicos.TitanicInventory.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Random;
import java.util.List;

@Controller
public class HomeController {

    private static final Random RANDOM = new Random(System.currentTimeMillis());
    @Autowired
    UserRepository userRepo;
    @Autowired
    SaleRepository saleRepo;
    @RequestMapping("/admin")
    public String Home(@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model){
        if (userAcc == null || userAcc.getRol() == null){
            return "redirect:";
        }else if (userAcc.getRol().equals("vendedor")) {
            return "redirect:";
        }else if (userAcc.getRol().equals("administrador")) {
            System.out.println(userRepo.countByActiveAndRol(true,"vendedor"));
            System.out.println(saleRepo.countByActive(true));
            System.out.println(saleRepo.sumOfTotals());
            System.out.println(saleRepo.productsSold());
            System.out.println(saleRepo.productsIncome());
            System.out.println(saleRepo.sellerSale());
            System.out.println(saleRepo.sellerIngresos());
            System.out.println(saleRepo.ingresosDia());
            System.out.println(saleRepo.ventasDia());
            model.addAttribute("logged_user", userAcc);
            model.addAttribute("chartData", getChartData());
            return "admin";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:";
        }
    }

    private List<List<Object>> getChartData() {
        return List.of(
                List.of("Mushrooms", RANDOM.nextInt(5)),
                List.of("Onions", RANDOM.nextInt(5)),
                List.of("Olives", RANDOM.nextInt(5)),
                List.of("Zucchini", RANDOM.nextInt(5)),
                List.of("Pepperoni", RANDOM.nextInt(5))
        );
    }
}
