package io.jay.springbootsandbox.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cookie")
public class CookieController {

    @GetMapping("/add")
    public String add(HttpServletResponse response) {
        Cookie uiColorCookie = new Cookie("color", "red");
        uiColorCookie.setMaxAge(60 * 60);
//        uiColorCookie.setDomain("JJ");
//        uiColorCookie.setPath("/welcomeUser");
        response.addCookie(uiColorCookie);
        return "add done";
    }

    @GetMapping("/remove")
    public String remove(HttpServletResponse response) {
        Cookie uiColorCookie = new Cookie("color", "");
        uiColorCookie.setMaxAge(0);
        response.addCookie(uiColorCookie);
        return "remove done";
    }

    @GetMapping("/all")
    public List<Cookie> readCookie(HttpServletRequest request) {
        List<Cookie> cookies = Arrays.asList(request.getCookies());
        return cookies;
    }
}
