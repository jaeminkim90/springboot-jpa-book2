package jpabook2.jpashop2.service;

import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.repository.MemberRepository;
import jpabook2.jpashop2.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // Junit 실행할 때 스프링과 통합해서 실행하는 설정
@SpringBootTest // 스프링 부트를 띄우고 테스트(없으면 @Autowired 모두 실패)
@Transactional // 각각의 테스트를 실행할 때마다 트랜잭션을 시작하고 테스트가 끝나면 트랜잭션 강제 롤백
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    @Rollback(false)
    public void 회원가입() throws Exception {

        // given: 주어진 상황
        Member member = new Member();
        member.setName("테스트용_멤버_이름2");

        // when: 특정 조건
        Long savedId = memberService.join(member);

        // then: 예상되는 결과
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class) // try catch로 에러를 잡지 않아도 된다.
    public void 중복_회원_예외() throws Exception {

        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);
        memberService.join(member2); // 예외가 발생해야하는 것이 정상
        // annotation에서 expected 설정을 통해 예상되는 예외를 확인할 수 있다

        // then
        fail("예외가 발생해야 합니다!");
    }
}
