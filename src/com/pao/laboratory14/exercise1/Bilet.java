package com.pao.laboratory14.exercise1;

public class Bilet {
    private int id;
    private String eveniment;
    private TipBilet tip;
    private double pret;

    public Bilet(int id, String eveniment, TipBilet tip, double pret) {
        this.id = id;
        this.eveniment = eveniment;
        this.tip = tip;
        this.pret = pret;
    }

    public int getId() { return id; }
    public String getEveniment() { return eveniment; }
    public TipBilet getTip() { return tip; }
    public double getPret() { return pret; }
}
