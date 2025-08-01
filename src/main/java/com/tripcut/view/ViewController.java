package com.tripcut.view;

import com.tripcut.domain.user.entity.User;
import com.tripcut.domain.user.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {
    @GetMapping("/view")
    public String index() {
        return "index"; // templates/index.html
    }

    @GetMapping("/login/kakao")
    public String redirectToKakao() {

        return "redirect:" + "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=baf916b3a07072b8333ad4dd2c9481cc"
                + "&redirect_uri=http://localhost:8080/auth/login/kakao"
                + "&response_type=code";
    }
}