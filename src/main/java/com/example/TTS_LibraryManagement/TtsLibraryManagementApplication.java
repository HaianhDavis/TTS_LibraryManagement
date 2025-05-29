package com.example.TTS_LibraryManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
public class TtsLibraryManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TtsLibraryManagementApplication.class, args);
	}

}
