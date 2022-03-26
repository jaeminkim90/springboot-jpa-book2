package jpabook2.jpashop2;

import org.junit.Test;

class sampleTest {

	@Test
	void sampleTest() throws Exception{
		int result = 100;            // result 라는 변수를 선언하고, 100이라는 숫자를 저장함

		for (int i=2; i<10; i=i+1){  // i는 2부터 시작해 10보다 작을때까지 1씩 더해짐
			result = result - i;      // result 라는 변수에 result에서 i를 뺀 값을 다시 저장함
		}
		System.out.println("result = " + result);

	}

}
