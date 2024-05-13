package project.boardservice.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.boardservice.domain.Member;
import project.boardservice.dto.MemberSaveDto;
import project.boardservice.dto.MemberUpdateDto;
import project.boardservice.exception.MemberNameDuplicateException;
import project.boardservice.exception.MemberNicknameDuplicateException;
import project.boardservice.service.MemberService;

@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 정보 조회
    @GetMapping("{memberId}")
    public String member(@PathVariable long memberId, Model model) {
        Member member = memberService.findById(memberId).get();
//        model.addAttribute("member", member);
        return "ok";
//        return "members/member";
    }

    // 회원 가입 페이지
    @GetMapping("/add")
    public String addMemberForm(){
        return "ok";
//        return "members/addMemberForm";
    }

    // 회원 가입
    @PostMapping("/add")
    public String addMember(@Validated @ModelAttribute MemberSaveDto memberSaveDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 검증 실패
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "fail";
        }

        // 성공 로직
        Member member = memberService.save(memberSaveDto);

        return "ok";
//        return "redirect:/"; // 로그인 페이지로 이동하기
    }

    // 회원 정보 수정 페이지
    @GetMapping("/{memberId}/edit")
    public String updateMemberForm(@PathVariable Long memberId, Model model) {
        Member member = memberService.findById(memberId).get();
        return "ok";
//        return "members/updateMemberForm";
    }

    // 회원 정보 수정
    @PutMapping("/{memberId}")
    public String updateMember(@PathVariable Long memberId, @Validated @ModelAttribute MemberUpdateDto memberUpdateDto, BindingResult bindingResult) {
        // 검증 실패
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "fail";
        }

        // 성공 로직
        memberService.update(memberId, memberUpdateDto);

        return "ok";
//        return "redirect:/members/{memberId}";
    }

}
