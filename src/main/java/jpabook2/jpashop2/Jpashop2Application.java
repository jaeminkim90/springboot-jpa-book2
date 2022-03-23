package jpabook2.jpashop2;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Jpashop2Application {

	public static void main(String[] args) {

		SpringApplication.run(Jpashop2Application.class, args);
		}

		@Bean
		Hibernate5Module hibernate5Module() {
			Hibernate5Module hibernate5Module = new Hibernate5Module();
			// hibernate5Module.configure(Feature.FORCE_LAZY_LOADING, true);
			return hibernate5Module;
		}
}
