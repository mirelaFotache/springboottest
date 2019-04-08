package com.pentalog.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.pentalog"})
public class BookstoreApplication {


	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
		//ApplicationContext context = SpringApplication.run(BookstoreApplication.class, args);
		//System.out.println(context.getBeansOfType(Object.class));
	}

}
