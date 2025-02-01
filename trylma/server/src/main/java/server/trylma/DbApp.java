package server.trylma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import server.trylma.components.GameEngine;

@SpringBootApplication
public class DbApp {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DbApp.class, args);
		GameEngine engine = context.getBean(GameEngine.class);
		
		try {
			new ServerApp(6000, 2, 'c', engine);
		} catch (Exception e) {System.out.println(e);}
	}
}
