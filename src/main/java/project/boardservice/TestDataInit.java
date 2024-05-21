package project.boardservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import project.boardservice.domain.Member;
import project.boardservice.domain.Post;
import project.boardservice.dto.MemberSaveDto;
import project.boardservice.dto.PostSaveDto;
import project.boardservice.repository.MemberRepository;
import project.boardservice.repository.PostRepository;
import project.boardservice.repository.PostSearch;
import project.boardservice.service.MemberService;
import project.boardservice.service.PostService;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {

    private final MemberService memberService;
    private final PostService postService;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");

        Member member1 = memberService.save(new MemberSaveDto("member1", "qweqweqwe", "멤버1"));
        Member member2 = memberService.save(new MemberSaveDto("member2", "qweqweqwe", "멤버2"));
        postService.save(member1.getId(), new PostSaveDto("첫번째 제목", "첫번째 내용"));
        postService.save(member2.getId(), new PostSaveDto("두번째 제목", "두번째 내용"));
    }
}
