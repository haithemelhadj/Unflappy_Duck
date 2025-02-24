package tn.esprit.models;

public class Panier {
    private int id;
    private int utilisateur_id;
    private int article_id;
    private int quantite;


    public Panier() {
    }

    public Panier(int id, int utilisateur_id, int article_id, int quantite) {
        this.id = id;
        this.utilisateur_id = utilisateur_id;
        this.article_id = article_id;
        this.quantite = quantite;
    }

    public Panier(int utilisateur_id, int article_id, int quantite) {
        this.utilisateur_id = utilisateur_id;
        this.article_id = article_id;
        this.quantite = quantite;
    }

    public int getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setUtilisateur_id(int utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Panier{" +
                "id=" + id +
                ", utilisateur_id=" + utilisateur_id +
                ", article_id=" + article_id +
                ", quantite=" + quantite +
                '}';
    }
}
