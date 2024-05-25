package project.boardservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import project.boardservice.domain.Comment;
import project.boardservice.domain.Member;
import project.boardservice.domain.Post;
import project.boardservice.dto.CommentSaveDto;
import project.boardservice.dto.CommentUpdateDto;
import project.boardservice.exception.UnauthorizedMemberException;
import project.boardservice.repository.CommentRepository;
import project.boardservice.repository.MemberRepository;
import project.boardservice.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 댓글 조회
    public Optional<Comment> findById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        return Optional.ofNullable(comment);
    }

    // 게시글 별 댓글 전체 조회
    public List<Comment> findComments(Long postId) {
        return commentRepository.findComments(postId);
    }

    // 댓글 작성
    public Comment save(Long memberId, Long postId, @Valid CommentSaveDto commentSaveDto) {
        // 엔티티 생성
        Member member = memberRepository.findById(memberId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        // 댓글 생성
        Comment comment = Comment.createComment(member, post, commentSaveDto);

        // 댓글 저장
        commentRepository.save(comment);
        return comment;
    }

    // 댓글 수정
    public void update(Long memberId, Long postId, Long commentId, @Valid CommentUpdateDto commentUpdateDto) {
        // 엔티티 생성
        Member member = memberRepository.findById(memberId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        // 댓글 수정
        if(member == comment.getMember() && post == comment.getPost())
            comment.updateComment(commentUpdateDto.getContent());
        else {
            throw new UnauthorizedMemberException("허가되지 않은 사용자입니다.");
        }
    }

    // 댓글 삭제
    public void delete(Long memberId, Long postId, Long commentId) {
        // 엔티티 생성
        Member member = memberRepository.findById(memberId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        // 댓글 작성자 또는 게시글 작성자가 삭제
        if((member == comment.getMember() || member == post.getMember()) && post == comment.getPost())
            commentRepository.delete(comment);
        else {
            throw new UnauthorizedMemberException("허가되지 않은 사용자입니다.");
        }
    }
}
