package com.pao.project.catalog.repository;
import com.pao.project.catalog.model.InregistrareNota;
import com.pao.project.catalog.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class InregistrareNotaRepository implements Repository<InregistrareNota, Integer> {
    private final Connection connection;
    public InregistrareNotaRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    @Override
    public void save(InregistrareNota entity) {
        String query = "INSERT INTO inregistrari_note (numar_matricol, cod_materie, nota) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, entity.getNumarMatricol());
            pstmt.setString(2, entity.getCodMaterie());
            pstmt.setInt(3, entity.getNota());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Optional<InregistrareNota> findById(Integer id) {
        String query = "SELECT * FROM inregistrari_note WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new InregistrareNota(rs.getString("numar_matricol"), rs.getString("cod_materie"), rs.getInt("nota")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public List<InregistrareNota> findAll() {
        List<InregistrareNota> list = new ArrayList<>();
        String query = "SELECT * FROM inregistrari_note";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new InregistrareNota(rs.getString("numar_matricol"), rs.getString("cod_materie"), rs.getInt("nota")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public void update(InregistrareNota entity) {
    }
    @Override
    public void delete(Integer id) {
        String query = "DELETE FROM inregistrari_note WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
