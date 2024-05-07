package project.boardservice.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.boardservice.domain.Comment;

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

    // 댓글 삭제
    public void delete(Comment comment) {
        em.remove(comment);
    }
}