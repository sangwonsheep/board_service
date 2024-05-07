package project.boardservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import project.boardservice.domain.Member;
import project.boardservice.domain.Post;
import project.boardservice.dto.PostSaveDto;
import project.boardservice.dto.PostUpdateDto;
import project.boardservice.exception.UnauthorizedMemberException;
import project.boardservice.repository.MemberRepository;
import project.boardservice.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 게시글 전체 조회
    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    // 게시글 상세 조회
    public Optional<Post> findById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();

        // 조회수 증가
        post.increaseView();
        return Optional.ofNullable(post);
    }

    // 게시글 작성
    @Transactional
    public Post save(Long memberId, @Valid PostSaveDto postSaveDto) {
        // 엔티티 조회
        Member member = memberRepository.findById(memberId).orElseThrow();

        // 게시글 생성
        Post post = Post.createPost(member, postSaveDto);

        // 게시글 저장
        postRepository.save(post);
        return post;
    }

    // 게시글 수정
    @Transactional
    public void update(Long memberId, Long postId, @Valid PostUpdateDto postUpdateDto) {
        // 엔티티 조회
        Member member = memberRepository.findById(memberId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        // 수정
        if(member == post.getMember())
            post.updatePost(postUpdateDto.getTitle(), postUpdateDto.getContent(), postUpdateDto.getModifiedDate());
        else {
            throw new UnauthorizedMemberException("허가되지 않은 사용자입니다.");
        }
    }

    // 게시글 삭제
    @Transactional
    public void delete(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        if(member == post.getMember())
            postRepository.delete(post);
        else {
            throw new UnauthorizedMemberException("허가되지 않은 사용자입니다.");
        }
    }
}
