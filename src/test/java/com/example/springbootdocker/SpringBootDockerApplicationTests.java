package com.example.springbootdocker;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootDockerApplicationTests {

	@BeforeAll
	static void setup() {
		Security.addProvider(new BouncyCastleProvider());
	}

	@Test
	void contextLoads() {
	}

}
