package com.tripcut.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/api/view")
    public String index() {
        return "index";
    }

    @GetMapping("/login/kakao")
    public String redirectToKakao() {
        return "redirect:" + "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=baf916b3a07072b8333ad4dd2c9481cc"
                + "&redirect_uri=http://localhost:8080/auth/login/kakao"
                + "&response_type=code";
    }
}
