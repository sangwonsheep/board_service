package project.boardservice.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.boardservice.domain.Post;
import project.boardservice.dto.PostSaveDto;
import project.boardservice.dto.PostUpdateDto;
import project.boardservice.repository.PostSearch;
import project.boardservice.service.PostService;

import java.util.List;

@Slf4j
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
        Post post = postService.findById(postId).get();
        log.info("게시글 조회 : {}", post.getTitle());
        return "posts/post";
    }

    // 게시글 등록 페이지
    @GetMapping("/add")
    public String addPostForm(){
        return "posts/addPostForm";
    }

    // 게시글 등록
    @PostMapping("/add")
    public String addPost(@RequestParam Long memberId, @Validated @ModelAttribute PostSaveDto postSaveDto, BindingResult bindingResult) {
        // 검증 실패
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "fail";
        }

        // 성공 로직
        postService.save(memberId, postSaveDto);
        log.info("게시글 저장");
        return "redirect:/";
    }

    // 게시글 수정 페이지
    @GetMapping("/{postId}/edit")
    public String editPostForm(@PathVariable Long postId) {
        return "posts/editPostForm";
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public String editPost(@RequestParam Long memberId, @PathVariable Long postId,
                           @Validated @ModelAttribute PostUpdateDto postUpdateDto, BindingResult bindingResult) {
        // 검증 실패
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "fail";
        }

        // 성공 로직
        postService.update(memberId, postId, postUpdateDto);
        return "redirect:/posts/{postId}";
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public String deletePost(@RequestParam Long memberId, @PathVariable Long postId){
        postService.delete(memberId, postId);
        return "redirect:/";
    }

}
