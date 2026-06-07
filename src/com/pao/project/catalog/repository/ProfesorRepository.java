package com.pao.project.catalog.repository;
import com.pao.project.catalog.model.Profesor;
import com.pao.project.catalog.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class ProfesorRepository implements Repository<Profesor, Integer> {
    private final Connection connection;
    public ProfesorRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    @Override
    public void save(Profesor entity) {
        String query = "INSERT INTO profesori (nume, prenume, departament) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, entity.getNume());
            pstmt.setString(2, entity.getPrenume());
            pstmt.setString(3, entity.getDepartament());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Optional<Profesor> findById(Integer id) {
        String query = "SELECT * FROM profesori WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Profesor(rs.getString("nume"), rs.getString("prenume"), rs.getString("departament")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public List<Profesor> findAll() {
        List<Profesor> list = new ArrayList<>();
        String query = "SELECT * FROM profesori";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Profesor(rs.getString("nume"), rs.getString("prenume"), rs.getString("departament")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public void update(Profesor entity) {
        String query = "UPDATE profesori SET nume = ?, prenume = ?, departament = ? WHERE nume = ? AND prenume = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, entity.getNume());
            pstmt.setString(2, entity.getPrenume());
            pstmt.setString(3, entity.getDepartament());
            pstmt.setString(4, entity.getNume());
            pstmt.setString(5, entity.getPrenume());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void delete(Integer id) {
        String query = "DELETE FROM profesori WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
