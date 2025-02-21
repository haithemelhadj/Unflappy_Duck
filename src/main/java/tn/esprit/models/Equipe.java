package tn.esprit.models;

public class Equipe {

    //id	evenement_id	nom	chef_equipe_id
    int id;
    int evenement_id;
    String nom;
    int chef_equipe_id;

    //region constructors

    public Equipe() {
    }

    public Equipe(int id, int evenement_id, String nom, int chef_equipe_id) {
        this.id = id;
        this.evenement_id = evenement_id;
        this.nom = nom;
        this.chef_equipe_id = chef_equipe_id;
    }

    public Equipe(int evenement_id, String nom, int chef_equipe_id) {
        this.evenement_id = evenement_id;
        this.nom = nom;
        this.chef_equipe_id = chef_equipe_id;
    }
    //endregion

    //region getters & setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEvenement_id() {
        return evenement_id;
    }

    public void setEvenement_id(int evenement_id) {
        this.evenement_id = evenement_id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getChef_equipe_id() {
        return chef_equipe_id;
    }

    public void setChef_equipe_id(int chef_equipe_id) {
        this.chef_equipe_id = chef_equipe_id;
    }

    //endregion

    //region toString

    @Override
    public String toString() {
        return "Equipe{" +
                "id=" + id +
                ", evenement_id=" + evenement_id +
                ", nom='" + nom + '\'' +
                ", chef_equipe_id=" + chef_equipe_id +
                '}';
    }

    //endregion

}
