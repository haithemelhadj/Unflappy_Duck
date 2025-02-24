package tn.esprit.models;

import tn.esprit.services.ServiceLieu;
import java.sql.Timestamp;

public class Evenement {
    private int evenementId;
    private String nom;
    private String description;
    private Timestamp dateDebut;
    private Timestamp dateFin;
    private int lieuId;
    private int calendrierId;
    private int createurEvenement;

    // Constructors
    public Evenement() {}

    public Evenement(int evenementId, String nom, String description, Timestamp dateDebut, Timestamp dateFin, int lieuId, int calendrierId, int createurEvenement) {
        this.evenementId = evenementId;
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieuId = lieuId;
        this.calendrierId = calendrierId;
        this.createurEvenement = createurEvenement;
    }

    // Getters and Setters
    public int getEvenementId() { return evenementId; }
    public void setEvenementId(int evenementId) { this.evenementId = evenementId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Timestamp getDateDebut() { return dateDebut; }
    public void setDateDebut(Timestamp dateDebut) { this.dateDebut = dateDebut; }
    public Timestamp getDateFin() { return dateFin; }
    public void setDateFin(Timestamp dateFin) { this.dateFin = dateFin; }
    public int getLieuId() { return lieuId; }
    public void setLieuId(int lieuId) { this.lieuId = lieuId; }
    public int getCalendrierId() { return calendrierId; }
    public void setCalendrierId(int calendrierId) { this.calendrierId = calendrierId; }
    public int getCreateurEvenement() { return createurEvenement; }
    public void setCreateurEvenement(int createurEvenement) { this.createurEvenement = createurEvenement; }

    @Override
    public String toString() {
        return "Evenement{" +
                "evenementId=" + evenementId +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", lieuId=" + lieuId +
                ", calendrierId=" + calendrierId +
                ", createurEvenement=" + createurEvenement +
                '}';
    }

    // Add a method to retrieve the Lieu object
    public Lieu getLieu() {
        ServiceLieu serviceLieu = new ServiceLieu();
        return serviceLieu.getById(this.lieuId);  // Fetch the Lieu using the ID
    }
}