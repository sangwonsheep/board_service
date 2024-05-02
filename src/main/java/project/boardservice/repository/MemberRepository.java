package project.boardservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.boardservice.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name=:name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Member> findByNickname(String nickname) {
        return em.createQuery("select m from Member m where m.nickname=:nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();
    }

}