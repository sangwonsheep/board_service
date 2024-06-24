package project.boardservice.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import project.boardservice.domain.Member;
import project.boardservice.web.session.SessionConst;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                       Model model) {

        if(loginMember == null) {
            return "redirect:/posts";
        }

        model.addAttribute("member", loginMember);
        return "posts/loginPosts";
    }
}
