package project.boardservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import project.boardservice.domain.Member;
import project.boardservice.dto.MemberPasswordUpdateDto;
import project.boardservice.dto.MemberSaveDto;
import project.boardservice.dto.MemberUpdateDto;
import project.boardservice.exception.MemberNameDuplicateException;
import project.boardservice.exception.MemberNicknameDuplicateException;
import project.boardservice.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
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

    // 닉네임 수정
    @Transactional
    public void update(Long memberId, @Valid MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        // 현재 닉네임과 수정된 닉네임이 같은 경우
        if(!member.getNickname().equals(memberUpdateDto.getNickname())) {
            validateDuplicateUpdateMember(memberUpdateDto);
        }

        member.updateMemberNickname(memberUpdateDto.getNickname());
    }

    // 비밀번호 수정
    @Transactional
    public void updatePassword(Long memberId, @Valid MemberPasswordUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        // 현재 비밀번호가 일치하는지 확인
        if(member.getPassword().equals(memberUpdateDto.getPassword())) {
            // 새 비밀번호, 확인이 일치하는지 확인
            if(memberUpdateDto.getNew_password().equals(memberUpdateDto.getNew_password_check())){
                member.updateMemberPassword(memberUpdateDto.getNew_password());
            }
        }
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
    private void validateDuplicateUpdateMember(MemberUpdateDto memberUpdateDto) {
        List<Member> findMembersNickname = memberRepository.findAllByNickname(memberUpdateDto.getNickname());
        if (!findMembersNickname.isEmpty()) {
            throw new MemberNicknameDuplicateException("이미 사용 중인 이름입니다.");
        }
    }

}
