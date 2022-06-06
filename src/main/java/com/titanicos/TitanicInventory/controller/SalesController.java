package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.model.LogEvent;
import com.titanicos.TitanicInventory.model.Products;
import com.titanicos.TitanicInventory.model.Sales;
import com.titanicos.TitanicInventory.model.User;
import com.titanicos.TitanicInventory.repositories.LogRepository;
import com.titanicos.TitanicInventory.repositories.ProductRepository;
import com.titanicos.TitanicInventory.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

@Controller
public class SalesController {

    @Autowired
    LogRepository logRepo;

    @Autowired
    SaleRepository saleRepo;
    @Autowired
    ProductRepository ProductRepo;
    @RequestMapping("/admin_sales")
    public String Admin_users(@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model){

        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {

            Sales[] listaSales = saleRepo.findProductsByActive(true).toArray(new Sales[0]);
            model.addAttribute("sales",listaSales);

            return "admin_sales";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }

    @GetMapping("/admin_new_sales")
    public String new_sale(@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model){
        System.out.println("NEW SALE FORM!");
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:";
        }else if (userAcc.getRol().equals("administrador")) {
            //System.out.println("en userAcc queda el objeto usuario que inicio sesion" + userAcc.toString());
            model.addAttribute("logged_user", userAcc);
            Products[] listaProductos = ProductRepo.findProductsByActive(true).toArray(new Products[0]);
            model.addAttribute("productos",listaProductos);
            return "admin_new_sales";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:";
        }
    }

    @RequestMapping("/admin_new_sales")
    public String New_sale(@SessionAttribute(required=false,name="logged_user") User userAcc,
                              final Model model,
                              @RequestParam("new_id_sale") String id_sale,
                              //@RequestParam("new_quantity") int[] quantity,
                              @RequestParam("new_selected") String products,
                              HttpServletRequest request) {
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {
            //System.out.println(quantity);
            System.out.println(products);
            String [] ProductString = products.split(",");
            String ip = request.getRemoteAddr();
            CreateSale(id_sale,ProductString,userAcc.getId(),ip);
            return "redirect:"+"admin_sales";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }

    public boolean CreateSale(String id_sale,String[] products, String author, String ip)  {
        try {
            Sales test = saleRepo.findProductByID(id_sale);
            if ((id_sale.equals(""))) {
                System.out.println("id input is empty");
                return false;
            }else {
                if (test == null) {
                    int quantify =10;
                    Sales sale = new Sales(id_sale,author,quantify);
                    for (String x : products){
                        Products p1 = ProductRepo.findProductByID(x);
                        sale.addProduct(p1);
                    }
                    saleRepo.save(sale);
                    logRepo.save(new LogEvent("SALE "+sale+" CREATED", author, ip));
                    //System.out.println("Created product:");
                    //System.out.println(newAcc.toString());
                    return true;
                } else {
                    System.out.println("Sale already exists.");
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    @GetMapping("/edit_sales")
    public String Edit_sales(@RequestParam("id") String edit_id,
                                @SessionAttribute(required=false,name="logged_user") User userAcc,
                                final Model model){

        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {
            System.out.println("EDIT USER FORM!");
            Sales saleToEdit = saleRepo.findProductByID(edit_id);
            model.addAttribute("saleToEdit", saleToEdit);
            return "admin_edit_sales";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }

    @RequestMapping("/edit_sales")
    public String Edit_Sales(@RequestParam("id") String edit_id,
                                @SessionAttribute(required=false,name="logged_user") User userAcc,
                                final Model model,
                                @RequestParam("new_date") Date timestamp,
                                @RequestParam("new_quantity") int quantity,
                                HttpServletRequest request)  {
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {
            String ip = request.getRemoteAddr();
            UpdateSale(edit_id,timestamp,quantity,userAcc.getId(),ip);
            return "redirect:"+"admin_sales";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }

    @RequestMapping("/delete_sales")
    public String Delete_Sale(@RequestParam("id") String edit_id,
                                 @SessionAttribute(required=false,name="logged_user") User userAcc,
                                 final Model model,
                                 HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {
            String ip = request.getRemoteAddr();
            DeleteSale(edit_id,userAcc.getId(),ip);
            return "redirect:"+"admin_products";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }

    public boolean DeleteSale(String id_sale, String author, String ip) {
        try {
            Sales test = saleRepo.findProductByID(id_sale);
            if (test == null || !test.isActive()) {
                System.out.println("Sale doesn't exist.");
                return false;
            }else {
                test.setActive(false);
                saleRepo.save(test);
                logRepo.save(new LogEvent("PRODUCT "+id_sale+" DELETED", author, ip));
                return true;
            }
        } catch (Exception e) {
            System.out.println("Failed to delete sale.");
            return false;
        }
    }

    public boolean UpdateSale(String id_sale, Date timestamp, int quantity, String author, String ip) {
        try {
            Sales sale = saleRepo.findProductByID(id_sale);
            if ((id_sale.equals(""))) {
                System.out.println("id input is empty");
                return false;
            }else {
                String changes = new String();
                if (!sale.getId_sale().equals(id_sale)){
                    String oldName = sale.getId_sale();
                    sale.setId_sale(id_sale);
                    changes += "[ID: " + oldName + " > " + id_sale + "]";
                }else {
                    changes += "";
                }
                if (sale.getTimestamp()!=timestamp){
                    Date oldTimesatamp = sale.getTimestamp();
                    sale.setTimestamp(timestamp);
                    changes += "[TIMESTAMP: " + oldTimesatamp + " > " + timestamp + "]";
                }else {
                    changes += "";
                }
                if (sale.getQuantity()!=quantity){
                    int oldQuantity = sale.getQuantity();
                    sale.setQuantity(quantity);
                    changes += "[CANTIDAD: " + oldQuantity + " > " + quantity + "]";
                }else {
                    changes += "";
                }
                if (changes.equals("")) {
                    changes = "NO CHANGES";
                }
                saleRepo.save(sale);
                logRepo.save(new LogEvent("SALE "+sale+" UPDATED: " + changes, author, ip));
                System.out.println("Sale updated:");
                System.out.println(sale.toString());
                return true;
            }
        } catch (Exception e) {
            System.out.println("Failed to update sale.");
            return false;
        }
    }

}
