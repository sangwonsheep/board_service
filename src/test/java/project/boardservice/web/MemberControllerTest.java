package project.boardservice.web;

import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import project.boardservice.domain.Member;
import project.boardservice.dto.MemberSaveDto;
import project.boardservice.exception.MemberNameDuplicateException;
import project.boardservice.repository.MemberRepository;
import project.boardservice.service.MemberService;


import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired MockMvc mvc;
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void 회원가입_조회() throws Exception {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");

        //when
        mvc.perform(post("/members/add")
                        .param("name", memberSaveDto.getName())
                        .param("password", memberSaveDto.getPassword())
                        .param("nickname", memberSaveDto.getNickname()))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));

        List<Member> list = memberRepository.findAll();

        //then
        mvc.perform(get("/members/{memberId}", list.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void 회원가입_중복() throws Exception {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");

        //when
        mvc.perform(post("/members/add")
                        .param("name", memberSaveDto.getName())
                        .param("password", memberSaveDto.getPassword())
                        .param("nickname", memberSaveDto.getNickname()))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));


        //then
        assertThatThrownBy(() -> mvc.perform(post("/members/add")
                        .param("name", memberSaveDto.getName())
                        .param("password", memberSaveDto.getPassword())
                        .param("nickname", memberSaveDto.getNickname()))
                .andExpect(status().isOk())
                .andExpect(content().string("ok")))
                .hasCauseInstanceOf(MemberNameDuplicateException.class);
    }

    @Test
    void 회원정보조회_실패(){
        assertThatThrownBy(() -> mvc.perform(get("/members/{memberId}", 1L)))
                .hasCauseInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 회원정보수정() throws Exception {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");

        mvc.perform(post("/members/add")
                        .param("name", memberSaveDto.getName())
                        .param("password", memberSaveDto.getPassword())
                        .param("nickname", memberSaveDto.getNickname()))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));

        List<Member> list = memberRepository.findAll();

        //when
        mvc.perform(put("/members/{memberId}", list.get(0).getId())
                .param("password", memberSaveDto.getPassword())
                .param("nickname", "김영한"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));

        //then
        Member member = memberService.findById(list.get(0).getId()).get();
        log.info("nickname : {}", member.getNickname());
    }
}