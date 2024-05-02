package project.boardservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.boardservice.domain.Member;
import project.boardservice.exception.MemberNameDuplicateException;
import project.boardservice.exception.MemberNicknameDuplicateException;
import project.boardservice.repository.MemberRepository;
import project.boardservice.repository.MemberUpdateDto;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional
    public Member save(Member member) {
        validateDuplicateSaveMember(member);
        memberRepository.save(member);
        return member;
    }

    // 회원 정보 조회
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    // 회원 정보 수정
    @Transactional
    public void update(Long id, MemberUpdateDto updateParam) {
        Member member = memberRepository.findById(id).orElseThrow();
        validateDuplicateUpdateMember(updateParam);
        member.updateMember(updateParam.getName(), updateParam.getPassword(), updateParam.getNickname());
    }

    // 저장할 때 회원 중복 검사
    private void validateDuplicateSaveMember(Member member) {
        List<Member> findMembersName = memberRepository.findByName(member.getName());
        if (!findMembersName.isEmpty()) {
            throw new MemberNameDuplicateException("이미 존재하는 아이디입니다.");
        }

        List<Member> findMembersNickname = memberRepository.findByNickname(member.getNickname());
        if (!findMembersNickname.isEmpty()) {
            throw new MemberNicknameDuplicateException("이미 존재하는 이름입니다.");
        }
    }

    // 수정할 때 중복 검사
    private void validateDuplicateUpdateMember(MemberUpdateDto updateParam) {
        List<Member> findMembersName = memberRepository.findByName(updateParam.getName());
        if (!findMembersName.isEmpty()) {
            throw new MemberNameDuplicateException("이미 존재하는 아이디입니다.");
        }

        List<Member> findMembersNickname = memberRepository.findByNickname(updateParam.getNickname());
        if (!findMembersNickname.isEmpty()) {
            throw new MemberNicknameDuplicateException("이미 존재하는 이름입니다.");
        }
    }

}
