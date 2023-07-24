package com.example.dodam;
import com.example.dodam.config.auth.AuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AuthProperties.class)
public class DodamApplication {

	public static void main(String[] args) {
		SpringApplication.run(DodamApplication.class, args);
	}
}


