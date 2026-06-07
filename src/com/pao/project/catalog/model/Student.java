package com.pao.project.catalog.model;
import java.util.Objects;
public class Student extends Persoana implements Comparable<Student> {
    private String numarMatricol;
    public Student(String nume, String prenume, String numarMatricol) {
        super(nume, prenume);
        this.numarMatricol = numarMatricol;
    }
    public String getNumarMatricol() { return numarMatricol; }
    public void setNumarMatricol(String numarMatricol) { this.numarMatricol = numarMatricol; }
    @Override
    public String getRol() { return "STUDENT"; }
    @Override
    public int compareTo(Student altul) {
        int rez = this.nume.compareTo(altul.nume);
        return (rez == 0) ? this.prenume.compareTo(altul.prenume) : rez;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(numarMatricol, student.numarMatricol);
    }
    @Override
    public int hashCode() {
        return Objects.hash(numarMatricol);
    }
    @Override
    public String toString() {
        return "Student{" + "nume='" + nume + '\'' + ", prenume='" + prenume + '\'' +
                ", numarMatricol='" + numarMatricol + '\'' + '}';
    }
}
