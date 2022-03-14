package jpabook2.jpashop2.domain.service;

import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 컴포넌트 스캔 자동 등록으로 스프링 빈 등록됨
@Transactional(readOnly = true) // JPA의 모든 로직은 트랜잭션 안에서 실행되는 것이 좋다. 전체는 읽기 전용으로 세팅하되, 저장은 별도로 @Transactional 처리한다
public class MemberService {


    final MemberRepository memberRepository; // 생성자 인젝션을 쓰기 떄문에 final을 해도 된다.

    // setter 인젝션은 테스트가 간편하지만, setter기 때문에 변경될 가능성이 있어 위험하다는 치명적인 단점이 있다
    // 대안으로 요새는 생성자 인젝션을 많이 사용한다. 중간에 변경될 위험이 없기 때문에 setter보다 안전하다.
    @Autowired // 생성자가 한 개만 있을 경우 @Autowired를 생략해도 스프링이 자동 주입한다
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 가입
    @Transactional // @Transactional의 기본 readOnly 값은 false다
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
