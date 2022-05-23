package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.model.User;
import com.titanicos.TitanicInventory.repositories.UserRepository;
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
public class UsuariosController {

    @Autowired
    UserRepository userRepo;

    @RequestMapping("/home_admin_users")
    public String Admin_users(@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model){
        if (userAcc == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else {
            System.out.println("en userAcc queda el objeto usuario que inicio sesion" + userAcc.toString());
            User[] users = new User[5];
            byte[] by = new byte[0];
            users[0] = new User("1", by, by, "User 1", "user");
            users[1] = new User("2", by, by, "User 2", "user");
            users[2] = new User("3", by, by, "User 3", "user");
            users[3] = new User("4", by, by, "User 4", "user");
            users[4] = new User("5", by, by, "User 5", "user");
            model.addAttribute("users", users);
            return "home_admin_users";
        }
    }

    @RequestMapping("/home_admin_new_user")
    public String New_student(@SessionAttribute(required=false,name="logged_user") User userAcc,
                              final Model model,
                              @RequestParam("new_user") String user,
                              @RequestParam("new_password") String password,
                              @RequestParam("new_name") String name,
                              @RequestParam("new_rol") String rol,
                              HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (userAcc == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else {
            System.out.println(user + " " + password + " " + name + " " + rol);
            return "home_admin_users";
        }
    }

    @GetMapping("/home_admin_new_user")
    public String Inicio(){
        System.out.println("NEW USER FORM!");
        return "home_admin_new_user";
    }

    // READ and SHOW ALL USERS IN CONSOLE
    public void showAllUsers() {
        System.out.println("-----------------------------------------------");
        System.out.println("SHOWING ALL USERS");
        userRepo.findAll().forEach(user -> System.out.println(user.toString()));
        System.out.println("-----------------------------------------------");
    }
}
