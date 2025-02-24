package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Utilisateur;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUtilisateur implements IService<Utilisateur> {
    private Connection cnx;

    public ServiceUtilisateur() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Utilisateur utilisateur) {
        String qry = "INSERT INTO `Utilisateur` (`pseudo`, `email`, `mot_de_passe_hache`, `est_actif`, `role_id`) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, utilisateur.getPseudo());
            pstm.setString(2, utilisateur.getEmail());
            pstm.setString(3, utilisateur.getMotDePasseHache());
            pstm.setBoolean(4, utilisateur.isEstActif());
            pstm.setInt(5, utilisateur.getRoleId());

            pstm.executeUpdate();

            // Récupérer l'ID généré automatiquement
            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    utilisateur.setUtilisateurId(generatedKeys.getInt(1)); // Mettre à jour l'ID dans l'objet Utilisateur
                }
            }

            System.out.println("Utilisateur ajouté avec succès. ID : " + utilisateur.getUtilisateurId());
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public List<Utilisateur> getAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String qry = "SELECT * FROM `Utilisateur`";
        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setUtilisateurId(rs.getInt("utilisateur_id"));
                u.setPseudo(rs.getString("pseudo"));
                u.setEmail(rs.getString("email"));
                u.setMotDePasseHache(rs.getString("mot_de_passe_hache"));
                u.setEstActif(rs.getBoolean("est_actif"));
                u.setRoleId(rs.getInt("role_id"));

                utilisateurs.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }

    @Override
    public void update(Utilisateur utilisateur) {
        String qry = "UPDATE `Utilisateur` SET `pseudo` = ?, `email` = ?, `mot_de_passe_hache` = ?, `est_actif` = ?, `role_id` = ? WHERE `utilisateur_id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, utilisateur.getPseudo());
            pstm.setString(2, utilisateur.getEmail());
            pstm.setString(3, utilisateur.getMotDePasseHache());
            pstm.setBoolean(4, utilisateur.isEstActif());
            pstm.setInt(5, utilisateur.getRoleId());
            pstm.setInt(6, utilisateur.getUtilisateurId());

            pstm.executeUpdate();
            System.out.println("Utilisateur mis à jour avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public void delete(Utilisateur utilisateur) {
        String qry = "DELETE FROM `Utilisateur` WHERE `utilisateur_id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, utilisateur.getUtilisateurId());
            pstm.executeUpdate();
            System.out.println("Utilisateur supprimé avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }
}