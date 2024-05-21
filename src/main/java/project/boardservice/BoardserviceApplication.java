package project.boardservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import project.boardservice.service.MemberService;
import project.boardservice.service.PostService;

@SpringBootApplication
public class BoardserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardserviceApplication.class, args);
	}

	@Bean TestDataInit testDataInit(MemberService memberService, PostService postService){
		return new TestDataInit(memberService, postService);
	}
}
