package project.boardservice.web;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import project.boardservice.domain.Member;
import project.boardservice.domain.Post;
import project.boardservice.dto.MemberSaveDto;
import project.boardservice.dto.PostSaveDto;
import project.boardservice.repository.MemberRepository;
import project.boardservice.repository.PostRepository;
import project.boardservice.service.MemberService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired MockMvc mvc;
    @Autowired MemberRepository memberRepository;
    @Autowired PostRepository postRepository;

    @Test
    void 게시글_등록_조회() throws Exception {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        PostSaveDto postSaveDto = new PostSaveDto("제목1", "내용1");

        //when
        mvc.perform(post("/members/add")
                        .param("name", memberSaveDto.getName())
                        .param("password", memberSaveDto.getPassword())
                        .param("nickname", memberSaveDto.getNickname()))
                .andExpect(status().isOk());

        List<Member> members = memberRepository.findAll();

        //then
        mvc.perform(post("/posts/add")
                        .param("memberId", members.get(0).getId().toString())
                        .param("title", postSaveDto.getTitle())
                        .param("content", postSaveDto.getContent()))
                .andExpect(status().isOk());

        List<Post> posts = postRepository.findAll();

        mvc.perform(get("/posts/{postId}", posts.get(0).getId()))
                .andExpect(status().isOk());
    }

    @Test
    void 게시글_빈칸_검증() throws Exception {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        PostSaveDto postSaveDto = new PostSaveDto("", "");

        //when
        mvc.perform(post("/members/add")
                        .param("name", memberSaveDto.getName())
                        .param("password", memberSaveDto.getPassword())
                        .param("nickname", memberSaveDto.getNickname()))
                .andExpect(status().isOk());

        List<Member> members = memberRepository.findAll();

        //then
        mvc.perform(post("/posts/add")
                        .param("memberId", members.get(0).getId().toString())
                        .param("title", postSaveDto.getTitle())
                        .param("content", postSaveDto.getContent()))
                .andExpect(content().string("fail"));

    }

}