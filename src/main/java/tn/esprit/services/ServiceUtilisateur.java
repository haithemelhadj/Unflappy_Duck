package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Utilisateur;
import tn.esprit.models.enums.userRoles;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUtilisateur implements IService<Utilisateur> {
    private final Connection cnx;

    public ServiceUtilisateur() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Utilisateur utilisateur) {
        String qry = "INSERT INTO utilisateur (nom, email, motDePasse, role, bio, photoProfil, xp, niveau, xpRequis) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, utilisateur.getNom());
            pstm.setString(2, utilisateur.getEmail());
            pstm.setString(3, utilisateur.getMotDePasse()); // Ensure this is hashed before calling
            pstm.setString(4, utilisateur.getRole().name()); // Store enum name
            pstm.setString(5, utilisateur.getBio());
            pstm.setString(6, utilisateur.getPhotoProfil());
            pstm.setInt(7, utilisateur.getXp());
            pstm.setObject(8, utilisateur.getNiveau(), Types.INTEGER); // Handle null values
            pstm.setInt(9, utilisateur.getXpRequis());

            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        utilisateur.setId(generatedKeys.getInt(1));
                    }
                }
            }
            System.out.println("Utilisateur ajouté avec succès. ID : " + utilisateur.getId());
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public List<Utilisateur> getAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String qry = "SELECT * FROM utilisateur";
        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Utilisateur u = mapResultSetToUtilisateur(rs);
                utilisateurs.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }

    @Override
    public void update(Utilisateur utilisateur) {
        String qry = "UPDATE utilisateur SET nom = ?, email = ?, motDePasse = ?, role = ?, bio = ?, photoProfil = ?, xp = ?, niveau = ?, xpRequis = ? WHERE id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, utilisateur.getNom());
            pstm.setString(2, utilisateur.getEmail());
            pstm.setString(3, utilisateur.getMotDePasse()); // Ensure this is hashed before calling
            pstm.setString(4, utilisateur.getRole().name()); // Store enum name
            pstm.setString(5, utilisateur.getBio());
            pstm.setString(6, utilisateur.getPhotoProfil());
            pstm.setInt(7, utilisateur.getXp());
            pstm.setObject(8, utilisateur.getNiveau(), Types.INTEGER); // Handle null values
            pstm.setInt(9, utilisateur.getXpRequis());
            pstm.setInt(10, utilisateur.getId());

            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Utilisateur mis à jour avec succès.");
            } else {
                System.err.println("Aucun utilisateur mis à jour. ID non trouvé.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public void delete(Utilisateur utilisateur) {
        String qry = "DELETE FROM utilisateur WHERE id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, utilisateur.getId());
            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Utilisateur supprimé avec succès.");
            } else {
                System.err.println("Aucun utilisateur supprimé. ID non trouvé.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

    public Utilisateur loginUtilisateur(String email, String motDePasse) {
        String qry = "SELECT * FROM utilisateur WHERE email = ? AND motDePasse = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, email);
            pstm.setString(2, motDePasse); // Ensure this is hashed before calling
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification : " + e.getMessage());
        }
        return null;
    }

    // Helper method to map ResultSet to Utilisateur
    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        return new Utilisateur(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("email"),
                rs.getString("motDePasse"),
                userRoles.valueOf(rs.getString("role")), // Convert string back to enum
                rs.getString("bio"),
                rs.getString("photoProfil"),
                rs.getInt("xp"),
                rs.getObject("niveau", Integer.class), // Handle null values
                rs.getInt("xpRequis")
        );
    }
}