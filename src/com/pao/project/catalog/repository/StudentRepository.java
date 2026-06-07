package com.pao.project.catalog.repository;
import com.pao.project.catalog.model.Student;
import com.pao.project.catalog.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class StudentRepository implements Repository<Student, String> {
    private final Connection connection;
    public StudentRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    @Override
    public void save(Student entity) {
        String query = "INSERT INTO studenti (numar_matricol, nume, prenume) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, entity.getNumarMatricol());
            pstmt.setString(2, entity.getNume());
            pstmt.setString(3, entity.getPrenume());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Optional<Student> findById(String id) {
        String query = "SELECT * FROM studenti WHERE numar_matricol = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Student s = new Student(rs.getString("nume"), rs.getString("prenume"), rs.getString("numar_matricol"));
                return Optional.of(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        String query = "SELECT * FROM studenti";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Student(rs.getString("nume"), rs.getString("prenume"), rs.getString("numar_matricol")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public void update(Student entity) {
        String query = "UPDATE studenti SET nume = ?, prenume = ? WHERE numar_matricol = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, entity.getNume());
            pstmt.setString(2, entity.getPrenume());
            pstmt.setString(3, entity.getNumarMatricol());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void delete(String id) {
        String query = "DELETE FROM studenti WHERE numar_matricol = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
