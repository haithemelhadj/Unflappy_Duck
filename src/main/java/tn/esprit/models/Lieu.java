package tn.esprit.models;

public class Lieu {
    private int lieuId;
    private String nom;
    private String adresse;
    private int capacite;

    // Constructeurs
    public Lieu() {}

    public Lieu(int lieuId, String nom, String adresse, int capacite) {
        this.lieuId = lieuId;
        this.nom = nom;
        this.adresse = adresse;
        this.capacite = capacite;
    }

    // Getters et Setters
    public int getLieuId() { return lieuId; }
    public void setLieuId(int lieuId) { this.lieuId = lieuId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    @Override
    public String toString() {
        return "Lieu{" +
                "lieuId=" + lieuId +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", capacite=" + capacite +
                '}';
    }

}