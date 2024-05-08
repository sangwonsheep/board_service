package project.boardservice.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.boardservice.domain.Member;
import project.boardservice.domain.Post;
import project.boardservice.dto.MemberSaveDto;
import project.boardservice.dto.PostSaveDto;
import project.boardservice.dto.PostUpdateDto;
import project.boardservice.exception.UnauthorizedMemberException;
import project.boardservice.repository.PostSearch;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired MemberService memberService;
    @Autowired PostService postService;

    @Test
    void save() {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        PostSaveDto postSaveDto = new PostSaveDto("test!!", "test");

        //when
        Member savedMember = memberService.save(memberSaveDto);
        Post post = postService.save(savedMember.getId(), postSaveDto);
        postService.findById(post.getId());

        //then
        assertThat(savedMember).isEqualTo(post.getMember());
        assertThat(post.getTitle()).isEqualTo(postSaveDto.getTitle());
        assertThat(post.getContent()).isEqualTo(postSaveDto.getContent());
        assertThat(post.getWriter()).isEqualTo(savedMember.getNickname());
        assertThat(post.getCreatedDate()).isEqualTo(post.getModifiedDate());
        assertThat(post.getView()).isEqualTo(1);
    }

    @Test
    void update() {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        PostSaveDto postSaveDto = new PostSaveDto("test!", "tt");

        Member member = memberService.save(memberSaveDto);
        Post post = postService.save(member.getId(), postSaveDto);
        log.info("save = {}", post.getModifiedDate());

        //when
        PostUpdateDto postUpdateDto = new PostUpdateDto("abc", "qwe");
        postService.update(member.getId(), post.getId(), postUpdateDto);

        //then
        log.info("update = {}", post.getModifiedDate());

        assertThat(post.getMember().getId()).isEqualTo(member.getId());
        assertThat(post.getCreatedDate()).isNotEqualTo(post.getModifiedDate());
        assertThat(post.getTitle()).isEqualTo(postUpdateDto.getTitle());
        assertThat(post.getContent()).isEqualTo(postUpdateDto.getContent());
    }

    @Test
    void 허가되지_않은_사용자가_게시글수정() {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        MemberSaveDto memberSaveDto2 = new MemberSaveDto("park", "qweqweqwe", "박기자");
        PostSaveDto postSaveDto = new PostSaveDto("test!", "tt");

        Member kim = memberService.save(memberSaveDto);
        Member park = memberService.save(memberSaveDto2);
        Post post = postService.save(kim.getId(), postSaveDto);

        //when
        PostUpdateDto postUpdateDto = new PostUpdateDto("abc", "qwe");

        //then
        assertThatThrownBy(() -> postService.update(park.getId(), post.getId(), postUpdateDto))
                .isInstanceOf(UnauthorizedMemberException.class);
    }

    @Test
    void delete() {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        PostSaveDto postSaveDto = new PostSaveDto("test!", "tt");

        Member member = memberService.save(memberSaveDto);
        Post post = postService.save(member.getId(), postSaveDto);

        //when
        postService.delete(post.getMember().getId(), post.getId());

        //then
        assertThatThrownBy(() -> postService.findById(post.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 허가되지_않은_사용자가_게시글삭제() {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        MemberSaveDto memberSaveDto2 = new MemberSaveDto("park", "qweqweqwe", "박기자");
        PostSaveDto postSaveDto = new PostSaveDto("test!", "tt");

        //when
        Member kim = memberService.save(memberSaveDto);
        Member park = memberService.save(memberSaveDto2);
        Post post = postService.save(kim.getId(), postSaveDto);

        //then
        assertThatThrownBy(() -> postService.delete(park.getId(), post.getId()))
                .isInstanceOf(UnauthorizedMemberException.class);
    }

    @Test
    void 게시글_검색() {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        MemberSaveDto memberSaveDto2 = new MemberSaveDto("park", "qweqweqwe", "박기자");
        PostSaveDto postSaveDto = new PostSaveDto("testA", "tt");
        PostSaveDto postSaveDto2 = new PostSaveDto("testB", "tt");
        PostSaveDto postSaveDto3 = new PostSaveDto("testC", "tt");
        PostSaveDto postSaveDto4 = new PostSaveDto("testD", "tt");

        Member memberA = memberService.save(memberSaveDto);
        Member memberB = memberService.save(memberSaveDto2);
        postService.save(memberA.getId(), postSaveDto);
        postService.save(memberA.getId(), postSaveDto2);
        postService.save(memberB.getId(), postSaveDto3);
        postService.save(memberB.getId(), postSaveDto4);

        PostSearch blank = new PostSearch("");
        PostSearch searchA = new PostSearch("A");
        PostSearch searchB = new PostSearch("B");
        PostSearch searchC = new PostSearch("C");
        PostSearch searchD = new PostSearch("D");
        PostSearch searchTest = new PostSearch("test");

        //when
        List<Post> postBlank = postService.findPosts(blank);
        List<Post> postSearchA = postService.findPosts(searchA);
        List<Post> postSearchB = postService.findPosts(searchB);
        List<Post> postSearchC = postService.findPosts(searchC);
        List<Post> postSearchD = postService.findPosts(searchD);
        List<Post> postSearchTest = postService.findPosts(searchTest);

        //then
        assertThat(postBlank.size()).isEqualTo(4);
        assertThat(postSearchA.get(0).getTitle()).isEqualTo("testA");
        assertThat(postSearchB.get(0).getTitle()).isEqualTo("testB");
        assertThat(postSearchC.get(0).getTitle()).isEqualTo("testC");
        assertThat(postSearchD.get(0).getTitle()).isEqualTo("testD");
        assertThat(postSearchTest.size()).isEqualTo(4);
    }

}