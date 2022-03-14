package jpabook2.jpashop2.domain.service;

import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 컴포넌트 스캔 자동 등록으로 스프링 빈 등록됨
public class MemberService {

    @Autowired // 자동으로 의존 관계 주입
    private MemberRepository memberRepository;

    // 회원 가입
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원을 검증하는 메서드
        memberRepository.save(member);
        return member.getId(); // Repository에서 영속성 컨텍스트에 들어가는 순간 id가 생성되기 때문에 반환이 가능하다
    }

    private void validateDuplicateMember(Member member) {
        // 중복일 경우 예외 발생
        // 찾는 방법: repository에서 같은 이름으로 저장된 객체가 있는지 조회해 본다
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }

    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 한 건만 조회
    public Member findOne(Long memberId) {
        return memberRepository.FindOne(memberId);

    }

}
