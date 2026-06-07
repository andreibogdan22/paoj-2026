package com.pao.project.catalog.repository;
import com.pao.project.catalog.model.Materie;
import com.pao.project.catalog.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class MaterieRepository implements Repository<Materie, String> {
    private final Connection connection;
    public MaterieRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    @Override
    public void save(Materie entity) {
        String query = "INSERT INTO materii (cod, denumire, credite) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, entity.getCod());
            pstmt.setString(2, entity.getDenumire());
            pstmt.setInt(3, entity.getCredite());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Optional<Materie> findById(String id) {
        String query = "SELECT * FROM materii WHERE cod = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Materie(rs.getString("cod"), rs.getString("denumire"), rs.getInt("credite")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public List<Materie> findAll() {
        List<Materie> list = new ArrayList<>();
        String query = "SELECT * FROM materii";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Materie(rs.getString("cod"), rs.getString("denumire"), rs.getInt("credite")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public void update(Materie entity) {
        String query = "UPDATE materii SET denumire = ?, credite = ? WHERE cod = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, entity.getDenumire());
            pstmt.setInt(2, entity.getCredite());
            pstmt.setString(3, entity.getCod());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void delete(String id) {
        String query = "DELETE FROM materii WHERE cod = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
