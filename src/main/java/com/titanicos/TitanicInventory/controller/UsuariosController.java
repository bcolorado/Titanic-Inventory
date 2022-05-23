package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.model.LogEvent;
import com.titanicos.TitanicInventory.model.User;
import com.titanicos.TitanicInventory.repositories.LogRepository;
import com.titanicos.TitanicInventory.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;

@Controller
public class UsuariosController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    LogRepository logRepo;


    @RequestMapping("/home_admin_users")
    public String Admin_users(@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model){
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {
            User[] users = userRepo.findAll().toArray(new User[0]);
            model.addAttribute("users", users);
            return "home_admin_users";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
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
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {
            String ip = request.getRemoteAddr();
            CreateUser(user,password,name,rol,userAcc.getId(),ip);
            return "redirect:"+"home_admin_users";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }

    @GetMapping("/home_admin_new_user")
    public String Inicio(){
        System.out.println("NEW USER FORM!");
        return "home_admin_new_user";
    }

    // generates random 16 byte salt
    public byte[] generateSalt() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    // hashes a password with PBKDF2 with a 16 byte salt
    public byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return hash;
    }

    public boolean CreateUser(String user, String password, String name, String rol, String author, String ip) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            byte[] salt = new byte[16];
            salt = generateSalt();
            byte[] hash = hashPassword(password, salt);
            User newAcc = new User(user, hash, salt, name, rol);
            userRepo.save(newAcc);
            logRepo.save(new LogEvent("ACCOUNT CREATED: "+user,author,ip));
            System.out.println("Created account:");
            System.out.println(newAcc.toString());
            return true;
        } catch (Exception e) {
            System.out.println("Failed to create account.");
            return false;
        }
    }

    // READ and SHOW ALL USERS IN CONSOLE
    public void showAllUsers() {
        System.out.println("-----------------------------------------------");
        System.out.println("SHOWING ALL USERS");
        userRepo.findAll().forEach(user -> System.out.println(user.toString()));
        System.out.println("-----------------------------------------------");
    }
}
