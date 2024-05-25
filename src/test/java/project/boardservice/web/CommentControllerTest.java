package project.boardservice.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import project.boardservice.domain.Comment;
import project.boardservice.domain.Member;
import project.boardservice.domain.Post;
import project.boardservice.dto.CommentSaveDto;
import project.boardservice.dto.MemberSaveDto;
import project.boardservice.dto.PostSaveDto;
import project.boardservice.repository.CommentRepository;
import project.boardservice.repository.MemberRepository;
import project.boardservice.repository.PostRepository;
import project.boardservice.service.CommentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired MockMvc mvc;
    @Autowired MemberRepository memberRepository;
    @Autowired PostRepository postRepository;
    @Autowired CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 등록")
    void save() throws Exception {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        PostSaveDto postSaveDto = new PostSaveDto("post1", "post1");
        CommentSaveDto commentSaveDto = new CommentSaveDto("hi comment");

        // 회원 가입
        mvc.perform(post("/members/add")
                        .param("name", memberSaveDto.getName())
                        .param("password", memberSaveDto.getPassword())
                        .param("nickname", memberSaveDto.getNickname()))
                .andExpect(status().isOk());

        List<Member> members = memberRepository.findAll();

        // 게시글 등록
        mvc.perform(post("/posts/add")
                        .param("memberId", members.get(0).getId().toString())
                        .param("title", postSaveDto.getTitle())
                        .param("content", postSaveDto.getContent()))
                .andExpect(status().isOk());

        List<Post> posts = postRepository.findAll();

        //when
        // 댓글 등록
        mvc.perform(post("/posts/{postId}/comments", posts.get(0).getId())
                        .param("content", commentSaveDto.getContent()))
                .andExpect(status().isOk());

        //then
        List<Comment> comments = commentRepository.findComments(posts.get(0).getId());

        for (Comment comment : comments) {
            log.info("comment : {}", comment.getContent());
        }
    }
}