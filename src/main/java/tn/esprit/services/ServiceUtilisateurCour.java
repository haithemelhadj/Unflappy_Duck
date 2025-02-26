package tn.esprit.services;

import tn.esprit.models.ReponseUtilisateur;
import tn.esprit.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUtilisateurCour {

    public boolean addReponseUtilisateur(ReponseUtilisateur response) {
        String sql = "INSERT INTO reponses (id_utilisateur, id_question, reponse, reponse_correcte) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, response.getIdUtilisateur());
            stmt.setInt(2, response.getIdQuestion());
            stmt.setString(3, response.getReponse());
            stmt.setString(4, response.getReponseCorrecte());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ReponseUtilisateur> getReponseUtilisateurs(int idUtilisateur) {
        List<ReponseUtilisateur> responses = new ArrayList<>();
        String sql = "SELECT * FROM reponses WHERE id_utilisateur = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUtilisateur);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ReponseUtilisateur ru = new ReponseUtilisateur(
                        rs.getInt("id"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("id_question"),
                        rs.getString("reponse"),
                        rs.getString("reponse_correcte")
                );
                responses.add(ru);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responses;
    }
}
