package project.boardservice.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.boardservice.domain.Comment;
import project.boardservice.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    // 댓글 작성
    public void save(Comment comment) {
        em.persist(comment);
    }

    // 댓글 조회
    public Optional<Comment> findById(Long commentId) {
        Comment comment = em.find(Comment.class, commentId);
        return Optional.ofNullable(comment);
    }

    // 게시글 별 댓글 전체 조회
    public List<Comment> findComments(Long postId) {
        return em.createQuery("select c from Comment c where c.post.id=:postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    // 댓글 삭제
    public void delete(Comment comment) {
        em.remove(comment);
    }
}