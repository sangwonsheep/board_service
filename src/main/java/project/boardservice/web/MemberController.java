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
import project.boardservice.dto.MemberPasswordUpdateDto;
import project.boardservice.dto.MemberSaveDto;
import project.boardservice.dto.MemberUpdateDto;
import project.boardservice.exception.MemberNameDuplicateException;
import project.boardservice.exception.MemberNicknameDuplicateException;
import project.boardservice.service.MemberService;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 정보 조회
    @GetMapping("{memberId}")
    public String member(@PathVariable long memberId, Model model) {
        Member member = memberService.findById(memberId).get();
        model.addAttribute("member", member);
        return "members/member";
    }

    // 회원 가입 페이지
    @GetMapping("/add")
    public String addMemberForm(Model model){
        model.addAttribute("member", new Member());
        return "members/addMemberForm";
    }

    // 회원 가입
    @PostMapping("/add")
    public String addMember(@Validated @ModelAttribute("member") MemberSaveDto memberSaveDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 검증 실패
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "members/addMemberForm";
        }

        // 성공 로직
        try {
            memberService.save(memberSaveDto);
        } catch (MemberNameDuplicateException | MemberNicknameDuplicateException e) {
            return saveDuplicateException(e, bindingResult);
        }

        return "redirect:/"; // 로그인 페이지로 이동하기, 현재는 홈 화면인 상태
    }

    // 회원 정보 수정 페이지
    @GetMapping("/{memberId}/edit")
    public String updateMemberForm(@PathVariable Long memberId, Model model) {
        Member member = memberService.findById(memberId).get();
        model.addAttribute("member", member);
        return "members/updateMemberForm";
    }

    // 회원 정보 수정
    @PutMapping("/{memberId}")
    public String updateMember(@PathVariable Long memberId, @Validated @ModelAttribute("member") MemberUpdateDto memberUpdateDto, BindingResult bindingResult) {
        // 검증 실패
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "members/updateMemberForm";
        }

        // 성공 로직
        try {
            memberService.update(memberId, memberUpdateDto);
        }
        catch (MemberNicknameDuplicateException e) {
            return updateDuplicateException(e, bindingResult);
        }

        return "redirect:/members/{memberId}";
    }

    // 회원 비밀번호 수정 페이지
    @GetMapping("/{memberId}/password")
    public String updatePasswordForm(@PathVariable Long memberId, Model model) {
        Member member = memberService.findById(memberId).get();
        MemberPasswordUpdateDto memberUpdateDto = new MemberPasswordUpdateDto(member);
        model.addAttribute("member", memberUpdateDto);
        return "members/updatePasswordForm";
    }

    // 회원 비밀번호 수정
    @PutMapping("/{memberId}/password")
    public String updatePassword(@PathVariable Long memberId, @Validated @ModelAttribute("member") MemberPasswordUpdateDto memberUpdateDto, BindingResult bindingResult) {
        Member member = memberService.findById(memberId).get();
        // 현재 비밀번호가 일치하는지 확인
        if(!member.getPassword().equals(memberUpdateDto.getPassword())) {
            bindingResult.rejectValue("password", "current.member.password");
        }

        // 새 비밀번호, 확인이 일치하는지 확인
        if(!memberUpdateDto.getNew_password().equals(memberUpdateDto.getNew_password_check())){
            bindingResult.rejectValue("new_password", "new");
            bindingResult.rejectValue("new_password_check", "new");
        }

        // 검증 실패
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "members/updatePasswordForm";
        }

        // 성공 로직
        memberService.updatePassword(memberId, memberUpdateDto);


        return "redirect:/members/{memberId}";
    }

    // 저장 시 예외 처리
    private String saveDuplicateException(Exception e, BindingResult bindingResult) {
        if (e instanceof MemberNameDuplicateException) {
            bindingResult.rejectValue("name", "duplicate.member.name");
        }
        if (e instanceof MemberNicknameDuplicateException) {
            bindingResult.rejectValue("nickname", "duplicate.member.nickname");
        }

        return "members/addMemberForm";
    }

    // 수정 시 예외 처리
    private String updateDuplicateException(Exception e, BindingResult bindingResult) {
        if (e instanceof MemberNicknameDuplicateException) {
            bindingResult.rejectValue("nickname", "duplicate.member.nickname");
        }

        return "members/updateMemberForm";
    }
}
