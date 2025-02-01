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
			String portString = args[0];
			String capacityString = args[1];
			String variantString = args[2];

			int port = Integer.parseInt(portString);
			int capacity = Integer.parseInt(capacityString);
			char variant = variantString.charAt(0);

			new ServerApp(port, capacity, variant, engine);
		} catch (Exception e) {System.out.println(e);}
	}
}
