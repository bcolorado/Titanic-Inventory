package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.model.LogEvent;
import com.titanicos.TitanicInventory.model.User;
import com.titanicos.TitanicInventory.repositories.LogRepository;
import com.titanicos.TitanicInventory.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
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
        return "login";
    }

    @RequestMapping("/logout")
    public String Logut(Model model, HttpSession session){
        model.asMap().clear();
        session.invalidate();
        return "redirect:";
    }

    @RequestMapping("")
    public String Inicio(final Model model,@ModelAttribute User userAcc,
                         @RequestParam("user") String user,
                         @RequestParam("password") String password,
                         RedirectAttributes redirAttrs,
                         HttpServletRequest request)
                         throws NoSuchAlgorithmException,
                         InvalidKeySpecException {
        String ip = request.getRemoteAddr();
        int login_answer = LogIn(user, password, ip);
        if (login_answer == 1) {
            User loggedAcc = userRepo.findUserByID(user);
            model.addAttribute("logged_user",loggedAcc);
            logRepo.save(new LogEvent("USER LOGIN",user,ip));
            if (loggedAcc.getRol().equals("administrador")) {
                System.out.println("logged in:"+user+" as admin");
                return "redirect:"+"admin";
            }else if (loggedAcc.getRol().equals("vendedor")){
                System.out.println("logged in:"+user+" as seller");
                //return "redirect:"+"home_seller"; // para cuando este home de vendedor
                return "redirect:"+"user";
            }else {
                System.out.println("logged in:"+user+" with wrong role");
                return "login";
            }

        }else if (login_answer == -1) {
            redirAttrs.addFlashAttribute("error", "Contraseña incorrecta");
            return "redirect:";
        } else if (login_answer == 0) {
            redirAttrs.addFlashAttribute("error", "Usuario no existe");
            return "redirect:";
        } else {
            redirAttrs.addFlashAttribute("error", "Ha ocurrido un error");
            return "redirect:";
        }

    }


    // Verifies if user and password match to an active user in the database, returns 0 if user don´t exist, 1 if login was succesful, -1 if wrong password

    int LogIn(String user, String password, String ip) throws NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("Getting " + user + " info...");
        User loginUser = userRepo.findUserByID(user);
        //System.out.println(loginUser.getActive());
        if (loginUser == null) {
            logRepo.save(new LogEvent("LOGIN ATTEMPT - USER DOESN'T EXIST",user,ip));
            return 0;
        } else if (!loginUser.getActive()) {
            //System.out.println("User is inactive...");
            logRepo.save(new LogEvent("LOGIN ATTEMPT - Log-IN with inactive acc",user,ip));
            return 0;
        } else {
            if (verifyPassword(loginUser,password)){
                return 1;
            }else {
                logRepo.save(new LogEvent("FAILED LOGIN (WRONG PASSWORD)",user,ip));
                return -1;
            }
        }
    }

    @ModelAttribute("logged_user")
    public User logged_user(){
        return new User();
    }

    // verify password using user salt
    public boolean verifyPassword(User user, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = user.getSalt();
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = factory.generateSecret(spec).getEncoded();
        return (Arrays.equals(user.getPassword(),testHash));
    }

}
