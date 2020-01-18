package me.study.studyjpashop.service;

import lombok.RequiredArgsConstructor;
import me.study.studyjpashop.domain.Member;
import me.study.studyjpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 해당 옵션은 jpa 내부에서 성능을 최적화 한다. default false
//@RequiredArgsConstructor final 로 정의한 필드만 생성자 초기화
public class MemberService {


    private final MemberRepository memberRepository; //필드 인젝션은 test하는 경우엔 바꿀수 없다. 생성자 주입이 조금더 유연하다., final로 설정시 컴파일 시점에 확인 가능.

    @Autowired // 생성자가 하나만 있을경우 autowired 애노테이션 생략 가능.
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     * @param member
     * @return
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // Exception
        List<Member> findMembers = memberRepository.findByName(member.getName()); //unique 제약 조건이 더 안전하다.
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원 입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional // command와 query를 철저히 분리한다.
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
