package com.pao.laboratory14.exercise1;

import java.util.*;
import java.util.stream.Collector;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();
        scanner.nextLine();

        List<Bilet> bilete = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!scanner.hasNextLine()) break;
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) { i--; continue; }
            String[] tokens = line.split("\\s+");
            int id = Integer.parseInt(tokens[0]);
            String eveniment = tokens[1];
            TipBilet tip = TipBilet.valueOf(tokens[2]);
            double pret = Double.parseDouble(tokens[3]);
            bilete.add(new Bilet(id, eveniment, tip, pret));
        }

        String comanda = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        
        Collector<Bilet, Map<TipBilet, double[]>, RaportVanzari> raportCollector = Collector.of(
                () -> new EnumMap<>(TipBilet.class),
                (map, bilet) -> {
                    double[] stats = map.computeIfAbsent(bilet.getTip(), k -> new double[2]);
                    stats[0]++;
                    stats[1] += bilet.getPret();
                },
                (map1, map2) -> {
                    map2.forEach((k, v) -> {
                        double[] stats = map1.computeIfAbsent(k, key -> new double[2]);
                        stats[0] += v[0];
                        stats[1] += v[1];
                    });
                    return map1;
                },
                map -> {
                    Map<TipBilet, Long> numarPerTip = new EnumMap<>(TipBilet.class);
                    Map<TipBilet, Double> incasariPerTip = new EnumMap<>(TipBilet.class);
                    double totalGlobal = 0;
                    long totalCount = 0;
                    TipBilet tipCelMaiPopular = null;
                    long maxCount = -1;

                    for (TipBilet tip : TipBilet.values()) {
                        if (map.containsKey(tip)) {
                            double[] stats = map.get(tip);
                            long count = (long) stats[0];
                            double incasari = stats[1];
                            numarPerTip.put(tip, count);
                            incasariPerTip.put(tip, incasari);
                            totalGlobal += incasari;
                            totalCount += count;
                            
                            if (count > maxCount || (count == maxCount && tipCelMaiPopular != null && tip.name().compareTo(tipCelMaiPopular.name()) < 0)) {
                                maxCount = count;
                                tipCelMaiPopular = tip;
                            } else if (count == maxCount && tipCelMaiPopular == null) {
                                tipCelMaiPopular = tip;
                            }
                        }
                    }
                    
                    double medieGlobala = totalCount == 0 ? 0 : totalGlobal / totalCount;
                    return new RaportVanzari(numarPerTip, incasariPerTip, totalGlobal, medieGlobala, tipCelMaiPopular);
                }
        );

        RaportVanzari raport = bilete.stream().collect(raportCollector);

        for (TipBilet tip : TipBilet.values()) {
            if (raport.getNumarPerTip().containsKey(tip)) {
                System.out.printf(Locale.US, "%s: count=%d incasari=%.2f RON\n", 
                        tip.name(), 
                        raport.getNumarPerTip().get(tip), 
                        raport.getIncasariPerTip().get(tip));
            }
        }

        if ("RAPORT_COMPLET".equals(comanda)) {
            System.out.println("---");
            System.out.printf(Locale.US, "Total: %.2f RON\n", raport.getTotalGlobal());
            System.out.printf(Locale.US, "Medie: %.2f RON\n", raport.getMedieGlobala());
            System.out.println("Cel mai popular: " + (raport.getTipCelMaiPopular() != null ? raport.getTipCelMaiPopular().name() : "N/A"));
        }
    }
}
