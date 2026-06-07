package com.pao.project.catalog.model;
public final class InregistrareNota {
    private final String numarMatricol;
    private final String codMaterie;
    private final int nota;
    public InregistrareNota(String numarMatricol, String codMaterie, int nota) {
        this.numarMatricol = numarMatricol;
        this.codMaterie = codMaterie;
        this.nota = nota;
    }
    public String getNumarMatricol() { return numarMatricol; }
    public String getCodMaterie() { return codMaterie; }
    public int getNota() { return nota; }
    @Override
    public String toString() {
        return "Nota: " + nota + " (Materie: " + codMaterie + ")";
    }
}
