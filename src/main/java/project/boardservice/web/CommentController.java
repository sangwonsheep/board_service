package project.boardservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.boardservice.dto.CommentSaveDto;
import project.boardservice.dto.CommentUpdateDto;
import project.boardservice.service.CommentService;

@Controller
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 전체 조회
    @GetMapping
    public String comments(@PathVariable long postId, Model model) {
        return "comments/comments";
    }

    // 댓글 등록
    @PostMapping
    public String addComment(@PathVariable long postId, @ModelAttribute CommentSaveDto commentSaveDto) {
        return "redirect:/posts/{postId}";
    }

    // 댓글 수정 페이지
    @GetMapping("/{commentId}")
    public String updateCommentForm(@PathVariable long postId, @PathVariable long commentId, Model model) {
        return "comments/updateCommentForm";
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public String updateComment(@PathVariable long postId, @PathVariable long commentId, @ModelAttribute CommentUpdateDto commentUpdateDto) {
        return "redirect:/posts/{postId}";
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable long postId, @PathVariable long commentId) {
        return "redirect:/posts/{postId}";
    }

}
