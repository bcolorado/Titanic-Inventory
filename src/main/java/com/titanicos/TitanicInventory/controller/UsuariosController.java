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
import java.util.ArrayList;
import java.util.List;

@Controller
public class UsuariosController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    LogRepository logRepo;


    @RequestMapping("/admin_users")
    public String Admin_users(@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model){
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {
            List userList = userRepo.findUsersByActive(true); // Get list of all active users in database.
            userList.remove(0); // removes developer, we don't want to see or be able to edit developer account
            User[] users = new User[userList.size()]; // creates empty array
            userList.toArray(users); // pass the list of users (minus developer) to the array
            model.addAttribute("users", users); // pass the array to the model, so front end can use that info to list users.
            return "admin_users";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }

    @RequestMapping("/admin_new_user")
    public String New_user(@SessionAttribute(required=false,name="logged_user") User userAcc,
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
            CreateUser(user,password,name,rol,userAcc.getId(),ip);return "redirect:"+"admin_users";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }

    @GetMapping("/admin_new_user")
    public String Inicio(@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model){
        System.out.println("NEW USER FORM!");
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:";
        }else if (userAcc.getRol().equals("administrador")) {
            //System.out.println("en userAcc queda el objeto usuario que inicio sesion" + userAcc.toString());
            model.addAttribute("logged_user", userAcc);
            return "admin_new_user";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:";
        }
    }

    @GetMapping("/edit_user")
    public String Edit_User(@RequestParam("id") String edit_id,
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
            List<String> roles = new ArrayList<String>();
            roles.add("vendedor");
            roles.add("administrador");
            User userToEdit = userRepo.findUserByID(edit_id);
            model.addAttribute("userToEdit", userToEdit);
            model.addAttribute("roles",roles);
            return "admin_edit_user";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }

    }
    @RequestMapping("/edit_user")
    public String Edit_User(@RequestParam("id") String edit_id,
                            @SessionAttribute(required=false,name="logged_user") User userAcc,
                            final Model model,
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
            UpdateUser(edit_id,password,name,rol,userAcc.getId(),ip);
            return "redirect:"+"admin_users";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }

    @RequestMapping("/delete_user")
    public String Delete_User(@RequestParam("id") String edit_id,
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
            DeleteUser(edit_id,userAcc.getId(),ip);
            return "redirect:"+"admin_users";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
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
            User test = userRepo.findUserByID(user);
            if ((user.equals(""))||(password.equals(""))||(name.equals(""))||(rol.equals(""))) {
                System.out.println("One or more input values empty.");
                return false;
            }else {
                if (test == null || !test.getActive()) {
                    byte[] salt = new byte[16];
                    salt = generateSalt();
                    byte[] hash = hashPassword(password, salt);
                    User newAcc = new User(user, hash, salt, name, rol);
                    userRepo.save(newAcc);
                    logRepo.save(new LogEvent("ACCOUNT "+user+" CREATED", author, ip));
                    System.out.println("Created account:");
                    System.out.println(newAcc.toString());
                    return true;
                } else {
                    System.out.println("Account already exists.");
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to create account.");
            return false;
        }
    }

    public boolean UpdateUser(String user, String password, String name, String rol, String author, String ip) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            User acc = userRepo.findUserByID(user);
            if(user.equals("developer")) {
                System.out.println("Can't edit developer account.");
                return false;
            }else if ((user.equals(""))||(name.equals(""))||(rol.equals(""))) {
                System.out.println("One or more input values empty.");
                return false;
            }else if (password.equals("")){
                String changes = new String();
                if (!acc.getName().equals(name)){
                    String oldName = acc.getName();
                    acc.setName(name);
                    changes += "[NAME: " + oldName + " > " + name + "]";
                }else {
                    changes += "";
                }
                if (!acc.getRol().equals(rol)){
                    String oldRol = acc.getRol();
                    acc.setRol(rol);
                    changes += "[ROL: " + oldRol + " > " + rol + "]";
                }else {
                    changes = "";
                }
                if (changes.equals("")) {
                    changes = "NO CHANGES";
                }
                userRepo.save(acc);
                logRepo.save(new LogEvent("ACCOUNT "+user+" UPDATED > " + changes, author, ip));
                System.out.println("Account updated:");
                System.out.println(acc.toString());
                return true;
            }else {
                String changes = new String();
                if (!acc.getName().equals(name)){
                    String oldName = acc.getName();
                    acc.setName(name);
                    changes += "[NAME: " + oldName + " > " + name + "]";
                }else {
                    changes += "";
                }
                if (!acc.getRol().equals(rol)){
                    String oldRol = acc.getRol();
                    acc.setRol(rol);
                    changes += "[ROL: " + oldRol + " > " + rol + "]";
                }else {
                    changes += "";
                }
                byte[] salt = new byte[16];
                salt = generateSalt();
                byte[] hash = hashPassword(password, salt);

                acc.setSalt(salt);
                acc.setPassword(hash);
                changes += "[PASSWORD AND SALT UPDATED]";
                userRepo.save(acc);
                logRepo.save(new LogEvent("ACCOUNT "+user+" UPDATED: " + changes, author, ip));
                System.out.println("Account updated:");
                System.out.println(acc.toString());
                return true;
            }
        } catch (Exception e) {
            System.out.println("Failed to update account.");
            return false;
        }
    }

    public boolean DeleteUser(String user, String author, String ip) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            User test = userRepo.findUserByID(user);
            if (user.equals(author)) {
                System.out.println("Can't delete yourself.");
                return false;
            }else if(user.equals("developer")) {
                System.out.println("Can't delete developer account.");
                return false;
            }else {
                if (test == null) {
                    System.out.println("User doesn't exist.");
                    return false;
                }else {
                    test.setActive(false);
                    userRepo.save(test);
                    //userRepo.deleteById(user);
                    logRepo.save(new LogEvent("ACCOUNT "+user+" DELETED", author, ip));
                    System.out.println("ACCOUNT " + user + " DELETED");
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to delete account.");
            return false;
        }
    }

}
