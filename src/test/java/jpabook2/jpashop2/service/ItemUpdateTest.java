package jpabook2.jpashop2.service;

import jpabook2.jpashop2.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest

public class ItemUpdateTest {

    @Autowired EntityManager em;

    @Test
    void updateTest() throws Exception{
        Book book = em.find(Book.class, 1L); // book을 가져온다

        // Transactional 안에서는 '변경내용'을 Transactional commit 시점에 JPA가 자동으로 파악하여 업데이트 쿼리를 만든다.
        // 이러한 방식의 업데이트를 변경 감지(dirty checking)라고 한다.
        book.setName("변경내용");


       // given
    
    // when
    
    // then
    }

}
