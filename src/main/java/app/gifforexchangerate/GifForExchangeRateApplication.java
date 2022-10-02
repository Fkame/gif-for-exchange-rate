package app.gifforexchangerate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GifForExchangeRateApplication {

	public static void main(String[] args) {
		SpringApplication.run(GifForExchangeRateApplication.class, args);
	}

}
