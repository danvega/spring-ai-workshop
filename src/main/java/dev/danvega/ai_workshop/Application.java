package dev.danvega.ai_workshop;

import dev.danvega.ai_workshop.functions.WeatherConfigProperties;
import dev.danvega.ai_workshop.openai.HelloOpenAI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;

@EnableConfigurationProperties(WeatherConfigProperties.class)
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
