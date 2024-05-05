package project.boardservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import project.boardservice.domain.Member;
import project.boardservice.dto.MemberSaveDto;
import project.boardservice.dto.MemberUpdateDto;
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

    // 회원 정보 조회
    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 회원 가입
    @Transactional
    public Member save(@Valid MemberSaveDto memberSaveDto) {
        Member member = new Member(memberSaveDto.getName(), memberSaveDto.getPassword(), memberSaveDto.getNickname());
        validateDuplicateSaveMember(memberSaveDto);
        memberRepository.save(member);
        return member;
    }

    // 회원 정보 수정
    @Transactional
    public void update(Long memberId, @Valid MemberUpdateDto updateParam) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        validateDuplicateUpdateMember(updateParam);
        member.updateMember(updateParam.getPassword(), updateParam.getNickname());
    }

    // 저장 시 회원 중복 검사
    private void validateDuplicateSaveMember(MemberSaveDto memberSaveDto) {
        List<Member> findMembersName = memberRepository.findAllByName(memberSaveDto.getName());
        if (!findMembersName.isEmpty()) {
            throw new MemberNameDuplicateException("이미 존재하는 아이디입니다.");
        }

        List<Member> findMembersNickname = memberRepository.findAllByNickname(memberSaveDto.getNickname());
        if (!findMembersNickname.isEmpty()) {
            throw new MemberNicknameDuplicateException("이미 사용 중인 이름입니다.");
        }
    }

    // 수정 시 회원 중복 검사
    private void validateDuplicateUpdateMember(MemberUpdateDto memberDto) {
        List<Member> findMembersNickname = memberRepository.findAllByNickname(memberDto.getNickname());
        if (!findMembersNickname.isEmpty()) {
            throw new MemberNicknameDuplicateException("이미 사용 중인 이름입니다.");
        }
    }

}
