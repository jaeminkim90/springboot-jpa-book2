package jpabook2.jpashop2.repository.order;

import java.util.List;
import jpabook2.jpashop2.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

	// select m from Member m where m.name = ?을 자동으로 만든다
	List<Member> findByName(String name);
}
