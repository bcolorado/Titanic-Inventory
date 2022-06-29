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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            int respuesta = CreateUser(user,password,name,rol,userAcc.getId(),ip);
            if(respuesta == 0){
                redirAttrs.addFlashAttribute("error", "Falta llenar información");
                return "redirect:"+"admin_new_user";
            } else if(respuesta == -1){
                redirAttrs.addFlashAttribute("error", "El usuario ya existe");
            } else if (respuesta == -2){
                redirAttrs.addFlashAttribute("error", "No fue posible crear el usuario");
            }
            return "redirect:"+"admin_users";
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
            int respuesta = UpdateUser(edit_id,password,name,rol,userAcc.getId(),ip);
            if(respuesta == 0){
                redirAttrs.addFlashAttribute("error", "No se puede editar la cuenta del desarrollador");
                return "redirect:"+"admin_edit_user";
            } else if(respuesta == -1){
                redirAttrs.addFlashAttribute("error", "Faltan datos");
            } else if (respuesta == -2){
                redirAttrs.addFlashAttribute("error", "No fue posible actualizar el usuario");
            }
            return "redirect:"+"admin_users";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }

    @RequestMapping("/delete_user")
    public String Delete_User(@RequestParam("id") String edit_id,
                            @SessionAttribute(required=false,name="logged_user") User userAcc,
                            final Model model, RedirectAttributes redirAttrs,
                            HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {
            String ip = request.getRemoteAddr();
            int respuesta = DeleteUser(edit_id,userAcc.getId(),ip);
            if(respuesta == 0){
                redirAttrs.addFlashAttribute("error", "No se puede borrar su cuenta");
            } else if(respuesta == -1){
                redirAttrs.addFlashAttribute("error", "No puede borrar la cuenta de desarrollador");
            } else if (respuesta == -2){
                redirAttrs.addFlashAttribute("error", "Usuario no existe");
            } else if (respuesta == -3){
                redirAttrs.addFlashAttribute("error", "No fue posible borrar la cuenta");
            }
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

    public int CreateUser(String user, String password, String name, String rol, String author, String ip) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            User test = userRepo.findUserByID(user);
            if ((user.equals(""))||(password.equals(""))||(name.equals(""))||(rol.equals(""))) {
                System.out.println("One or more input values empty.");
                return 0;
            }else if (!(user.matches("[a-zñ_0-9]{3,}"))) {
                System.out.println("wrong user ID pattern.");
                return 0;
            }else{
                if (test == null || !test.getActive()) {
                    byte[] salt = new byte[16];
                    salt = generateSalt();
                    byte[] hash = hashPassword(password, salt);
                    User newAcc = new User(user, hash, salt, name, rol);
                    userRepo.save(newAcc);
                    logRepo.save(new LogEvent("ACCOUNT "+user+" CREATED", author, ip));
                    System.out.println("Created account:");
                    System.out.println(newAcc.toString());
                    return 1;
                } else {
                    System.out.println("Account already exists.");
                    return -1;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to create account.");
            return -2;
        }
    }

    public int UpdateUser(String user, String password, String name, String rol, String author, String ip) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            User acc = userRepo.findUserByID(user);
            if(user.equals("developer")) {
                System.out.println("Can't edit developer account.");
                return 0;
            }else if ((user.equals(""))||(name.equals(""))||(rol.equals(""))) {
                System.out.println("One or more input values empty.");
                return -1;
            }else if (password.equals("")){
                String changes = "";
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
                return 1;
            }else {
                String changes = "";
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
                return 1;
            }
        } catch (Exception e) {
            System.out.println("Failed to update account.");
            return -2;
        }
    }

    public int DeleteUser(String user, String author, String ip) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            User test = userRepo.findUserByID(user);
            if (user.equals(author)) {
                System.out.println("Can't delete yourself.");
                return 0;
            }else if(user.equals("developer")) {
                System.out.println("Can't delete developer account.");
                return -1;
            }else {
                if (test == null) {
                    System.out.println("User doesn't exist.");
                    return -2;
                }else {
                    test.setActive(false);
                    userRepo.save(test);
                    //userRepo.deleteById(user);
                    logRepo.save(new LogEvent("ACCOUNT "+user+" DELETED", author, ip));
                    System.out.println("ACCOUNT " + user + " DELETED");
                    return 1;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to delete account.");
            return -3;
        }
    }

}
