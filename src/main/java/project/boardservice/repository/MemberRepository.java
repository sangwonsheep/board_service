package project.boardservice.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.boardservice.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    // 회원 가입
    public void save(Member member) {
        em.persist(member);
    }

    // 회원 정보 조회
    public Optional<Member> findById(Long memberId) {
        Member member = em.find(Member.class, memberId);
        return Optional.ofNullable(member);
    }

    // 로그인 ID 조회
    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getName().equals(loginId))
                .findFirst();
    }

    // 회원 전체 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 회원 아이디 전체 조회
    public List<Member> findAllByName(String name) {
        return em.createQuery("select m from Member m where m.name=:name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    // 회원 이름 전체 조회
    public List<Member> findAllByNickname(String nickname) {
        return em.createQuery("select m from Member m where m.nickname=:nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();
    }

}
