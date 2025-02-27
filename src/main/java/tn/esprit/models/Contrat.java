package tn.esprit.models;

import java.sql.Date;
import java.sql.Timestamp;

public class Contrat {
    private int contrat_id;
    private Service service;
    private Offre offre;
    private Date date_debut;
    private Date date_fin;
    private String description;
    private Status statut;
    private String echeancier_paiement;
    private Timestamp cree_le;
    private Timestamp mis_a_jour_le;

    // Constructors
    public Contrat(){};
    public Contrat(int contrat_id, Service service, Offre offre, Date date_debut, Date date_fin, String description, Status statut, String echeancier_paiement, Timestamp cree_le, Timestamp mis_a_jour_le) {
        this.contrat_id = contrat_id;
        this.service = service;
        this.offre = offre;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.description = description;
        this.statut = statut;
        this.echeancier_paiement = echeancier_paiement;
        this.cree_le = cree_le;
        this.mis_a_jour_le = mis_a_jour_le;
    }

    public Contrat(Service service, Offre offre, Date date_debut, Date date_fin, String description, Status statut, String echeancier_paiement) {
        this.service = service;
        this.offre = offre;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.description = description;
        this.statut = statut;
        this.echeancier_paiement = echeancier_paiement;
    }

    // Getters & Setters
    public int getContrat_id() {
        return contrat_id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Offre getOffre() {
        return offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }

    public Date getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatut() {
        return statut;
    }

    public void setStatut(Status statut) {
        this.statut = statut;
    }

    public String getEcheancier_paiement() {
        return echeancier_paiement;
    }

    public void setEcheancier_paiement(String echeancier_paiement) {
        this.echeancier_paiement = echeancier_paiement;
    }

    public Timestamp getCree_le() {
        return cree_le;
    }

    public Timestamp getMis_a_jour_le() {
        return mis_a_jour_le;
    }

    // Enum for status
    public enum Status {
        brouillon, actif, termine, resilie, dispute
    }
}
