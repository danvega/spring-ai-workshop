package dev.danvega.workshop;

import dev.danvega.workshop.functions.WeatherConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;

@EnableConfigurationProperties(WeatherConfigProperties.class)
@SpringBootApplication
public class Application {

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(Application.class, args);
	}

}
