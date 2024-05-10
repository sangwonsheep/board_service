package project.boardservice.web.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm(){
        return "login/loginForm";
    }

    // 로그인
    @PostMapping("/login")
    public String login() {
        return "/";
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout() {
        return "/";
    }

}
