package tn.esprit.models;

public class Utilisateur {
    private int utilisateurId;
    private String pseudo;
    private String email;
    private String motDePasseHache;
    private boolean estActif;
    private int roleId;

    // Constructeurs, getters et setters
    public Utilisateur() {}

    public Utilisateur(int utilisateurId, String pseudo, String email, String motDePasseHache, boolean estActif, int roleId) {
        this.utilisateurId = utilisateurId;
        this.pseudo = pseudo;
        this.email = email;
        this.motDePasseHache = motDePasseHache;
        this.estActif = estActif;
        this.roleId = roleId;
    }

    // Getters et Setters
    public int getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId = utilisateurId; }
    public String getPseudo() { return pseudo; }
    public void setPseudo(String pseudo) { this.pseudo = pseudo; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasseHache() { return motDePasseHache; }
    public void setMotDePasseHache(String motDePasseHache) { this.motDePasseHache = motDePasseHache; }
    public boolean isEstActif() { return estActif; }
    public void setEstActif(boolean estActif) { this.estActif = estActif; }
    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "utilisateurId=" + utilisateurId +
                ", pseudo='" + pseudo + '\'' +
                ", email='" + email + '\'' +
                ", motDePasseHache='" + motDePasseHache + '\'' +
                ", estActif=" + estActif +
                ", roleId=" + roleId +
                '}';
    }
}