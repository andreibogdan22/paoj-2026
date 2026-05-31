package com.pao.laboratory14.exercise2;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.repository.EvenimentRepository;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            EvenimentRepository repo = new EvenimentRepository();
            Scanner scanner = new Scanner(System.in);
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                
                String[] tokens = line.split("\\s+");
                String comanda = tokens[0];
                
                if (comanda.equals("ADD")) {
                    String nume = tokens[1];
                    String data = tokens[2];
                    int capacitate = Integer.parseInt(tokens[3]);
                    TipBilet tip = TipBilet.valueOf(tokens[4]);
                    Eveniment e = new Eveniment(0, nume, data, capacitate, tip);
                    repo.save(e);
                    System.out.println("Adaugat: [" + e.getId() + "] " + e.getNume());
                } else if (comanda.equals("LIST")) {
                    for (Eveniment e : repo.findAll()) {
                        System.out.println("[" + e.getId() + "] " + e.getNume() + " | " + e.getData() + " | cap=" + e.getCapacitate() + " | " + e.getTip().name());
                    }
                } else if (comanda.equals("DELETE")) {
                    int id = Integer.parseInt(tokens[1]);
                    int rows = repo.deleteImpl(id);
                    if (rows > 0) {
                        System.out.println("Sters: " + id);
                    } else {
                        System.out.println("Nu exista: " + id);
                    }
                } else if (comanda.equals("COUNT")) {
                    System.out.println("Total: " + repo.count());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
