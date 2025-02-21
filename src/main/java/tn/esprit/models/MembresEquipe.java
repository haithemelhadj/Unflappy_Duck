package tn.esprit.models;

import tn.esprit.models.enums.roles;

;
public class MembresEquipe {

    //id	equipe_id	utilisateur_id	role
    private int id;
    private int equipe_id;
    private int utilisateur_id;
    private roles role;

    //region constructors

    public MembresEquipe() {
    }

    public MembresEquipe(int id, int equipe_id, int utilisateur_id, roles role) {
        this.id = id;
        this.equipe_id = equipe_id;
        this.utilisateur_id = utilisateur_id;
        this.role = role;
    }

    public MembresEquipe(int equipe_id, int utilisateur_id, roles role) {
        this.equipe_id = equipe_id;
        this.utilisateur_id = utilisateur_id;
        this.role = role;
    }
    //endregion

    //region getters & setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEquipe_id() {
        return equipe_id;
    }

    public void setEquipe_id(int equipe_id) {
        this.equipe_id = equipe_id;
    }

    public int getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setUtilisateur_id(int utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }

    public roles getRole() {
        return role;
    }

    public void setRole(roles role) {
        this.role = role;
    }

    //endregion

    //region toString

    @Override
    public String toString() {
        return "MembresEquipe{" +
                "id=" + id +
                ", equipe_id=" + equipe_id +
                ", utilisateur_id=" + utilisateur_id +
                ", role=" + role +
                '}';
    }

    //endregion

    //region
    //endregion


}
