package tn.esprit.models;

import java.sql.Date;
import java.sql.Timestamp;

public class Contrat {
    private int contrat_id;
    private int service_id;
    private int offre_id;
    private Date date_debut;
    private Date date_fin;
    private String description;
    private Status statut;
    private String echeancier_paiement;
    private Timestamp cree_le;
    private Timestamp mis_a_jour_le;

    // Constructors
    public Contrat(){};
    public Contrat(int contrat_id, int service_id, int offre_id, Date date_debut, Date date_fin, String description, Status statut, String echeancier_paiement, Timestamp cree_le, Timestamp mis_a_jour_le) {
        this.contrat_id = contrat_id;
        this.service_id = service_id;
        this.offre_id = offre_id;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.description = description;
        this.statut = statut;
        this.echeancier_paiement = echeancier_paiement;
        this.cree_le = cree_le;
        this.mis_a_jour_le = mis_a_jour_le;
    }

    public Contrat(int service_id, int offre_id, Date date_debut, Date date_fin, String description, Status statut, String echeancier_paiement) {
        this.service_id = service_id;
        this.offre_id = offre_id;
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

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getOffre_id() {
        return offre_id;
    }

    public void setOffre_id(int offre_id) {
        this.offre_id = offre_id;
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
