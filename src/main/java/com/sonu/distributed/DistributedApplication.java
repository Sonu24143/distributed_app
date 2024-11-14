package com.sonu.distributed;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class DistributedApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(DistributedApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//do nothing yet
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println("Password:[admin], encrypted["+encoder.encode("admin")+"].");
	}
}
