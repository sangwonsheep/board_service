package project.boardservice.service;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.boardservice.domain.Member;
import project.boardservice.dto.MemberDto;
import project.boardservice.exception.MemberNameDuplicateException;
import project.boardservice.exception.MemberNicknameDuplicateException;
import project.boardservice.repository.MemberRepository;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired EntityManager em;
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void save() {
        //given
        Member member = createMember();
        MemberDto memberDto = new MemberDto(member);

        //when
        Member savedMember = memberService.save(memberDto);

        // member != savedMember
        log.info("id = {}", member.getId());
        log.info("id = {}", savedMember.getId());

        //then
        assertThat(member.getName()).isEqualTo(savedMember.getName());
        assertThat(member.getPassword()).isEqualTo(savedMember.getPassword());
        assertThat(member.getNickname()).isEqualTo(savedMember.getNickname());
    }

    //https://kth990303.tistory.com/451
    @Test
    void 중복_회원_예외(){
        //given
        Member member1 = createMember();
        Member member2 = createMember();

        MemberDto memberDto1 = new MemberDto(member1);
        MemberDto memberDto2 = new MemberDto(member2);

        // when
        memberService.save(memberDto1);

        //then
        assertThatThrownBy(() -> memberService.save(memberDto2))
                .isInstanceOf(MemberNameDuplicateException.class);
    }

    @Test
    void update() {
        //given
        Member member = createMember();
        MemberDto memberDto = new MemberDto(member);
        Member savedMember = memberService.save(memberDto);
        Long id = savedMember.getId();

        //when
        MemberDto updateParam = new MemberDto("kkk", "bcdqqwed", "임꺽정");
        memberService.update(id, updateParam);

        //then
        Member findMember = memberService.findById(id).orElseThrow();
        assertThat(findMember.getName()).isNotEqualTo(updateParam.getName()); // 아이디(name)는 변경되면 안됨.
        assertThat(findMember.getPassword()).isEqualTo(updateParam.getPassword());
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

        MemberDto memberDto1 = new MemberDto(member1);
        MemberDto memberDto2 = new MemberDto(member2);

        //when
        memberService.save(memberDto1);
        Member updateMember = memberService.save(memberDto2);
        MemberDto updateParam = new MemberDto("qwe", "bcddsadse", "김기자");

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