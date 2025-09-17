package com.danlju.tulip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TulipApplication {

	public static void main(String[] args) {
		SpringApplication.run(TulipApplication.class, args);
	}

}
