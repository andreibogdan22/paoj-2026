package com.pao.project.catalog;
import com.pao.project.catalog.exception.EntitateNegasitaException;
import com.pao.project.catalog.exception.ValidareException;
import com.pao.project.catalog.model.InregistrareNota;
import com.pao.project.catalog.model.Materie;
import com.pao.project.catalog.model.Student;
import com.pao.project.catalog.service.AcademicService;
import com.pao.project.catalog.service.StudentService;
import java.util.Arrays;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        StudentService studentService = StudentService.getInstance();
        AcademicService academicService = AcademicService.getInstance();
        System.out.println("=== TESTARE ETAPA II: JDBC, Tranzactii, JOIN-uri, Audit ===");
        try {
            try { studentService.stergeStudent("101"); } catch(Exception ignored) {}
            try { studentService.stergeStudent("102"); } catch(Exception ignored) {}
            try { studentService.stergeStudent("103"); } catch(Exception ignored) {}
            try { academicService.stergeMaterie("PAO"); } catch(Exception ignored) {}
            try { academicService.stergeMaterie("BD"); } catch(Exception ignored) {}
            try { academicService.stergeMaterie("SD"); } catch(Exception ignored) {}
            studentService.adaugaStudent(new Student("Popescu", "Ion", "101"));
            studentService.adaugaStudent(new Student("Avram", "Mihai", "102"));
            studentService.adaugaStudent(new Student("Ionescu", "Ana", "103"));
            System.out.println("1. Studenti adaugati cu succes (in baza de date).");
            Student gasit = studentService.cautaStudent("102");
            System.out.println("2. Student gasit: " + gasit);
            studentService.stergeStudent("103");
            System.out.println("3. Studentul cu nr 103 a fost sters.");
            System.out.println("4. Listare:");
            studentService.listeazaTotiStudentii();
            academicService.adaugaMaterie(new Materie("PAO", "Programare Avansata", 6));
            academicService.adaugaMaterie(new Materie("BD", "Baze de Date", 5));
            academicService.adaugaMaterie(new Materie("SD", "Structuri Discrete", 4));
            System.out.println("\n5. Materii adaugate.");
            Materie pao = academicService.cautaMaterie("PAO");
            System.out.println("6. Materie gasita: " + pao);
            academicService.stergeMaterie("SD");
            System.out.println("7. Materia SD a fost stearsa.");
            System.out.println("8. Listare materii:");
            academicService.listeazaToateMateriile();
            InregistrareNota nota1 = new InregistrareNota("101", "PAO", 10);
            InregistrareNota nota2 = new InregistrareNota("101", "BD", 8);
            academicService.adaugaNota(nota1);
            academicService.adaugaNota(nota2);
            System.out.println("\n9. Note adaugate individual pentru studentul 101.");
            System.out.println("10. Afisare catalog (FOLOSESTE JOIN INTRE TABELE):");
            academicService.afiseazaCatalogStudent("101");
            System.out.println("\n--- Testare Interogari Suplimentare cu JOIN ---");
            academicService.afiseazaMediaStudentilor();
            academicService.afiseazaToateNoteleCuDetalii();
            System.out.println("\n--- Testare Tranzactie Explicita ---");
            List<InregistrareNota> noteTranzactie = Arrays.asList(
                new InregistrareNota("102", "PAO", 9),
                new InregistrareNota("102", "BD", 7)
            );
            academicService.transferaNoteInGrup(noteTranzactie);
            academicService.afiseazaCatalogStudent("102");
            System.out.println("\n--- Testare Excepții Custom ---");
            try {
                studentService.cautaStudent("999");
            } catch (EntitateNegasitaException e) {
                System.out.println("Excepție prinsă corect: " + e.getMessage());
            }
            try {
                academicService.adaugaNota(new InregistrareNota("102", "PAO", 15));
            } catch (ValidareException e) {
                System.out.println("Excepție prinsă corect: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\n[!] Toate actiunile au fost salvate si in audit.csv.");
    }
}
