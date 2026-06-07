package com.pao.project.catalog.service;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class AuditService {
    private static AuditService instance;
    private static final String FILE_PATH = "audit.csv";
    private AuditService() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }
    public synchronized void logAction(String actionName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            writer.println(actionName + "," + timestamp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
