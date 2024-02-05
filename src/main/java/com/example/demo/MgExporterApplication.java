package com.example.demo;

import com.example.demo.exporter.LogReaderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MgExporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(MgExporterApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(LogReaderService logReaderService) {
		return args -> {
			// Remplacez "/chemin/vers/votre/fichier.log" par le chemin réel vers le fichier de logs
			String logFilePath = "D:/Utilisateurs/steddi.andritiana/Stage/localhost_access_log.log";
			logReaderService.readAndProcessLogs(logFilePath);
		};
	}

}
