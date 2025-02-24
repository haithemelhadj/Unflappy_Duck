package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Calendrier;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCalendrier implements IService<Calendrier> {
    private Connection cnx;

    public ServiceCalendrier() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Calendrier calendrier) {
        String qry = "INSERT INTO `Calendrier` (`annee`, `mois`, `jour`) VALUES (?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setInt(1, calendrier.getAnnee());
            pstm.setInt(2, calendrier.getMois());
            pstm.setInt(3, calendrier.getJour());

            pstm.executeUpdate();

            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    calendrier.setCalendrierId(generatedKeys.getInt(1));
                }
            }
            System.out.println("Calendrier ajouté avec succès. ID : " + calendrier.getCalendrierId());
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du calendrier : " + e.getMessage());
        }
    }

    public int addAndReturnId(Calendrier calendrier) {
        String qry = "INSERT INTO `Calendrier` (`annee`, `mois`, `jour`) VALUES (?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setInt(1, calendrier.getAnnee());
            pstm.setInt(2, calendrier.getMois());
            pstm.setInt(3, calendrier.getJour());
            pstm.executeUpdate();

            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du calendrier : " + e.getMessage());
        }
        return -1;
    }

    @Override
    public List<Calendrier> getAll() {
        List<Calendrier> calendriers = new ArrayList<>();
        String qry = "SELECT * FROM `Calendrier`";
        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Calendrier c = new Calendrier();
                c.setCalendrierId(rs.getInt("calendrier_id"));
                c.setAnnee(rs.getInt("annee"));
                c.setMois(rs.getInt("mois"));
                c.setJour(rs.getInt("jour"));
                calendriers.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des calendriers : " + e.getMessage());
        }
        return calendriers;
    }

    @Override
    public void update(Calendrier calendrier) {
        String qry = "UPDATE `Calendrier` SET `annee` = ?, `mois` = ?, `jour` = ? WHERE `calendrier_id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, calendrier.getAnnee());
            pstm.setInt(2, calendrier.getMois());
            pstm.setInt(3, calendrier.getJour());
            pstm.setInt(4, calendrier.getCalendrierId());
            pstm.executeUpdate();
            System.out.println("Calendrier mis à jour avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du calendrier : " + e.getMessage());
        }
    }

    @Override
    public void delete(Calendrier calendrier) {
        String qry = "DELETE FROM `Calendrier` WHERE `calendrier_id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, calendrier.getCalendrierId());
            pstm.executeUpdate();
            System.out.println("Calendrier supprimé avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du calendrier : " + e.getMessage());
        }
    }
}
