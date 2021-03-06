package jpabook2.jpashop2.repository;

import jpabook2.jpashop2.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository // component scan에 의해 자동으로 스프링 빈 등록, 관리됨
@RequiredArgsConstructor
public class MemberRepositoryOld {

    private final EntityManager em; // @RequiredArgsConstructor을 이용해서 자동으로 생성자 주입이 가능

    public void save(Member member) {
        em.persist(member); // 영속성 컨텍스트에 member가 올라가는 순간 @GeneratedValue에 의해 PK(id) 생성이 보장된다.
    }

    // 단건 조회
    public Member findOne(Long id) {
        return em.find(Member.class, id); // id를 이용해 Member.class 타입의 객체를 찾는다.
    }

    // 전체 조회(List 조회)
    public List<Member> findAll() {
        return em.createQuery("select m from Member as m", Member.class)
                .getResultList();
    }

    // 이름으로 조회
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
