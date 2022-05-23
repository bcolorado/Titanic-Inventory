package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.repositories.LogRepository;
import com.titanicos.TitanicInventory.repositories.UserRepository;
import com.titanicos.TitanicInventory.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

@Controller
@SessionAttributes("logged_user")
public class InicioController {

    @Autowired
    UserRepository userRepo;
    @Autowired
    LogRepository logRepo;

    @GetMapping("")
    public String Inicio(){
        System.out.println("INICIO!");
        return "login";
    }

    @RequestMapping("")
    public String Inicio(final Model model,@ModelAttribute User userAcc, @RequestParam("user") String user, @RequestParam("password") String password, HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("here:"+user);
        String ip = request.getRemoteAddr();
        System.out.println(userRepo.findUserByName(user).toString());
        if (LogIn(user, password, ip)) {
            System.out.println("logged in:"+user);
            model.addAttribute("logged_user",userRepo.findUserByName(user));
            return "redirect:"+"home";
        }else {
            System.out.println("wrong password:"+user);
            return "";
        }

    }


    // CREATES an USER in the database
    boolean LogIn(String user, String password, String ip) throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("Getting "+user+" info...");
        User loginUser = userRepo.findUserByName(user);
        if (loginUser == null) {
            System.out.println("User doesn't exists...");
            return false;
        }else {
            System.out.println("Verifying user...");
            System.out.println(user.toString());
            return verifyPassword(loginUser,password);
            //logRepo.save(new LogEvent("Create account","test",ip));
        }
    }

    @ModelAttribute("logged_user")
    public User logged_user(){
        return new User();
    }

    // READ and SHOW ALL USERS IN CONSOLE
    public void showAllUsers() {
        System.out.println("-----------------------------------------------");
        System.out.println("SHOWING ALL USERS");
        userRepo.findAll().forEach(user -> System.out.println(user.toString()));
        System.out.println("-----------------------------------------------");
    }

    // READ and SHOW ALL USERS IN CONSOLE
    public void showLog() {
        System.out.println("-----------------------------------------------");
        System.out.println("SHOWING ACTION LOG");
        logRepo.findAll().forEach(event -> System.out.println(event.toString()));
        System.out.println("-----------------------------------------------");
    }
    public byte[] generateSalt() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
    public byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return hash;
    }
    public boolean verifyPassword(User user, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = user.getSalt();
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = factory.generateSecret(spec).getEncoded();
        return (Arrays.equals(user.getPassword(),testHash));
    }

}
