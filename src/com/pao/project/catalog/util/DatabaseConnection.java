package com.pao.project.catalog.util;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private DatabaseConnection() {
        try {
            Properties props = new Properties();
            try (InputStream in = Files.newInputStream(Paths.get("resources/db.properties"))) {
                props.load(in);
            }
            String url = props.getProperty("db.url");
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(url);
            initSchema();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Eroare la conectarea la baza de date!");
        }
    }
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }
    private void initSchema() {
        try {
            String schema = new String(Files.readAllBytes(Paths.get("resources/schema.sql")));
            String[] commands = schema.split(";");
            try (Statement stmt = connection.createStatement()) {
                for (String command : commands) {
                    if (!command.trim().isEmpty()) {
                        stmt.execute(command);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Atenție: Baza de date este deja initializata sau schema nu a rulat perfect.");
        }
    }
}
