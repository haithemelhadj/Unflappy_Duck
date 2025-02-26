package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Evenement;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement implements IService<Evenement> {
    private Connection cnx;

    public ServiceEvenement() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Evenement evenement) {
        String qry = "INSERT INTO `Evenement` (`nom`, `description`, `date_debut`, `date_fin`, `lieu_id`, `calendrier_id`, `createur_evenement`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, evenement.getNom());
            pstm.setString(2, evenement.getDescription());
            pstm.setTimestamp(3, evenement.getDateDebut());
            pstm.setTimestamp(4, evenement.getDateFin());
            pstm.setInt(5, evenement.getLieuId());
            pstm.setInt(6, evenement.getCalendrierId());
            pstm.setInt(7, evenement.getCreateurEvenement());

            pstm.executeUpdate();

            // Retrieve the auto-generated ID
            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    evenement.setEvenementId(generatedKeys.getInt(1)); // Update the ID in the Evenement object
                }
            }

            System.out.println("Événement ajouté avec succès. ID : " + evenement.getEvenementId());
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'événement : " + e.getMessage());
        }
    }

    @Override
    public List<Evenement> getAll() {
        List<Evenement> evenements = new ArrayList<>();
        String qry = "SELECT * FROM `Evenement`";
        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Evenement e = new Evenement();
                e.setEvenementId(rs.getInt("evenement_id"));
                e.setNom(rs.getString("nom"));
                e.setDescription(rs.getString("description"));
                e.setDateDebut(rs.getTimestamp("date_debut"));
                e.setDateFin(rs.getTimestamp("date_fin"));
                e.setLieuId(rs.getInt("lieu_id"));
                e.setCalendrierId(rs.getInt("calendrier_id"));
                e.setCreateurEvenement(rs.getInt("createur_evenement"));

                evenements.add(e);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }
        return evenements;
    }

    public List<Evenement> searchEvents(String keyword) {
        List<Evenement> events = new ArrayList<>();
        String query = "SELECT * FROM Evenement WHERE nom LIKE ?"; // Only search by nom
        try (PreparedStatement pstm = cnx.prepareStatement(query)) {
            pstm.setString(1, "%" + keyword + "%"); // Search for events with names containing the keyword
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Evenement event = new Evenement();
                    event.setEvenementId(rs.getInt("evenement_id"));
                    event.setNom(rs.getString("nom"));
                    event.setDescription(rs.getString("description"));
                    event.setDateDebut(rs.getTimestamp("date_debut"));
                    event.setDateFin(rs.getTimestamp("date_fin"));
                    event.setLieuId(rs.getInt("lieu_id"));
                    event.setCalendrierId(rs.getInt("calendrier_id"));

                    events.add(event);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche d'événements par nom : " + e.getMessage());
        }
        return events;
    }

    public List<Evenement> sortEvents(String sortBy) {
        List<Evenement> events = new ArrayList<>();
        String query = "SELECT * FROM Evenement ORDER BY nom"; // Always sort by nom
        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(query)) {
            while (rs.next()) {
                Evenement event = new Evenement();
                event.setEvenementId(rs.getInt("evenement_id"));
                event.setNom(rs.getString("nom"));
                event.setDescription(rs.getString("description"));
                event.setDateDebut(rs.getTimestamp("date_debut"));
                event.setDateFin(rs.getTimestamp("date_fin"));
                event.setLieuId(rs.getInt("lieu_id"));
                event.setCalendrierId(rs.getInt("calendrier_id"));

                events.add(event);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du tri des événements : " + e.getMessage());
        }
        return events;
    }

    @Override
    public void update(Evenement evenement) {
        String qry = "UPDATE `Evenement` SET `nom` = ?, `description` = ?, `date_debut` = ?, `date_fin` = ?, `lieu_id` = ?, `calendrier_id` = ?, `createur_evenement` = ? WHERE `evenement_id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, evenement.getNom());
            pstm.setString(2, evenement.getDescription());
            pstm.setTimestamp(3, evenement.getDateDebut());
            pstm.setTimestamp(4, evenement.getDateFin());
            pstm.setInt(5, evenement.getLieuId());
            pstm.setInt(6, evenement.getCalendrierId());
            pstm.setInt(7, evenement.getCreateurEvenement());
            pstm.setInt(8, evenement.getEvenementId());

            pstm.executeUpdate();
            System.out.println("Événement mis à jour avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'événement : " + e.getMessage());
        }
    }

    @Override
    public void delete(Evenement evenement) {
        String qry = "DELETE FROM `Evenement` WHERE `evenement_id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, evenement.getEvenementId());
            pstm.executeUpdate();
            System.out.println("Événement supprimé avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'événement : " + e.getMessage());
        }
    }
    public void addEventToUser(int eventId) {
        String qry = "UPDATE Evenement SET is_added = TRUE WHERE evenement_id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, eventId);
            pstm.executeUpdate();
            System.out.println("Événement ajouté à l'utilisateur.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'événement à l'utilisateur : " + e.getMessage());
        }
    }

    public void removeEventFromUser(int eventId) {
        String qry = "UPDATE Evenement SET is_added = FALSE WHERE evenement_id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, eventId);
            pstm.executeUpdate();
            System.out.println("Événement supprimé de l'utilisateur.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'événement de l'utilisateur : " + e.getMessage());
        }
    }

    public List<Evenement> getEventsForUser() {
        List<Evenement> events = new ArrayList<>();
        String qry = "SELECT * FROM Evenement WHERE is_added = TRUE";
        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Evenement event = new Evenement();
                event.setEvenementId(rs.getInt("evenement_id"));
                event.setNom(rs.getString("nom"));
                event.setDescription(rs.getString("description"));
                event.setDateDebut(rs.getTimestamp("date_debut"));
                event.setDateFin(rs.getTimestamp("date_fin"));
                event.setLieuId(rs.getInt("lieu_id"));
                event.setCalendrierId(rs.getInt("calendrier_id"));
                event.setAdded(rs.getBoolean("is_added"));
                events.add(event);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des événements de l'utilisateur : " + e.getMessage());
        }
        return events;
    }
}