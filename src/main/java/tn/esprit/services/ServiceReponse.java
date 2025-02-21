package tn.esprit.services;

import tn.esprit.models.Reponse;
import tn.esprit.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReponse {
    public int addReponse(Reponse reponse) {
        String sql = "INSERT INTO reponse (daterep, desrep, idrec) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, Date.valueOf(reponse.getDateReponse()));
            stmt.setString(2, reponse.getDescription());
            stmt.setInt(3, reponse.getIdReclamation());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Reponse> listReponses() {
        List<Reponse> responses = new ArrayList<>();
        String sql = "SELECT r.*, AVG(rv.stars) AS average_rating " +
                "FROM reponse r " +
                "LEFT JOIN review rv ON r.idrep = rv.id_response " +
                "GROUP BY r.idrep";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Reponse r = new Reponse(
                        rs.getInt("idrep"),
                        rs.getDate("daterep").toLocalDate(),
                        rs.getString("desrep"),
                        rs.getInt("idrec")
                );
                responses.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responses;
    }

    public boolean deleteReponse(int idrep) {
        String sql = "DELETE FROM reponse WHERE idrep = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idrep);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateReponse(Reponse reponse) {
        String sql = "UPDATE reponse SET daterep = ?, desrep = ?, idrec = ? WHERE idrep = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(reponse.getDateReponse()));
            stmt.setString(2, reponse.getDescription());
            stmt.setInt(3, reponse.getIdReclamation());
            stmt.setInt(4, reponse.getIdrep());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
