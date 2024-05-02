package project.boardservice.service;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.boardservice.domain.Member;
import project.boardservice.exception.MemberNameDuplicateException;
import project.boardservice.exception.MemberNicknameDuplicateException;
import project.boardservice.repository.MemberRepository;
import project.boardservice.repository.MemberUpdateDto;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

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

        //when
        Member savedMember = memberService.save(member);

        //then
        assertThat(member).isEqualTo(savedMember);
    }

    //https://kth990303.tistory.com/451
    @Test
    void 중복_회원_예외(){
        //given
        Member member1 = createMember();
        Member member2 = createMember();

        // when
        memberService.save(member1);

        //then
        assertThatThrownBy(() -> memberService.save(member2))
                .isInstanceOf(MemberNameDuplicateException.class);
    }

    @Test
    void update() {
        //given
        Member member = createMember();
        Member savedMember = memberService.save(member);
        Long id = savedMember.getId();

        //when
        MemberUpdateDto updateParam = new MemberUpdateDto("kkk", "bcd", "임");
        memberService.update(id, updateParam);

        //then
        Member findMember = memberService.findById(id).orElseThrow();
        assertThat(findMember.getName()).isEqualTo(updateParam.getName());
        assertThat(findMember.getPassword()).isEqualTo(updateParam.getPassword());
        assertThat(findMember.getNickname()).isEqualTo(updateParam.getNickname());
    }

    @Test
    void 회원정보수정_아이디중복() {
        //given
        Member member1 = createMember();
        Member member2 = new Member();
        member2.setName("kimi");
        member2.setPassword("qwe");
        member2.setNickname("김김");

        //when
        memberService.save(member1);
        Member updateMember = memberService.save(member2);
        MemberUpdateDto updateParam = new MemberUpdateDto("kim", "bcd", "임");

        //then
        assertThatThrownBy(() -> memberService.update(updateMember.getId(), updateParam))
                .isInstanceOf(MemberNameDuplicateException.class);
    }

    @Test
    void 회원정보수정_닉네임중복() {
        //given
        Member member1 = createMember();
        Member member2 = new Member();
        member2.setName("kimi");
        member2.setPassword("qwe");
        member2.setNickname("김김");

        //when
        memberService.save(member1);
        Member updateMember = memberService.save(member2);
        MemberUpdateDto updateParam = new MemberUpdateDto("qwe", "bcd", "김");

        //then
        assertThatThrownBy(() -> memberService.update(updateMember.getId(), updateParam))
                .isInstanceOf(MemberNicknameDuplicateException.class);
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("kim");
        member.setPassword("abc");
        member.setNickname("김");
        return member;
    }
}