package project.boardservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import project.boardservice.domain.Member;
import project.boardservice.dto.MemberDto;
import project.boardservice.exception.MemberNameDuplicateException;
import project.boardservice.exception.MemberNicknameDuplicateException;
import project.boardservice.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional
    public Member save(@Valid MemberDto memberDto) {
        Member member = new Member(memberDto.getName(), memberDto.getPassword(), memberDto.getNickname());
        validateDuplicateSaveMember(memberDto);
        memberRepository.save(member);
        return member;
    }

    // 회원 정보 조회
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    // 회원 정보 수정
    @Transactional
    public void update(Long id, @Valid MemberDto updateParam) {
        Member member = memberRepository.findById(id).orElseThrow();
        validateDuplicateUpdateMember(updateParam);
        member.updateMember(updateParam.getPassword(), updateParam.getNickname());
    }

    // 저장 시 회원 중복 검사
    private void validateDuplicateSaveMember(MemberDto memberDto) {
        List<Member> findMembersName = memberRepository.findByName(memberDto.getName());
        if (!findMembersName.isEmpty()) {
            throw new MemberNameDuplicateException("이미 존재하는 아이디입니다.");
        }

        List<Member> findMembersNickname = memberRepository.findByNickname(memberDto.getNickname());
        if (!findMembersNickname.isEmpty()) {
            throw new MemberNicknameDuplicateException("이미 사용 중인 이름입니다.");
        }
    }

    // 수정 시 회원 중복 검사
    private void validateDuplicateUpdateMember(MemberDto memberDto) {
        List<Member> findMembersNickname = memberRepository.findByNickname(memberDto.getNickname());
        if (!findMembersNickname.isEmpty()) {
            throw new MemberNicknameDuplicateException("이미 사용 중인 이름입니다.");
        }
    }

}
