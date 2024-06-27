package project.boardservice.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.boardservice.domain.Member;
import project.boardservice.web.argumentresolver.Login;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@Login Member loginMember, Model model) {

        if(loginMember == null) {
            return "redirect:/posts";
        }

        model.addAttribute("member", loginMember);
        return "posts/loginPosts";
    }
}
