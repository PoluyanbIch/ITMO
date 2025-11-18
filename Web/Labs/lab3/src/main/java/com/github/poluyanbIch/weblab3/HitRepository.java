package com.github.poluyanbIch.weblab3;

import com.github.poluyanbIch.weblab3.model.Hit;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="hitRepository")
@ApplicationScoped
public class HitRepository implements Serializable {

    private final String url = "url";
    private final String username = "user";
    private final String password = "password";

    public HitRepository() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            throw new RuntimeException("Cannot load PostgreSQL driver", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void save(Hit hit) {
        String sql = "INSERT INTO hits (x, y, r, is_hit, execution_time, current_datetime) VALUES (?,?,?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, hit.getX());
            ps.setBigDecimal(2, hit.getY());
            ps.setBigDecimal(3, hit.getR());
            ps.setBoolean(4, hit.isHit());
            ps.setDouble(5, hit.getExecutionTime());
            ps.setTimestamp(6, new Timestamp(hit.getCurrentDatetime().getTime()));

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Hit> getAll() {
        String sql = "SELECT id, x, y, r, is_hit, execution_time, current_datetime FROM hits";
        List<Hit> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Hit h = new Hit();
                h.setId(rs.getLong("id"));
                h.setX(rs.getBigDecimal("x"));
                h.setY(rs.getBigDecimal("y"));
                h.setR(rs.getBigDecimal("r"));
                h.setHit(rs.getBoolean("is_hit"));
                h.setExecutionTime(rs.getDouble("execution_time"));
                h.setCurrentDatetime(rs.getTimestamp("current_datetime"));

                list.add(h);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public void clear() {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM hits")) {

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
