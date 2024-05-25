package project.boardservice.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.boardservice.domain.Comment;
import project.boardservice.domain.Member;
import project.boardservice.domain.Post;
import project.boardservice.dto.CommentSaveDto;
import project.boardservice.dto.CommentUpdateDto;
import project.boardservice.dto.MemberSaveDto;
import project.boardservice.dto.PostSaveDto;
import project.boardservice.exception.UnauthorizedMemberException;

import java.util.List;
import java.util.NoSuchElementException;

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
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        PostSaveDto postSaveDto = new PostSaveDto("test!!", "test");
        CommentSaveDto commentSaveDto = new CommentSaveDto("hi comment");

        Member member = memberService.save(memberSaveDto);
        Post post = postService.save(member.getId(), postSaveDto);
        Comment comment = commentService.save(member.getId(), post.getId(), commentSaveDto);

        //when
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto("update!!");
        commentService.update(member.getId(), post.getId(), comment.getId(), commentUpdateDto);

        //then
        assertThat(comment.getContent()).isEqualTo("update!!");
    }

    @Test
    void delete() {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        PostSaveDto postSaveDto = new PostSaveDto("test!!", "test");
        CommentSaveDto commentSaveDto = new CommentSaveDto("hi comment");

        Member member = memberService.save(memberSaveDto);
        Post post = postService.save(member.getId(), postSaveDto);
        Comment comment = commentService.save(member.getId(), post.getId(), commentSaveDto);

        //when
        commentService.delete(member.getId(), post.getId(), comment.getId());

        //then
        assertThatThrownBy(() -> commentService.findById(comment.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 다른사람댓글_수정() {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        MemberSaveDto memberSaveDto2 = new MemberSaveDto("park", "qweqweqwe", "박기자");
        PostSaveDto postSaveDto = new PostSaveDto("test!!", "test");
        CommentSaveDto commentSaveDto = new CommentSaveDto("hi comment");
        CommentSaveDto commentSaveDto2 = new CommentSaveDto("comment test");

        Member member = memberService.save(memberSaveDto);
        Member member2 = memberService.save(memberSaveDto2);
        Post post = postService.save(member.getId(), postSaveDto);
        Comment comment = commentService.save(member.getId(), post.getId(), commentSaveDto);
        Comment comment2 = commentService.save(member2.getId(), post.getId(), commentSaveDto2);

        //when
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto("update!!");

        //then
        // 댓글 작성자가 아닌 사람이 수정하고자 할 때
        assertThatThrownBy(() -> commentService.update(member2.getId(), post.getId(), comment.getId(), commentUpdateDto))
                .isInstanceOf(UnauthorizedMemberException.class);
        // 게시글 작성자가 다른 댓글 수정은 불가
        assertThatThrownBy(() -> commentService.update(member.getId(), post.getId(), comment2.getId(), commentUpdateDto))
                .isInstanceOf(UnauthorizedMemberException.class);
    }

    @Test
    void 다른사람댓글_삭제() {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        MemberSaveDto memberSaveDto2 = new MemberSaveDto("park", "qweqweqwe", "박기자");
        PostSaveDto postSaveDto = new PostSaveDto("test!!", "test");
        CommentSaveDto commentSaveDto = new CommentSaveDto("hi comment");
        CommentSaveDto commentSaveDto2 = new CommentSaveDto("comment test");

        Member member = memberService.save(memberSaveDto);
        Member member2 = memberService.save(memberSaveDto2);
        Post post = postService.save(member.getId(), postSaveDto);
        Comment comment = commentService.save(member.getId(), post.getId(), commentSaveDto);
        Comment comment2 = commentService.save(member2.getId(), post.getId(), commentSaveDto2);

        //when
        // 게시글 작성자가 다른 사람의 댓글 삭제
        commentService.delete(member.getId(), post.getId(), comment2.getId());

        //then
        assertThatThrownBy(() -> commentService.findById(comment2.getId()))
                .isInstanceOf(NoSuchElementException.class);

        // 댓글 작성자가 아닌 사람이 다른 댓글 삭제하고자 할 때
        assertThatThrownBy(() -> commentService.delete(member2.getId(), post.getId(), comment.getId()))
                .isInstanceOf(UnauthorizedMemberException.class);
    }

    @Test
    void 게시글_삭제() {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        MemberSaveDto memberSaveDto2 = new MemberSaveDto("park", "qweqweqwe", "박기자");
        PostSaveDto postSaveDto = new PostSaveDto("test!!", "test");
        CommentSaveDto commentSaveDto = new CommentSaveDto("hi comment");
        CommentSaveDto commentSaveDto2 = new CommentSaveDto("comment test");

        Member member = memberService.save(memberSaveDto);
        Member member2 = memberService.save(memberSaveDto2);
        Post post = postService.save(member.getId(), postSaveDto);
        Comment comment = commentService.save(member.getId(), post.getId(), commentSaveDto);
        Comment comment2 = commentService.save(member2.getId(), post.getId(), commentSaveDto2);

        //when
        postService.delete(post.getMember().getId(), post.getId());

        //then
        // 게시글 삭제 시 관련 댓글까지 모두 삭제
        assertThatThrownBy(() -> commentService.findById(comment.getId()))
                .isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy(() -> commentService.findById(comment2.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    // 한 게시글에 포함된 전체 댓글 조회
    @Test
    @DisplayName("한 게시글에만 포함된 전체 댓글 조회")
    void post_comments() {
        //given
        MemberSaveDto memberSaveDto = new MemberSaveDto("kim", "qweqweqwe", "김기자");
        MemberSaveDto memberSaveDto2 = new MemberSaveDto("park", "qweqweqwe", "박기자");
        PostSaveDto postSaveDto = new PostSaveDto("post1", "post1");
        PostSaveDto postSaveDto2 = new PostSaveDto("post2", "post2");
        CommentSaveDto commentSaveDto = new CommentSaveDto("hi comment");
        CommentSaveDto commentSaveDto2 = new CommentSaveDto("comment test");
        CommentSaveDto commentSaveDto3 = new CommentSaveDto("first test");
        CommentSaveDto commentSaveDto4 = new CommentSaveDto("second test");
        CommentSaveDto commentSaveDto5 = new CommentSaveDto("third test");

        Member kim = memberService.save(memberSaveDto);
        Member park = memberService.save(memberSaveDto2);
        Post post1 = postService.save(kim.getId(), postSaveDto);
        Post post2 = postService.save(park.getId(), postSaveDto2);

        commentService.save(kim.getId(), post1.getId(), commentSaveDto);
        commentService.save(park.getId(), post1.getId(), commentSaveDto2);
        commentService.save(kim.getId(), post2.getId(), commentSaveDto3);
        commentService.save(kim.getId(), post2.getId(), commentSaveDto4);
        commentService.save(park.getId(), post2.getId(), commentSaveDto5);

        //when
        List<Comment> post1_comments = commentService.findComments(post1.getId());
        List<Comment> post2_comments = commentService.findComments(post2.getId());

        //then
        assertThat(post1_comments.size()).isEqualTo(2);
        assertThat(post2_comments.size()).isEqualTo(3);
    }
}