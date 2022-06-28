package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.model.LogEvent;
import com.titanicos.TitanicInventory.model.Queries;
import com.titanicos.TitanicInventory.model.User;
import com.titanicos.TitanicInventory.repositories.SaleRepository;
import com.titanicos.TitanicInventory.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

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
                Long noVendedores= userRepo.countByActiveAndRol(true,"vendedor");
                Long noAdmins= userRepo.countByActiveAndRol(true,"administrador")-1; // cuenta a developer
                Long noUsersActive=saleRepo.countActiveSellers();
                if(noUsersActive==null){
                    noUsersActive=0L;
                }
                Long noVentas=saleRepo.countByActive(true);
                Long noVentasAnuladas=saleRepo.countByActive(false);
                Long Ingresos=saleRepo.sumOfTotals();
                if(Ingresos==null){
                    Ingresos=0L;
                }
                model.addAttribute("vendedores", noVendedores);
                model.addAttribute("administradores", noAdmins);
                model.addAttribute("usuarios_activos", noUsersActive);
                model.addAttribute("ventas", noVentas);
                model.addAttribute("ventas_anuladas", noVentasAnuladas);
                double currencyAmount = Ingresos;
                Locale usa = new Locale("en", "US");
                NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(usa);
                model.addAttribute("ingresos", dollarFormat.format(currencyAmount));

                // listas de objetos "Queries", cada objeto tiene dos atributos, id y dato.
                // Se imprime id dato
                // id son las categorias para los de pie (productos y vendedores) o las bins para los de barras (dias)
                // dato es el valor numerico
                List<List<Object>> productosVendidos=getChartData(saleRepo.productsSold());
                List<List<Object>> productosIngresos=getChartData(saleRepo.productsIncome());
                List<List<Object>> vendedorVentas=getChartData(saleRepo.sellerSale());
                List<List<Object>> vendedorIngresos=getChartData(saleRepo.sellerIngresos());
                Map<String,Integer> ingresosDia=getBarData(rellenar(saleRepo.ingresosDia(),null,null));
                Map<String,Integer> ventasDia=getBarData(rellenar(saleRepo.ventasDia(),null,null));
                model.addAttribute("productsSold", productosVendidos);
                model.addAttribute("productsIncome", productosIngresos);
                model.addAttribute("sellerSale", vendedorVentas);
                model.addAttribute("sellerIngresos", vendedorIngresos);
                model.addAttribute("ingresosDia", ingresosDia);
                model.addAttribute("ventasDia", ventasDia);
                model.addAttribute("logged_user", userAcc);
                return "admin";
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            Date from_date = formatter.parse(from);
            c.setTime(formatter.parse(to));
            c.add(Calendar.DATE, 1);
            Date to_date = c.getTime();

            Long noVendedores= userRepo.countByActiveAndRol(true,"vendedor");
            Long noAdmins= userRepo.countByActiveAndRol(true,"administrador")-1; // cuenta a developer
            Long noUsersActive=saleRepo.countActiveSellersInRange(from_date,c.getTime());
            if(noUsersActive==null){
                noUsersActive=0L;
            }
            Long noVentas=saleRepo.countByActiveInRange(true,from_date,c.getTime());
            Long noVentasAnuladas=saleRepo.countByActiveInRange(false,from_date,c.getTime());
            Long Ingresos=saleRepo.sumOfTotalsInRange(from_date,c.getTime());
            if(Ingresos==null){
                Ingresos=0L;
            }
            model.addAttribute("vendedores", noVendedores);
            model.addAttribute("administradores", noAdmins);
            model.addAttribute("usuarios_activos", noUsersActive);
            model.addAttribute("ventas", noVentas);
            model.addAttribute("ventas_anuladas", noVentasAnuladas);
            double currencyAmount = Ingresos;
            Locale usa = new Locale("en", "US");
            NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(usa);
            model.addAttribute("ingresos", dollarFormat.format(currencyAmount));
            // listas de objetos "Queries", cada objeto tiene dos atributos, id y dato.
            // Se imprime id dato
            // id son las categorias para los de pie (productos y vendedores) o las bins para los de barras (dias)
            // dato es el valor numerico
            List<List<Object>> productosVendidos=getChartData(saleRepo.productsSoldInRange(from_date,c.getTime()));
            List<List<Object>> productosIngresos=getChartData(saleRepo.productsIncomeInRange(from_date,c.getTime()));
            List<List<Object>> vendedorVentas=getChartData(saleRepo.sellerSaleInRange(from_date,c.getTime()));
            List<List<Object>> vendedorIngresos=getChartData(saleRepo.sellerIngresosInRange(from_date,c.getTime()));
            Map<String,Integer> ingresosDia=getBarData(rellenar(saleRepo.ingresosDiaInRange(from_date,c.getTime()),from,to));
            Map<String,Integer> ventasDia=getBarData(rellenar(saleRepo.ventasDiaInRange(from_date,c.getTime()),from,to));
            model.addAttribute("productsSold", productosVendidos);
            model.addAttribute("productsIncome", productosIngresos);
            model.addAttribute("sellerSale", vendedorVentas);
            model.addAttribute("sellerIngresos", vendedorIngresos);
            model.addAttribute("ingresosDia", ingresosDia);
            model.addAttribute("ventasDia", ventasDia);
            model.addAttribute("logged_user", userAcc);
            return "admin";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:";
        }
    }
    public Map<String, Integer> getBarData(List<Queries> datos) {
        Map<String, Integer> Result = new TreeMap<>();
        for (Queries x: datos){
            Result.put(x.getId(),Integer.valueOf(x.getDato()));
        }
        return Result;
    }
    private List<List<Object>> getChartData(List<Queries> datos) {
        List<List<Object>> Result = new ArrayList<>();
        for (Queries x: datos){
            Result.add(List.of(x.getId(),Integer.parseInt(x.getDato())));
        }
        return Result;
    }


    public List<Queries> rellenar(List<Queries> Base, @Nullable String from, @Nullable String to){
        List<Queries> result = new ArrayList<>();
        int aux =0;
        LocalDate inicio;
        LocalDate fin;
        if (from==null || to==null){
            inicio = LocalDate.parse(Base.get(0).getId());
            fin = LocalDate.parse(Base.get(Base.size()-1).getId());
        }else{
            inicio = LocalDate.parse(from);
            fin = LocalDate.parse(to);
        }
        LocalDate currentDate = inicio;
        while (!currentDate.isEqual(fin.plusDays(1))){
            if(!Base.isEmpty()) {
                if (LocalDate.parse(Base.get(0).getId()).isEqual(currentDate)) {
                    result.add(Base.remove(0));
                } else {
                    result.add(new Queries(currentDate.toString(), "0"));
                }
            }else{
                result.add(new Queries(currentDate.toString(), "0"));
            }
            currentDate = currentDate.plusDays(1);
        }
        return result;

    }
}
