package tn.esprit.models;

import tn.esprit.models.enums.userRoles;

public class Utilisateur {
    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    private userRoles role;
    private String bio;
    private String photoProfil;
    private int xp;
    private Integer niveau; // Integer to allow null values
    private int xpRequis;
    private boolean statut;

    public Utilisateur() {}

    public Utilisateur(int id, String nom, String email, String motDePasse, userRoles role, String bio, String photoProfil, int xp, Integer niveau, int xpRequis, String token) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.bio = bio;
        this.photoProfil = photoProfil;
        this.xp = xp;
        this.niveau = niveau;
        this.xpRequis = xpRequis;
        this.statut = statut;
    }

    public Utilisateur(String nom, String email, String motDePasse, userRoles role, String bio, String photoProfil, int xp, Integer niveau, int xpRequis, String token) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.bio = bio;
        this.photoProfil = photoProfil;
        this.xp = xp;
        this.niveau = niveau;
        this.xpRequis = xpRequis;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public userRoles getRole() {
        return role;
    }

    public void setRole(userRoles role) {
        this.role = role;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public Integer getNiveau() {
        return niveau;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }

    public int getXpRequis() {
        return xpRequis;
    }

    public void setXpRequis(int xpRequis) {
        this.xpRequis = xpRequis;
    }

    public boolean getStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", role='" + role + '\'' +
                ", bio='" + bio + '\'' +
                ", photoProfil='" + photoProfil + '\'' +
                ", xp=" + xp +
                ", niveau=" + niveau +
                ", xpRequis=" + xpRequis +
                ", statut='" + statut + '\'' +
                '}';
    }


}
