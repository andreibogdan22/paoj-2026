package com.pao.project.catalog.model;
import java.util.Objects;
public class Profesor extends Persoana {
    private String departament;
    public Profesor(String nume, String prenume, String departament) {
        super(nume, prenume);
        this.departament = departament;
    }
    public String getDepartament() { return departament; }
    public void setDepartament(String departament) { this.departament = departament; }
    @Override
    public String getRol() { return "PROFESOR"; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profesor profesor = (Profesor) o;
        return Objects.equals(nume, profesor.nume) && Objects.equals(prenume, profesor.prenume) &&
               Objects.equals(departament, profesor.departament);
    }
    @Override
    public int hashCode() {
        return Objects.hash(nume, prenume, departament);
    }
    @Override
    public String toString() {
        return "Profesor{" + "nume='" + nume + '\'' + ", prenume='" + prenume + '\'' +
                ", departament='" + departament + '\'' + '}';
    }
}
