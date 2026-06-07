package com.pao.project.catalog.service;
import com.pao.project.catalog.exception.EntitateNegasitaException;
import com.pao.project.catalog.exception.ValidareException;
import com.pao.project.catalog.model.InregistrareNota;
import com.pao.project.catalog.model.Materie;
import com.pao.project.catalog.repository.InregistrareNotaRepository;
import com.pao.project.catalog.repository.MaterieRepository;
import com.pao.project.catalog.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
public class AcademicService {
    private static AcademicService instance;
    private final MaterieRepository materieRepository;
    private final InregistrareNotaRepository notaRepository;
    private final AuditService auditService;
    private final Connection connection;
    private AcademicService() {
        this.materieRepository = new MaterieRepository();
        this.notaRepository = new InregistrareNotaRepository();
        this.auditService = AuditService.getInstance();
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    public static AcademicService getInstance() {
        if (instance == null) {
            instance = new AcademicService();
        }
        return instance;
    }
    public void adaugaMaterie(Materie materie) {
        auditService.logAction("adauga_materie");
        materieRepository.save(materie);
    }
    public Materie cautaMaterie(String cod) throws EntitateNegasitaException {
        auditService.logAction("cauta_materie");
        return materieRepository.findById(cod)
            .orElseThrow(() -> new EntitateNegasitaException("Materia cu codul " + cod + " nu există!"));
    }
    public void listeazaToateMateriile() {
        auditService.logAction("listeaza_materii");
        System.out.println("--- Lista Materii (Din BD) ---");
        for (Materie m : materieRepository.findAll()) {
            System.out.println(m);
        }
    }
    public void stergeMaterie(String cod) throws EntitateNegasitaException {
        auditService.logAction("sterge_materie");
        cautaMaterie(cod);
        materieRepository.delete(cod);
    }
    public void adaugaNota(InregistrareNota nota) throws ValidareException {
        auditService.logAction("adauga_nota");
        if (nota.getNota() < 1 || nota.getNota() > 10) {
            throw new ValidareException("Nota trebuie să fie între 1 și 10!");
        }
        notaRepository.save(nota);
    }
    public void afiseazaCatalogStudent(String numarMatricol) {
        auditService.logAction("afiseaza_catalog");
        System.out.println("--- Note pentru studentul: " + numarMatricol + " ---");
        String sql = "SELECT s.nume, s.prenume, m.denumire, n.nota " +
                     "FROM inregistrari_note n " +
                     "JOIN studenti s ON n.numar_matricol = s.numar_matricol " +
                     "JOIN materii m ON n.cod_materie = m.cod " +
                     "WHERE n.numar_matricol = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, numarMatricol);
            ResultSet rs = pstmt.executeQuery();
            boolean areNote = false;
            while (rs.next()) {
                areNote = true;
                System.out.println(rs.getString("denumire") + ": " + rs.getInt("nota"));
            }
            if (!areNote) {
                System.out.println("Studentul nu are nicio notă.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void afiseazaMediaStudentilor() {
        auditService.logAction("afiseaza_medii_studenti");
        String sql = "SELECT s.nume, s.prenume, AVG(n.nota) as medie " +
                     "FROM studenti s " +
                     "JOIN inregistrari_note n ON s.numar_matricol = n.numar_matricol " +
                     "GROUP BY s.numar_matricol";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("--- Medii Studenți ---");
            while (rs.next()) {
                System.out.printf("%s %s: %.2f\n", rs.getString("nume"), rs.getString("prenume"), rs.getDouble("medie"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void afiseazaToateNoteleCuDetalii() {
        auditService.logAction("afiseaza_toate_notele_detaliat");
        String sql = "SELECT s.numar_matricol, s.nume, s.prenume, m.denumire, n.nota " +
                     "FROM inregistrari_note n " +
                     "JOIN studenti s ON n.numar_matricol = s.numar_matricol " +
                     "JOIN materii m ON n.cod_materie = m.cod";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("--- Toate Notele ---");
            while (rs.next()) {
                System.out.printf("[%s] %s %s - %s: %d\n", 
                    rs.getString("numar_matricol"), rs.getString("nume"), rs.getString("prenume"),
                    rs.getString("denumire"), rs.getInt("nota"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void transferaNoteInGrup(List<InregistrareNota> note) throws SQLException {
        auditService.logAction("transfer_note_tranzactie");
        boolean autoCommitInitial = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            for (InregistrareNota nota : note) {
                String sql = "INSERT INTO inregistrari_note (numar_matricol, cod_materie, nota) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, nota.getNumarMatricol());
                    pstmt.setString(2, nota.getCodMaterie());
                    pstmt.setInt(3, nota.getNota());
                    pstmt.executeUpdate();
                }
            }
            connection.commit();
            System.out.println("Tranzactia a reusit! Au fost inserate " + note.size() + " note.");
        } catch (SQLException e) {
            connection.rollback();
            System.err.println("Tranzactia a eșuat. S-a făcut rollback.");
            throw e;
        } finally {
            connection.setAutoCommit(autoCommitInitial);
        }
    }
}
