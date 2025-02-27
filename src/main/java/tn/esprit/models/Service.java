package tn.esprit.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

public class Service {
    private int service_id;
    private Utilisateur freelancer;
    private String titre;
    private String description;
    private String expertise;
    private int duree_jours;
    private BigDecimal prix;
    private MethodePaiement mode_paiement;
    private Timestamp cree_le;
    private Timestamp mis_a_jour_le;

    // Constructor
    public Service(){};

    public Service(Utilisateur freelancer, String titre, String description, String expertise, int duree_jours, BigDecimal prix, MethodePaiement mode_paiement) {
        this.mode_paiement = mode_paiement;
        this.prix = prix;
        this.duree_jours = duree_jours;
        this.expertise = expertise;
        this.description = description;
        this.titre = titre;
        this.freelancer = freelancer;
    }

    public Service(int service_id, Utilisateur freelancer, String titre, String description, String expertise, int duree_jours, BigDecimal prix, MethodePaiement mode_paiement, Timestamp cree_le, Timestamp mis_a_jour_le) {
        this.service_id = service_id;
        this.freelancer = freelancer;
        this.titre = titre;
        this.description = description;
        this.expertise = expertise;
        this.duree_jours = duree_jours;
        this.prix = prix;
        this.mode_paiement = mode_paiement;
        this.cree_le = cree_le;
        this.mis_a_jour_le = mis_a_jour_le;
    }

    // Getters & Setters
    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public Timestamp getMis_a_jour_le() {
        return mis_a_jour_le;
    }

    public void setMis_a_jour_le(Timestamp mis_a_jour_le) {
        this.mis_a_jour_le = mis_a_jour_le;
    }

    public Timestamp getCree_le() {
        return cree_le;
    }

    public void setCree_le(Timestamp cree_le) {
        this.cree_le = cree_le;
    }

    public MethodePaiement getMode_paiement() {
        return mode_paiement;
    }

    public void setMode_paiement(MethodePaiement mode_paiement) {
        this.mode_paiement = mode_paiement;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public int getDuree_jours() {
        return duree_jours;
    }

    public void setDuree_jours(int duree_jours) {
        this.duree_jours = duree_jours;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Utilisateur getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(Utilisateur freelancer) {
        this.freelancer = freelancer;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return getService_id() == service.getService_id() && getFreelancer() == service.getFreelancer() && getDuree_jours() == service.getDuree_jours() && Objects.equals(getTitre(), service.getTitre()) && Objects.equals(getDescription(), service.getDescription()) && Objects.equals(getExpertise(), service.getExpertise()) && Objects.equals(getPrix(), service.getPrix()) && getMode_paiement() == service.getMode_paiement() && Objects.equals(getCree_le(), service.getCree_le()) && Objects.equals(getMis_a_jour_le(), service.getMis_a_jour_le());
    }

    @Override
    public String toString() {
        return "Service{" +
                "service_id=" + service_id +
                ", freelancer=" + freelancer +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", expertise='" + expertise + '\'' +
                ", duree_jours=" + duree_jours +
                ", prix=" + prix +
                ", mode_paiement=" + mode_paiement +
                ", cree_le=" + cree_le +
                ", mis_a_jour_le=" + mis_a_jour_le +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getService_id(), getFreelancer(), getTitre(), getDescription(), getExpertise(), getDuree_jours(), getPrix(), getMode_paiement(), getCree_le(), getMis_a_jour_le());
    }

    // Enum for payment method
    public enum MethodePaiement {
        horaire, forfait, milestone
    }
}