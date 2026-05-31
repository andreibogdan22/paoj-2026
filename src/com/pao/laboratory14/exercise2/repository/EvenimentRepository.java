package com.pao.laboratory14.exercise2.repository;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EvenimentRepository implements Repository<Eveniment, Integer> {

    public EvenimentRepository() throws SQLException, java.io.IOException {
        initSchema();
    }

    private void initSchema() throws SQLException, java.io.IOException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS evenimente");
            stmt.execute("CREATE TABLE IF NOT EXISTS evenimente (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nume TEXT NOT NULL, " +
                    "data TEXT NOT NULL, " +
                    "capacitate INTEGER, " +
                    "tip TEXT)");
        }
    }

    @Override
    public void save(Eveniment entity) throws SQLException {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "INSERT INTO evenimente (nume, data, capacitate, tip) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getNume());
                ps.setString(2, entity.getData());
                ps.setInt(3, entity.getCapacitate());
                ps.setString(4, entity.getTip().name());
                ps.executeUpdate();
                
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getInt(1));
                    }
                }
            }
        } catch (java.io.IOException e) {
            throw new SQLException("Failed to get DB connection", e);
        }
    }

    @Override
    public Optional<Eveniment> findById(Integer id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<Eveniment> findAll() throws SQLException {
        List<Eveniment> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM evenimente ORDER BY id";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Eveniment e = new Eveniment(
                            rs.getInt("id"),
                            rs.getString("nume"),
                            rs.getString("data"),
                            rs.getInt("capacitate"),
                            TipBilet.valueOf(rs.getString("tip"))
                    );
                    list.add(e);
                }
            }
        } catch (java.io.IOException e) {
            throw new SQLException("Failed to get DB connection", e);
        }
        return list;
    }

    @Override
    public void update(Eveniment entity) throws SQLException {
    }

    @Override
    public void delete(Integer id) throws SQLException {
        deleteImpl(id);
    }
    
    public int deleteImpl(int id) throws SQLException {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "DELETE FROM evenimente WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate();
            }
        } catch (java.io.IOException e) {
            throw new SQLException("Failed to get DB connection", e);
        }
    }

    public int count() throws SQLException {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT COUNT(*) FROM evenimente";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (java.io.IOException e) {
            throw new SQLException("Failed to get DB connection", e);
        }
        return 0;
    }
}
