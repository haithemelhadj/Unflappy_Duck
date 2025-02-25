package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Utilisateur;
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
        String qry = "INSERT INTO utilisateur (pseudo, email, mot_de_passe_hache, est_actif, role_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, utilisateur.getPseudo());
            pstm.setString(2, utilisateur.getEmail());
            pstm.setString(3, utilisateur.getMotDePasseHache());
            pstm.setBoolean(4, utilisateur.isEstActif());
            pstm.setInt(5, utilisateur.getRoleId());

            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        utilisateur.setUtilisateurId(generatedKeys.getInt(1));
                    }
                }
            }
            System.out.println("Utilisateur ajouté avec succès. ID : " + utilisateur.getUtilisateurId());
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
                Utilisateur u = new Utilisateur(
                        rs.getInt("utilisateur_id"),
                        rs.getString("pseudo"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe_hache"),
                        rs.getBoolean("est_actif"),
                        rs.getInt("role_id")
                );
                utilisateurs.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }

    @Override
    public void update(Utilisateur utilisateur) {
        String qry = "UPDATE utilisateur SET pseudo = ?, email = ?, mot_de_passe_hache = ?, est_actif = ?, role_id = ? WHERE utilisateur_id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, utilisateur.getPseudo());
            pstm.setString(2, utilisateur.getEmail());
            pstm.setString(3, utilisateur.getMotDePasseHache());
            pstm.setBoolean(4, utilisateur.isEstActif());
            pstm.setInt(5, utilisateur.getRoleId());
            pstm.setInt(6, utilisateur.getUtilisateurId());

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
        String qry = "DELETE FROM utilisateur WHERE utilisateur_id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, utilisateur.getUtilisateurId());
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
        String qry = "SELECT * FROM utilisateur WHERE email = ? AND mot_de_passe_hache = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, email);
            pstm.setString(2, motDePasse);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new Utilisateur(
                            rs.getInt("utilisateur_id"),
                            rs.getString("pseudo"),
                            rs.getString("email"),
                            rs.getString("mot_de_passe_hache"),
                            rs.getBoolean("est_actif"),
                            rs.getInt("role_id")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification : " + e.getMessage());
        }
        return null;
    }
}
