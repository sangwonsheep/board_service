package project.boardservice.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.boardservice.domain.Member;
import project.boardservice.domain.Post;
import project.boardservice.dto.MemberSaveDto;
import project.boardservice.dto.PostDto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired MemberService memberService;
    @Autowired PostService postService;

    @Test
    void save_and_increaseView() {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        PostDto postDto = new PostDto("test!!", "test");

        //when
        Member savedMember = memberService.save(memberSaveDto);
        Post post = postService.save(savedMember.getId(), postDto);
        postService.findById(post.getId());

        //then
        assertThat(savedMember).isEqualTo(post.getMember());
        assertThat(post.getTitle()).isEqualTo(postDto.getTitle());
        assertThat(post.getContent()).isEqualTo(postDto.getContent());
        assertThat(post.getWriter()).isEqualTo(savedMember.getNickname());
        assertThat(post.getCreatedDate()).isEqualTo(post.getModifiedDate());
        assertThat(post.getView()).isEqualTo(1);
    }

    @Test
    void update() {

    }

    @Test
    void delete() {

    }

}