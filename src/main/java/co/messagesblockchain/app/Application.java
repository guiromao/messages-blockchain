package co.messagesblockchain.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	/*
	To deploy on localhost:
	mvnw spring-boot:run
	 */

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
