package project.boardservice.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.boardservice.domain.Post;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    // 게시글 전체 조회
    public Post save(Post post) {
        em.persist(post);
        return post;
    }

    // 게시글 상세 조회

    // 게시글 저장

    // 게시글 삭제

}
