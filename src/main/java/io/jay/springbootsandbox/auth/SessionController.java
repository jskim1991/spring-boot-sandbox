package io.jay.springbootsandbox.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/session")
public class SessionController {

    @GetMapping("/all")
    public Map<String, Object> getAll(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Enumeration<String> attributeNames = session.getAttributeNames();
        Map<String, Object> map = new HashMap<>();
        while (attributeNames.hasMoreElements()) {
            String next = attributeNames.nextElement();
            Object attribute = session.getAttribute(next);
            map.put(next, attribute);
        }

        return map;
    }

    @GetMapping("/add")
    public String add(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("color", "red");
        return "add done";
    }

    @GetMapping("/update")
    public String update(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("color", "blue");
        return "update done";
    }

    @GetMapping("/remove")
    public String remove(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.removeAttribute("color");
        return "remove done";
    }

    @GetMapping("/invalidate")
    public String invalidate(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "invalidate done";
    }

    @GetMapping("/clear")
    public String clear() {
        SecurityContextHolder.clearContext();
        return "clear done";
    }
}
