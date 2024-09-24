package pl.illchess.websocket.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "pl.illchess")
public class IllChessWebSocketApplication {

	//@EnableJpaRepositories, @EntityScan, @Configuration + @ComponentScan + @Bean (nad metodą)

	public static void main(String[] args) {
		SpringApplication.run(IllChessWebSocketApplication.class, args);
	}

}
