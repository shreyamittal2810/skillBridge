package com.skillbridge.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
class SkillbridgeEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillbridgeEurekaServerApplication.class, args);
	}

}
