package project.boardservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import project.boardservice.domain.Post;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    // 게시글 저장
    public void save(Post post) {
        em.persist(post);
    }

    // 게시글 전체 조회
    public List<Post> findAll() {
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }

    // 게시글 상세 조회
    public Optional<Post> findById(Long postId) {
        Post post = em.find(Post.class, postId);
        return Optional.ofNullable(post);
    }

    // 게시글 삭제
    public void delete(Post post) {
        em.remove(post);
    }

    // 게시글 검색
    public List<Post> findAllByString(PostSearch postSearch) {
        String jpql = "select p from Post p";
        boolean isFirstCondition = true;

        if(StringUtils.hasText(postSearch.getTitle())){
            if(isFirstCondition){
                jpql += " where";
                isFirstCondition = false;
            }
            jpql += " p.title like :title";
        }

        TypedQuery<Post> query = em.createQuery(jpql, Post.class)
                .setMaxResults(1000);

        if(StringUtils.hasText(postSearch.getTitle())) {
            query = query.setParameter("title", "%" + postSearch.getTitle() + "%");
        }

        return query.getResultList();
    }
}
