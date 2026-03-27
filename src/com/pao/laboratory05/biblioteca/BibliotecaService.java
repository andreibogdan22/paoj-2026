package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti = new Carte[0];

    private BibliotecaService() {}

    // Holder intern (pattern din Lab 01)
    private static class SingletonHolder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void addCarte(Carte carte) {
        Carte[] newCarti = new Carte[this.carti.length + 1];
        System.arraycopy(this.carti, 0, newCarti, 0, this.carti.length);
        newCarti[newCarti.length - 1] = carte;
        System.out.println("Carte adaugata: " + carte.getTitlu());
        this.carti = newCarti;
    }

    public void listSortedByRating() {
        Carte[] copy = this.carti.clone();
        Arrays.sort(copy);

        for (Carte c : copy) {
            System.out.println(c);
        }
    }

    public void listSortedBy(Comparator<Carte> comparator) {
        Carte[] copy = this.carti.clone();
        Arrays.sort(copy, comparator);

        for (Carte c : copy) {
            System.out.println(c);
        }
    }
}