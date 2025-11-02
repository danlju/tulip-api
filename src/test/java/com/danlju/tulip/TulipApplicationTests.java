package com.danlju.tulip;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TulipApplicationTests {

	static {
		System.setProperty("GITHUB_TOKEN", "not empty");
	}

	@Test
	void contextLoads() {
	}

}
