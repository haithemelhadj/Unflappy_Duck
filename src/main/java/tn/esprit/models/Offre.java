package tn.esprit.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;


public class Offre {
    private int offre_id;
    private Evenement event;
    private String titre;
    private String description;
    private BigDecimal budget;
    private TypeContrat type_contrat;
    private Status statut;
    private Timestamp cree_le;
    private Date expire_le;

    // Constructor
    public Offre(){};

    public Offre(int offre_id, Evenement event, String titre, String description, BigDecimal budget, TypeContrat type_contrat, Status statut, Timestamp cree_le, Date expire_le) {
        this.offre_id = offre_id;
        this.event = event;
        this.titre = titre;
        this.description = description;
        this.budget = budget;
        this.type_contrat = type_contrat;
        this.statut = statut;
        this.cree_le = cree_le;
        this.expire_le = expire_le;
    }

    public Offre(Evenement event, String titre, String description, BigDecimal budget, TypeContrat type_contrat, Status statut, Date expire_le) {
        this.event = event;
        this.titre = titre;
        this.description = description;
        this.budget = budget;
        this.type_contrat = type_contrat;
        this.statut = statut;
        this.expire_le = expire_le;
    }

    // Getters & Setters
    public int getOffre_id() {
        return offre_id;
    }

    public void setOffre_id(int offre_id) {
        this.offre_id = offre_id;
    }
    public Evenement getEvent() {
        return event;
    }

    public void setevent(Evenement event) {
        this.event = event;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public TypeContrat getType_contrat() {
        return type_contrat;
    }

    public void setType_contrat(TypeContrat type_contrat) {
        this.type_contrat = type_contrat;
    }

    public Status getStatut() {
        return statut;
    }

    public void setStatut(Status statut) {
        this.statut = statut;
    }

    public Timestamp getCree_le() {
        return cree_le;
    }

    public Date getExpire_le() {
        return expire_le;
    }

    public void setExpire_le(Date expire_le) {
        this.expire_le = expire_le;
    }

    // Enum for contract type
    public enum TypeContrat {
        forfait, horaire, abonnement
    }

    // Enum for status
    public enum Status {
        ouvert, ferme, en_cours, expire
    }

    @Override
    public String toString() {
        return "Offre{" +
                "offre_id=" + offre_id +
                ", event=" + event +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", budget=" + budget +
                ", type_contrat=" + type_contrat +
                ", statut=" + statut +
                ", cree_le=" + cree_le +
                ", expire_le=" + expire_le +
                '}';
    }
}

