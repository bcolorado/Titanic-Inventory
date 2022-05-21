package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.TestLogRepository;
import com.titanicos.TitanicInventory.UserRepository;
import com.titanicos.TitanicInventory.model.TestLogEvent;
import com.titanicos.TitanicInventory.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class InicioController {

    @Autowired
    UserRepository userRepo;
    @Autowired
    TestLogRepository logRepo;

    @GetMapping("")
    public String Inicio(){
        System.out.println("INICIO!");
        return "inicio-sesion";
    }

    @RequestMapping("")
    public String Inicio(@RequestParam("user") String user, @RequestParam("password") String password, HttpServletRequest request){
        System.out.println("User: " + user);
        System.out.println("Password: " + password);
        String ip = request.getRemoteAddr();
        createUser(user, password, ip);
        return "redirect:home";
    }

    @RequestMapping("/home")
    public String Home(){

        showAllUsers();
        showLog();
        return "home";
    }

    // CREATES an USER in the database
    void createUser(String user, String password, String ip) {
        System.out.println("Creating User...");
        User test = userRepo.findUserByName(user);
        if (test != null) {
            System.out.println("Already exists.");
        }else {
            userRepo.save(new User(user, password));
            logRepo.save(new TestLogEvent("INSERT","dev_test",ip));
        }
        System.out.println("Saved User: "+user);
    }

    // READ and SHOW ALL USERS IN CONSOLE
    public void showAllUsers() {
        System.out.println("-----------------------------------------------");
        System.out.println("SHOWING ALL USERS");
        userRepo.findAll().forEach(user -> System.out.println(getUserDetails(user)));
        System.out.println("-----------------------------------------------");
    }
    // Print details in readable form

    public String getUserDetails(User user) {

        System.out.println(
                "Name: " + user.getName() +
                        ", \nPassword: " + user.getPassword()
        );

        return "";
    }
    // READ and SHOW ALL USERS IN CONSOLE
    public void showLog() {
        System.out.println("-----------------------------------------------");
        System.out.println("SHOWING ACTION LOG");
        logRepo.findAll().forEach(event -> System.out.println(getLogDetails(event)));
        System.out.println("-----------------------------------------------");
    }
    // Print details in readable form

    public String getLogDetails(TestLogEvent event) {
        String[] in = event.getIp().split(":");
        //in[in.length - 2] = "***";
        //in[in.length - 1] = "***";
        String ip = String.join(".",in);
        System.out.println(
                "Timestamp: " + event.getTimestamp() +
                        ", \nAction: " + event.getAction() +
                        ", \nUser: " + event.getUser() +
                        ", \nIP: " + ip
        );

        return "";
    }
}
