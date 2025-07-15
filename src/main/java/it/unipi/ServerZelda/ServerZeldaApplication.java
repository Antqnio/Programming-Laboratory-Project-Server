package it.unipi.ServerZelda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 
 * @author Antonio
 * dbName deve essere lo stesso presente in application.properties alla creazione del database.
 * Se lo si vuole cambiare, cambiarlo sia in application.properties che qui.
 */
@SpringBootApplication
public class ServerZeldaApplication {
        public final static String dbName = "655055";
        private static final Logger logger = LogManager.getLogger(ServerZeldaApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(ServerZeldaApplication.class, args);
	}

}
