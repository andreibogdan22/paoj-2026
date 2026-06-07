package com.pao.project.catalog.model;
public class Materie {
    private String cod;
    private String denumire;
    private int credite;
    public Materie(String cod, String denumire, int credite) {
        this.cod = cod;
        this.denumire = denumire;
        this.credite = credite;
    }
    public String getCod() { return cod; }
    public void setCod(String cod) { this.cod = cod; }
    public String getDenumire() { return denumire; }
    public void setDenumire(String denumire) { this.denumire = denumire; }
    public int getCredite() { return credite; }
    public void setCredite(int credite) { this.credite = credite; }
    @Override
    public String toString() {
        return "Materie{cod='" + cod + "', denumire='" + denumire + "', credite=" + credite + "}";
    }
}
