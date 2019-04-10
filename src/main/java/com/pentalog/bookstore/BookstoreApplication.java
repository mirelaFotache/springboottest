package com.pentalog.bookstore;

import com.pentalog.bookstore.utils.YAMLConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(YAMLConfig.class)
@SpringBootApplication(scanBasePackages={"com.pentalog"})
public class BookstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
		//ApplicationContext context = SpringApplication.run(BookstoreApplication.class, args);
		//System.out.println(context.getBeansOfType(Object.class));
	}
}
