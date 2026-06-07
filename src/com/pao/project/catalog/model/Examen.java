package com.pao.project.catalog.model;
public class Examen {
    private Materie materie;
    private Sala sala;
    public Examen(Materie materie, Sala sala) {
        this.materie = materie;
        this.sala = sala;
    }
    public Materie getMaterie() { return materie; }
    public Sala getSala() { return sala; }
}
