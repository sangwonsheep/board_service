package project.boardservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.boardservice.dto.MemberSaveDto;
import project.boardservice.dto.MemberUpdateDto;
import project.boardservice.service.MemberService;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 정보 조회
    @GetMapping("{memberId}")
    public String member(@PathVariable long memberId, Model model) {
        return "members/member";
    }

    // 회원 가입 페이지
    @GetMapping("/add")
    public String addMemberForm(){
        return "/members/addForm";
    }

    // 회원 가입
    @PostMapping("/add")
    public String addMember(@ModelAttribute MemberSaveDto memberSaveDto, RedirectAttributes redirectAttributes) {
        return "redirect:/"; // 로그인 페이지로 이동하기
    }

    // 회원 정보 수정 페이지
    @GetMapping("{memberId}/edit")
    public String updateMemberForm(@PathVariable long memberId, Model model) {
        return "members/updateMemberForm";
    }

    // 회원 정보 수정
    @PutMapping("{memberId}")
    public String updateMember(@PathVariable long memberId, @ModelAttribute MemberUpdateDto memberUpdateDto) {
        return "members/updateMember";
    }

}
