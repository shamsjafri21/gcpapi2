package com.teknikality.api1;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
public class Api2Application {

	public static void main(String[] args) {
		SpringApplication.run(Api2Application.class, args);

	}

	
	@Bean
	public Docket swaggerConfiguration() {
		
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.ant("/sandboxapi1/**"))
				.apis(RequestHandlerSelectors.basePackage("com.teknikality.api1.controller"))
				.build()
				.apiInfo(apiDetails());
	}
	

	private ApiInfo apiDetails() {
		
		return new ApiInfo(
				"SandBox Api1",
				"SandBox Api1 for retriving data from SandBox Api1",
				"1.0",
				"Free to Use",
				new springfox.documentation.service.Contact("Mr Mao", "http://www.carconnect.uk/", "mao@gmail.com"),
				"API Licence",
				"http://www.carconnect.uk/",
				Collections.emptyList());
	}
	
}
