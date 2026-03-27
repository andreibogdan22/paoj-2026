package com.pao.laboratory05.angajati;

import java.util.Scanner;
/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println();
            System.out.println("Gestionare Angajați");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            if (optiune == 0) {
                break;
            } else if (optiune == 1) {
                System.out.print("Nume: ");
                String nume = scanner.nextLine();

                System.out.print("Departament (Nume): ");
                String numeDept = scanner.nextLine();

                System.out.print("Departament (Locație): ");
                String locatieDept = scanner.nextLine();

                System.out.print("Salariu: ");
                double salariu = scanner.nextDouble();
                scanner.nextLine();

                Departament dept = new Departament(numeDept, locatieDept);
                Angajat angajat = new Angajat(nume, dept, salariu);
                service.addAngajat(angajat);

            } else if (optiune == 2) {
                service.listBySalary();
            } else if (optiune == 3) {
                System.out.print("Nume departament căutat: ");
                String numeDept = scanner.nextLine();
                service.findByDepartament(numeDept);
            } else {
                System.out.println("Opțiune invalidă.");
            }
        }
        scanner.close();
    }
}
