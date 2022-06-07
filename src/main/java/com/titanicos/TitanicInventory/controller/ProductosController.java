package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.model.LogEvent;
import com.titanicos.TitanicInventory.model.Products;
import com.titanicos.TitanicInventory.model.User;
import com.titanicos.TitanicInventory.repositories.LogRepository;
import com.titanicos.TitanicInventory.repositories.ProductRepository;
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


@Controller
public class ProductosController {


    @Autowired
    LogRepository logRepo;

    @Autowired
    ProductRepository ProductRepo;

    @RequestMapping("/admin_products")
    public String Admin_users(@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model){

        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {

            Products[] listaProductos = ProductRepo.findProductsByActive(true).toArray(new Products[0]);
            model.addAttribute("productos",listaProductos);

            return "admin_products";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }


    @GetMapping("/admin_new_products")
    public String new_product(@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model){
        System.out.println("NEW PRODUCT FORM!");
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:";
        }else if (userAcc.getRol().equals("administrador")) {
            model.addAttribute("logged_user", userAcc);
            return "admin_new_products";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:";
        }
    }


    @RequestMapping("/admin_new_products")
    public String New_product(@SessionAttribute(required=false,name="logged_user") User userAcc,
                           final Model model,
                           @RequestParam("new_product") String product,
                           @RequestParam("new_price") int price,
                           @RequestParam("new_quantity") int quantity,
                           HttpServletRequest request) {
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {
            String ip = request.getRemoteAddr();
            CreateProduct(product,price,quantity,userAcc.getId(),ip);
            return "redirect:"+"admin_products";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }


    public boolean CreateProduct(String product, int price, int quantity, String author, String ip)  {
        try {
            Products test = ProductRepo.findProductByID(product);
            if ((product.equals(""))) {
                System.out.println("Name input is empty");
                return false;
            }else {
                if (test == null) {
                    Products producto=new Products(product,price,quantity);
                    ProductRepo.save(producto);
                    logRepo.save(new LogEvent("PRODUCT "+product+" CREATED", author, ip));
                    //System.out.println("Created product:");
                    //System.out.println(newAcc.toString());
                    return true;
                } else {
                    System.out.println("Product already exists.");
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to create product.");
            return false;
        }
    }

    @GetMapping("/edit_products")
    public String Edit_products(@RequestParam("id") String edit_id,
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
            Products ProductToEdit = ProductRepo.findProductByID(edit_id);
            model.addAttribute("productToEdit", ProductToEdit);
            return "admin_edit_products";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }

    }
    @RequestMapping("/edit_products")
    public String Edit_Products(@RequestParam("id") String edit_id,
                            @SessionAttribute(required=false,name="logged_user") User userAcc,
                            final Model model,
                            @RequestParam("new_price") int price,
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
            UpdateProduct(edit_id,price,quantity,userAcc.getId(),ip);
            return "redirect:"+"admin_products";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }

    @RequestMapping("/delete_products")
    public String Delete_Product(@RequestParam("id") String edit_id,
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
            DeleteProduct(edit_id,userAcc.getId(),ip);
            return "redirect:"+"admin_products";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }

    public boolean DeleteProduct(String product, String author, String ip) {
        try {
            Products test = ProductRepo.findProductByID(product);
            if (test == null || !test.isActive()) {
                System.out.println("Product doesn't exist.");
                return false;
            }else {
                test.setActive(false);
                ProductRepo.save(test);
                logRepo.save(new LogEvent("PRODUCT "+product+" DELETED", author, ip));
                return true;
            }
        } catch (Exception e) {
            System.out.println("Failed to delete product.");
            return false;
        }
    }


    public boolean UpdateProduct(String product, int price, int quantity, String author, String ip) {
        try {
            Products producto = ProductRepo.findProductByID(product);
            if ((product.equals(""))) {
                System.out.println("Name input is empty");
                return false;
            }else {
                String changes = ""; //replaced new String()
                if (!producto.getId_name().equals(product)){
                    String oldName = producto.getId_name();
                    producto.setId_name(product);
                    changes += "[NAME: " + oldName + " > " + product + "]";
                }else {
                    changes += "";
                }
                if (producto.getPrecio()!=price){
                    int oldPrice = producto.getPrecio();
                    producto.setPrecio(price);
                    changes += "[PRECIO: " + oldPrice + " > " + price + "]";
                }else {
                    changes += "";
                }
                if (producto.getCantidad()!=quantity){
                    int oldQuantity = producto.getCantidad();
                    producto.setCantidad(quantity);
                    changes += "[CANTIDAD: " + oldQuantity + " > " + quantity + "]";
                }else {
                    changes += "";
                }
                if (changes.equals("")) {
                    changes = "NO CHANGES";
                }
                ProductRepo.save(producto);
                logRepo.save(new LogEvent("PRODUCT "+product+" UPDATED: " + changes, author, ip));
                System.out.println("Product updated:");
                System.out.println(producto); //removed .toString()
                return true;
            }
        } catch (Exception e) {
            System.out.println("Failed to update product.");
            return false;
        }
    }

}



