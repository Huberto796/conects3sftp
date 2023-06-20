package com.example.appjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExtensionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExtensionApplication.class, args);
		System.out.println("Hi");
	}

}
