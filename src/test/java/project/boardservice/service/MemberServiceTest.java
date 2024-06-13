package project.boardservice.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.boardservice.domain.Member;
import project.boardservice.dto.MemberSaveDto;
import project.boardservice.dto.MemberUpdateDto;
import project.boardservice.exception.MemberNameDuplicateException;
import project.boardservice.exception.MemberNicknameDuplicateException;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;

    @Test
    void save() {
        //given
        Member member = createMember();
        MemberSaveDto memberSaveDto = new MemberSaveDto(member);

        //when
        Member savedMember = memberService.save(memberSaveDto);

        // member != savedMember
        log.info("id = {}", member.getId());
        log.info("id = {}", savedMember.getId());

        //then
        assertThat(member.getName()).isEqualTo(savedMember.getName());
        assertThat(member.getPassword()).isEqualTo(savedMember.getPassword());
        assertThat(member.getNickname()).isEqualTo(savedMember.getNickname());
    }

    @Test
    @DisplayName("아이디가 빈 칸인 경우")
    void save_emptyName() {
        //given
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Member member = new Member("", "qweqweqwe", "안녕하시오");
        MemberSaveDto memberSaveDto = new MemberSaveDto(member);

        //when
        Set<ConstraintViolation<MemberSaveDto>> violations = validator.validate(memberSaveDto);

        //then
        // 방법 더 찾기
        for (ConstraintViolation<MemberSaveDto> violation : violations) {
            assertThat(violation.getMessage()).contains("아이디");
        }
    }

    //https://kth990303.tistory.com/451
    @Test
    void 중복_회원_예외(){
        //given
        Member member1 = createMember();
        Member member2 = createMember();

        MemberSaveDto memberSaveDto1 = new MemberSaveDto(member1);
        MemberSaveDto memberSaveDto2 = new MemberSaveDto(member2);

        // when
        memberService.save(memberSaveDto1);

        //then
        assertThatThrownBy(() -> memberService.save(memberSaveDto2))
                .isInstanceOf(MemberNameDuplicateException.class);
    }

    @Test
    void update() {
        //given
        Member member = createMember();
        MemberSaveDto memberDto = new MemberSaveDto(member);
        Member savedMember = memberService.save(memberDto);
        Long id = savedMember.getId();

        //when
        MemberUpdateDto updateParam = new MemberUpdateDto(member.getName(), "임꺽정");
        memberService.update(id, updateParam);

        //then
        Member findMember = memberService.findById(id).orElseThrow();
        assertThat(findMember.getNickname()).isEqualTo(updateParam.getNickname());
    }

    @Test
    void 회원정보수정_닉네임중복() {
        //given
        Member member1 = createMember();
        Member member2 = new Member();
        member2.setName("kimi");
        member2.setPassword("qweqweqwe");
        member2.setNickname("김김");

        MemberSaveDto memberSaveDto1 = new MemberSaveDto(member1);
        MemberSaveDto memberSaveDto2 = new MemberSaveDto(member2);

        //when
        memberService.save(memberSaveDto1);
        Member updateMember = memberService.save(memberSaveDto2);
        MemberUpdateDto updateParam = new MemberUpdateDto(updateMember.getName(), "김기자");

        //then
        assertThatThrownBy(() -> memberService.update(updateMember.getId(), updateParam))
                .isInstanceOf(MemberNicknameDuplicateException.class);
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("kim");
        member.setPassword("qweqweqwe");
        member.setNickname("김기자");
        return member;
    }

}