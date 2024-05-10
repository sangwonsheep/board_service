package project.boardservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.boardservice.dto.PostSaveDto;
import project.boardservice.dto.PostUpdateDto;
import project.boardservice.repository.PostSearch;
import project.boardservice.service.PostService;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 전체 조회
    @GetMapping
    public String posts(@ModelAttribute PostSearch postSearch, Model model) {
        return "posts/posts";
    }

    // 게시글 조회
    @GetMapping("/{postId}")
    public String post(@PathVariable long postId, Model model){
        return "posts/post";
    }

    // 게시글 등록 페이지
    @GetMapping("/add")
    public String addPostForm(){
        return "posts/addPostForm";
    }

    // 게시글 등록
    @PostMapping("/add")
    public String addPost(@ModelAttribute PostSaveDto postSaveDto) {
        return "redirect:/";
    }

    // 게시글 수정 페이지
    @GetMapping("/{postId}/edit")
    public String editPostForm(@PathVariable Long postId) {
        return "posts/editPostForm";
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public String editPost(@PathVariable Long postId, @ModelAttribute PostUpdateDto postUpdateDto) {
        return "redirect:/posts/{postId}";
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable Long postId){
        return "redirect:/";
    }

}
