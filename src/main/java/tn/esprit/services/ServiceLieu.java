package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Lieu;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceLieu implements IService<Lieu> {
    private Connection cnx;

    public ServiceLieu() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Lieu lieu) {
        String qry = "INSERT INTO `Lieu` (`nom`, `adresse`, `capacite`) VALUES (?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, lieu.getNom());
            pstm.setString(2, lieu.getAdresse());
            pstm.setInt(3, lieu.getCapacite());

            pstm.executeUpdate();

            // Retrieve the auto-generated ID
            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    lieu.setLieuId(generatedKeys.getInt(1)); // Update the ID in the Lieu object
                }
            }

            System.out.println("Lieu ajouté avec succès. ID : " + lieu.getLieuId());
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du lieu : " + e.getMessage());
        }
    }

    @Override
    public List<Lieu> getAll() {
        List<Lieu> lieux = new ArrayList<>();
        String qry = "SELECT * FROM `Lieu`";
        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Lieu l = new Lieu();
                l.setLieuId(rs.getInt("lieu_id"));
                l.setNom(rs.getString("nom"));
                l.setAdresse(rs.getString("adresse"));
                l.setCapacite(rs.getInt("capacite"));

                lieux.add(l);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des lieux : " + e.getMessage());
        }
        return lieux;
    }

    @Override
    public void update(Lieu lieu) {
        String qry = "UPDATE `Lieu` SET `nom` = ?, `adresse` = ?, `capacite` = ? WHERE `lieu_id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, lieu.getNom());
            pstm.setString(2, lieu.getAdresse());
            pstm.setInt(3, lieu.getCapacite());
            pstm.setInt(4, lieu.getLieuId());

            pstm.executeUpdate();
            System.out.println("Lieu mis à jour avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du lieu : " + e.getMessage());
        }
    }

    @Override
    public void delete(Lieu lieu) {
        String qry = "DELETE FROM `Lieu` WHERE `lieu_id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, lieu.getLieuId());
            pstm.executeUpdate();
            System.out.println("Lieu supprimé avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du lieu : " + e.getMessage());
        }
    }

    // Method to fetch Lieu by ID
    public Lieu getById(int id) {
        String qry = "SELECT * FROM `Lieu` WHERE `lieu_id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Lieu lieu = new Lieu();
                    lieu.setLieuId(rs.getInt("lieu_id"));
                    lieu.setNom(rs.getString("nom"));
                    lieu.setAdresse(rs.getString("adresse"));
                    lieu.setCapacite(rs.getInt("capacite"));
                    return lieu;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du lieu par ID : " + e.getMessage());
        }
        return null;  // Return null if not found
    }

    // Method to fetch Lieu by name and address
    public Lieu getByNameAndAddress(String nom, String adresse) {
        String qry = "SELECT * FROM `Lieu` WHERE `nom` = ? AND `adresse` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, nom);
            pstm.setString(2, adresse);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Lieu lieu = new Lieu();
                    lieu.setLieuId(rs.getInt("lieu_id"));
                    lieu.setNom(rs.getString("nom"));
                    lieu.setAdresse(rs.getString("adresse"));
                    lieu.setCapacite(rs.getInt("capacite"));
                    return lieu;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du lieu par nom et adresse : " + e.getMessage());
        }
        return null;  // Return null if not found
    }
}