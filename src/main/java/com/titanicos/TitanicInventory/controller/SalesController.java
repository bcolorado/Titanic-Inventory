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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
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
                              @RequestParam("new_quantity") List<Integer> quantity,
                              RedirectAttributes redirAttrs,
                              HttpServletRequest request) {
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {
            System.out.println(quantity);
            //String [] ProductString = products.split(",");
            List<Products> products = ProductRepo.findProductsByActive(true);
            String ip = request.getRemoteAddr();
            int respuesta = CreateSale(products,quantity,userAcc.getId(),ip);
            if(respuesta == 0){
                redirAttrs.addFlashAttribute("error", "Debe elegir al menos un producto");
                return "redirect:"+"admin_new_sales";
            } else if (respuesta == -1) {
                redirAttrs.addFlashAttribute("error", "No se pudo crear la venta");
                return "redirect:"+"admin_new_sales";
            }
            return "redirect:"+"admin_sales";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }

    public int CreateSale(List<Products> products,List<Integer> quantitys, String author, String ip)  {
        try {
            int validation = 0;
            int total = 0;
            Sales sale = new Sales(author,total);
            for (int i=0;i<products.size();i++){
                if(quantitys.get(i)!=0) {
                    Products p1 = products.get(i);
                    p1.setCantidad(p1.getCantidad() - quantitys.get(i));
                    ProductRepo.save(p1);
                    p1.setCantidad(quantitys.get(i));
                    p1.setSubtotal(quantitys.get(i) * p1.getPrecio());
                    sale.addProduct(p1);
                    total += p1.getCantidad() * p1.getPrecio();
                    validation+=1;
                }
            }
            sale.setQuantity(total);
            saleRepo.save(sale);
            logRepo.save(new LogEvent("SALE "+sale+" CREATED", author, ip));
            //System.out.println("Created product:");
            //System.out.println(newAcc.toString());
            return validation;

        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        }
    }

    @GetMapping("/seller")
    public String seller_sale(@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model){
        System.out.println("NEW SALE FORM 2!");
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:";
        }else if (userAcc.getRol().equals("vendedor")) {
            //System.out.println("en userAcc queda el objeto usuario que inicio sesion" + userAcc.toString());
            model.addAttribute("logged_user", userAcc);
            Products[] listaProductos = ProductRepo.findProductsByActive(true).toArray(new Products[0]);
            model.addAttribute("productos",listaProductos);
            return "seller";
        }else if (userAcc.getRol().equals("administrador")) {
            return "redirect:";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:";
        }
    }

    @RequestMapping("/seller")
    public String seller_new_sale(@SessionAttribute(required=false,name="logged_user") User userAcc,
                           final Model model,
                           @RequestParam("new_quantity") List<Integer> quantity,
                           RedirectAttributes redirAttrs,
                           HttpServletRequest request) {
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println(quantity);
            //System.out.println(products);
            //String [] ProductString = products.split(",");
            //while(quantity.remove(null));
            String ip = request.getRemoteAddr();
            List<Products> products = ProductRepo.findProductsByActive(true);
            int respuesta = CreateSale(products,quantity,userAcc.getId(),ip);
            if(respuesta == 0){
                redirAttrs.addFlashAttribute("error", "Debe elegir al menos un producto");
                return "redirect:"+"seller";
            } else if (respuesta == -1) {
                redirAttrs.addFlashAttribute("error", "No se pudo crear la venta");
                return "redirect:"+"seller";
            }
            return "redirect:" + "seller";
        }else if (userAcc.getRol().equals("administrador")) {
            return "redirect:"+"";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
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
                                RedirectAttributes redirAttrs,
                                HttpServletRequest request)  {
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {
            String ip = request.getRemoteAddr();
            int respuesta = UpdateSale(edit_id,timestamp,quantity,userAcc.getId(),ip);
            if(respuesta == 0){
                redirAttrs.addFlashAttribute("error", "Es necesario un ID");
                return "redirect:"+"admin_new_sales";
            } else if(respuesta == -1){
                redirAttrs.addFlashAttribute("error", "No fue posible editar la venta");
            }
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
                                 RedirectAttributes redirAttrs,
                                 HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {
            String ip = request.getRemoteAddr();
            int respuesta = DeleteSale(edit_id,userAcc.getId(),ip);
            if(respuesta == 0){
                redirAttrs.addFlashAttribute("error", "La venta no existe");
            } else if(respuesta == -1){
                redirAttrs.addFlashAttribute("error", "No fue posible borrar la venta");
            }
            return "redirect:"+"admin_products";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }

    public int DeleteSale(String id_sale, String author, String ip) {
        try {
            Sales test = saleRepo.findProductByID(id_sale);
            if (test == null || !test.isActive()) {
                System.out.println("Sale doesn't exist.");
                return 0;
            }else {
                test.setActive(false);
                saleRepo.save(test);
                logRepo.save(new LogEvent("PRODUCT "+id_sale+" DELETED", author, ip));
                return 1;
            }
        } catch (Exception e) {
            System.out.println("Failed to delete sale.");
            return -1;
        }
    }

    public int UpdateSale(String id_sale, Date timestamp, int quantity, String author, String ip) {
        try {
            Sales sale = saleRepo.findProductByID(id_sale);
            if ((id_sale.equals(""))) {
                System.out.println("id input is empty");
                return 0;
            }else {
                String changes = new String();
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
                return 1;
            }
        } catch (Exception e) {
            System.out.println("Failed to update sale.");
            return -1;
        }
    }

}
