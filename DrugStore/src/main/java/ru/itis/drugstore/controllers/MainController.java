package ru.itis.drugstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.itis.drugstore.models.Item;
import ru.itis.drugstore.parsers.MainParser;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
public class MainController {


    @Autowired
    private MainParser mainParser;

    @GetMapping("/home")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/search")
    public String getItems(ModelMap model, @RequestParam String name, HttpServletRequest request) {
        if (name != null && !name.isEmpty()) {
            List<Item> items = mainParser.getItemsListByRequest(name);
            String pattern = "(.*),(.*)";
            for (Item item : items) {
                if (item.getPrice().matches(pattern)) {
                    char[] ch = item.getPrice().toCharArray();
                    for (int i = 0; i < ch.length; i++)
                        if (ch[i] == ',') ch[i] = '.';
                    item.setPrice(String.valueOf(ch));
                }
            }
            request.getSession().setAttribute("items", items);
            model.addAttribute("items", items);
        } else {
            model.addAttribute("error", "error");
        }
        return "home";
    }

    @GetMapping("/sort/{sort}")
    public String sort(ModelMap model, @PathVariable String sort, HttpServletRequest request) {
        List<Item> items = (List<Item>) request.getSession().getAttribute("items");
        if (sort.equals("sort")) Collections.sort(items);
        if (sort.equals("reverse")) Collections.reverse(items);
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }

        return "home";
    }
}
