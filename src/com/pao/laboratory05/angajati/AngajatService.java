package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati = new Angajat[0];

    private AngajatService() {}

    private static class SingletonHolder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void addAngajat(Angajat a) {
        Angajat[] newAngajati = new Angajat[this.angajati.length + 1];
        System.arraycopy(this.angajati, 0, newAngajati, 0, this.angajati.length);
        newAngajati[newAngajati.length - 1] = a;
        this.angajati = newAngajati;
        System.out.println("S-a adăugat angajatul: " + a.getNume());
    }

    public void printAll() {
        for (Angajat a : angajati) {
            System.out.println(a);
        }
    }

    public void listBySalary() {
        Angajat[] copy = this.angajati.clone();
        Arrays.sort(copy);
        for (Angajat a : copy) {
            System.out.println(a);
        }
    }

    public void findByDepartament(String numeDept) {
        boolean gasit = false;
        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(a);
                gasit = true;
            }
        }
        if (!gasit) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }
}