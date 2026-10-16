package com.project.exam;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConnectionTest {

	@Bean
	public CommandLineRunner testConnection(DataSource dataSource)
	{
		return args -> {
			try (Connection conn = dataSource.getConnection()) {
				System.out.println("DB 연결 성공");
				System.out.println("DB URL: " + conn.getMetaData().getURL());
				System.out.println("DB NAME: " + conn.getCatalog());
			}
		};
	}
}