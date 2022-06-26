package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.model.LogEvent;
import com.titanicos.TitanicInventory.model.User;
import com.titanicos.TitanicInventory.repositories.SaleRepository;
import com.titanicos.TitanicInventory.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    public String Home(@RequestParam(name="from", required = false) String from, @RequestParam(name="to", required = false) String to, @SessionAttribute(required=false,name="logged_user") User userAcc, final Model model) throws ParseException {
        if (userAcc == null || userAcc.getRol() == null){
            return "redirect:";
        }else if (userAcc.getRol().equals("vendedor")) {
            return "redirect:";
        }else if (userAcc.getRol().equals("administrador")) {
            if (from == null && to == null) {
                System.out.println("# de vendedores: "+userRepo.countByActiveAndRol(true,"vendedor"));
                System.out.println("# de administradores: "+userRepo.countByActiveAndRol(true,"administrador")); // cuenta a developer
                System.out.println("# de usuarios activos: "+saleRepo.countActiveSellers());
                System.out.println("# de ventas: "+saleRepo.countByActive(true));
                System.out.println("# de ventas anuladas: "+saleRepo.countByActive(false));
                System.out.println("Total ingresos: $"+saleRepo.sumOfTotals());
                // listas de objetos "Queries", cada objeto tiene dos atributos, id y dato.
                // Se imprime id dato
                // id son las categorias para los de pie (productos y vendedores) o las bins para los de barras (dias)
                // dato es el valor numerico
                System.out.println(saleRepo.productsSold());
                System.out.println(saleRepo.productsIncome());
                System.out.println(saleRepo.sellerSale());
                System.out.println(saleRepo.sellerIngresos());
                System.out.println(saleRepo.ingresosDia());
                System.out.println(saleRepo.ventasDia());
                model.addAttribute("logged_user", userAcc);
                model.addAttribute("chartData", getChartData());
                return "admin";
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            Date from_date = formatter.parse(from);
            c.setTime(formatter.parse(to));
            c.add(Calendar.DATE, 1);
            Date to_date = c.getTime();

            System.out.println("# de vendedores: "+userRepo.countByActiveAndRol(true,"vendedor"));
            System.out.println("# de administradores: "+userRepo.countByActiveAndRol(true,"administrador")); // cuenta a developer
            System.out.println("# de usuarios activos: "+saleRepo.countActiveSellersInRange(from_date, c.getTime()));
            System.out.println("# de ventas: "+saleRepo.countByActiveInRange(true, from_date, c.getTime()));
            System.out.println("# de ventas anuladas: "+saleRepo.countByActiveInRange(false, from_date, c.getTime()));
            System.out.println("Total ingresos: $"+saleRepo.sumOfTotalsInRange(from_date, c.getTime()));
            // listas de objetos "Queries", cada objeto tiene dos atributos, id y dato.
            // Se imprime id dato
            // id son las categorias para los de pie (productos y vendedores) o las bins para los de barras (dias)
            // dato es el valor numerico
            System.out.println(saleRepo.productsSoldInRange(from_date, c.getTime()));
            System.out.println(saleRepo.productsIncomeInRange(from_date, c.getTime()));
            System.out.println(saleRepo.sellerSaleInRange(from_date, c.getTime()));
            System.out.println(saleRepo.sellerIngresosInRange(from_date, c.getTime()));
            System.out.println(saleRepo.ingresosDiaInRange(from_date, c.getTime()));
            System.out.println(saleRepo.ventasDiaInRange(from_date, c.getTime()));

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
