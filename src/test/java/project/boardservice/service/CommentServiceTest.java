package project.boardservice.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.boardservice.domain.Comment;
import project.boardservice.domain.Member;
import project.boardservice.domain.Post;
import project.boardservice.dto.CommentSaveDto;
import project.boardservice.dto.MemberSaveDto;
import project.boardservice.dto.PostSaveDto;

import static org.assertj.core.api.Assertions.*;


@Slf4j
@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired MemberService memberService;
    @Autowired PostService postService;
    @Autowired CommentService commentService;

    @Test
    void save() {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        PostSaveDto postSaveDto = new PostSaveDto("test!!", "test");
        CommentSaveDto commentSaveDto = new CommentSaveDto("hi comment");

        //when
        Member member = memberService.save(memberSaveDto);
        Post post = postService.save(member.getId(), postSaveDto);
        Comment comment = commentService.save(member.getId(), post.getId(), commentSaveDto);

        //then
        log.info("member_name : {}", comment.getMember().getNickname());
        log.info("post_name : {}", comment.getPost().getTitle());
        log.info("content : {}", comment.getContent());
        assertThat(member).isEqualTo(comment.getMember());
        assertThat(post).isEqualTo(comment.getPost());
        assertThat(comment.getContent()).isEqualTo("hi comment");
    }

    @Test
    void update() {
        
    }

    @Test
    void delete() {

    }

    @Test
    void 게시글_삭제() {

    }

}