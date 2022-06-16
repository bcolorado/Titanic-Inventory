package com.titanicos.TitanicInventory.controller;

import com.titanicos.TitanicInventory.model.LogEvent;
import com.titanicos.TitanicInventory.model.User;
import com.titanicos.TitanicInventory.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Controller
public class LogController {

    @Autowired
    LogRepository logRepo;
    @GetMapping("/admin_log")
    public String Admin_users(@RequestParam(name="from", required = false) String from,@RequestParam(name="to", required = false) String to,@SessionAttribute(required=false,name="logged_user") User userAcc, final Model model) throws ParseException {

        if (userAcc == null || userAcc.getRol() == null){
            System.out.println("Not logged in, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("vendedor")) {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }else if (userAcc.getRol().equals("administrador")) {
            if (from == null && to == null) {
                LogEvent[] listaLog = logRepo.findAll(Sort.by(Sort.Direction.DESC, "timestamp")).toArray(new LogEvent[0]);
                model.addAttribute("log",listaLog);
                return "admin_log";
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            Date from_date = formatter.parse(from);
            c.setTime(formatter.parse(to));
            c.add(Calendar.DATE, 1);
            Date to_date = c.getTime();
            LogEvent[] listaLog = logRepo.inRange(from_date, c.getTime()).toArray(new LogEvent[0]);
            model.addAttribute("log",listaLog);
            return "admin_log";
        }else {
            System.out.println("Wrong role, redirecting...");
            return "redirect:" + "";
        }
    }
}
