package project.boardservice.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.boardservice.domain.Comment;
import project.boardservice.domain.Post;
import project.boardservice.dto.CommentSaveDto;
import project.boardservice.dto.CommentUpdateDto;
import project.boardservice.service.CommentService;
import project.boardservice.service.PostService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    // 댓글 전체 조회
    @GetMapping
    public String comments(@PathVariable long postId, Model model) {
        List<Comment> comments = commentService.findComments(postId);
        for (Comment comment : comments) {
            log.info("comment : {}", comment.getContent());
        }
        return "ok";
//        return "comments/comments";
    }

    // 댓글 등록
    @PostMapping
    public String addComment(@PathVariable long postId,
                             @Validated @ModelAttribute CommentSaveDto commentSaveDto, BindingResult bindingResult) {
        // 검증 실패
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "fail";
        }
        
        // 성공 로직
        Post post = postService.findById(postId).get();
        commentService.save(post.getMember().getId(), postId, commentSaveDto);
        return "ok";
//        return "redirect:/posts/{postId}";
    }

    // 댓글 수정 페이지
    @GetMapping("/{commentId}")
    public String updateCommentForm(@PathVariable long postId, @PathVariable long commentId, Model model) {
        return "ok";
//        return "comments/updateCommentForm";
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public String updateComment(@PathVariable long postId, @PathVariable long commentId,
                                @Validated @ModelAttribute CommentUpdateDto commentUpdateDto, BindingResult bindingResult) {
        // 검증 실패
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "fail";
        }
        
        // 성공 로직
        Post post = postService.findById(postId).get();
        commentService.update(post.getMember().getId(), postId, commentId, commentUpdateDto);
        return "ok";
        //        return "redirect:/posts/{postId}";
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable long postId, @PathVariable long commentId) {
        Post post = postService.findById(postId).get();
        commentService.delete(post.getMember().getId(), postId, commentId);
        return "ok";
//        return "redirect:/posts/{postId}";
    }

}
