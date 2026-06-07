package com.pao.project.catalog.model;
public abstract class Persoana {
    protected String nume;
    protected String prenume;
    public Persoana(String nume, String prenume) {
        this.nume = nume;
        this.prenume = prenume;
    }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public String getPrenume() { return prenume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }
    public abstract String getRol();
}
