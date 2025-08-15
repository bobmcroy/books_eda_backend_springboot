package books.eda.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BooksEdaExampleApplication {

	public static void main(String[] args) {
		// Set the Spring profile to "local" to pick up your application-local.properties
        System.setProperty("spring.profiles.active", "local");
        SpringApplication.run(BooksEdaExampleApplication.class, args);
	}

}
