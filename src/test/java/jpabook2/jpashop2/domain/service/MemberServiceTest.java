package jpabook2.jpashop2.domain.service;

import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // 스프링 통합 테스트를 위해 설정
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
    public void 회원가입() throws Exception {

        // given: 주어진 상황
        Member member = new Member();
        member.setName("테스트용_멤버_이름2");

        // when: 특정 조건
        Long savedId = memberService.join(member);

        // then: 예상되는 결과
        em.flush(); // 영속성 컨텍스트의 내용을 DB에 반영하기 때문에 롤백환경에서도 DB에 member가 저장된다
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외() throws Exception {

        // given

        // when

        // then
    }

}
