package tn.esprit.models;

//import tn.esprit.models.enums.Statut;


public class Tache {

    //id	equipe_id	titre	description	id_responsable	statut
    private int id;
    private int equipe_id;
    private String titre;
    private String description;
    private int id_responsable;
    private boolean statut;

    //region constructors

    public Tache() {
    }

    public Tache(int id, int equipe_id, String titre, String description, int id_responsable, boolean statut) {
        //System.out.println("const w id....adding tache....");
        this.id = id;
        this.equipe_id = equipe_id;
        this.titre = titre;
        this.description = description;
        //this.id_assignateur = id_assignateur;, int id_assignateur
        this.id_responsable = id_responsable;
        this.statut = statut;
    }

    public Tache(int equipe_id, String titre, String description, int id_responsable, boolean statut) {
        //System.out.println("no id const....adding tache....");
        this.equipe_id = equipe_id;
        this.titre = titre;
        this.description = description;
        //this.id_assignateur = id_assignateur;, int id_assignateur
        this.id_responsable = id_responsable;
        this.statut = statut;
    }

    public Tache(int equipe_id, String titre, String description, int id_responsable) {
        //System.out.println("no id,status const....adding tache....");
        this.equipe_id = equipe_id;
        this.titre = titre;
        this.description = description;
        this.id_responsable = id_responsable;
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
/*
    public int getId_assignateur() {
        return id_assignateur;
    }

    public void setId_assignateur(int id_assignateur) {
        this.id_assignateur = id_assignateur;
    }
/**/
    public int getId_responsable() {
        return id_responsable;
    }

    public void setId_responsable(int id_responsable) {
        this.id_responsable = id_responsable;
    }

    public boolean getStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }
/**/
    //endregion



    //region toString
    @Override
    public String toString() {
        return "Tache{" +
                "id=" + id +
                ", equipe_id=" + equipe_id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", id_responsable=" + id_responsable +
                ", statut=" + statut +
                '}';
    }

    //endregion

    //region from string
    public static Tache fromString(String input) {
        // Split the input string by commas (assuming CSV format)
        String[] parts = input.split(",");

        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid input format. Expected 6 values.");
        }

        // Parse the values
        int id = Integer.parseInt(parts[0].trim());
        int equipe_id = Integer.parseInt(parts[1].trim());
        String titre = parts[2].trim();
        String description = parts[3].trim();
        int id_responsable = Integer.parseInt(parts[4].trim());

        // Assuming Statut is an enum or a class that can be constructed from a string
        //Statut statut = Statut.valueOf(parts[5].trim().toUpperCase());
        String statut = parts[5].trim().toUpperCase();

        // Create and return the Tache object
        Tache tache = new Tache();
        tache.setId(id);
        tache.setEquipe_id(equipe_id);
        tache.setTitre(titre);
        tache.setDescription(description);
        tache.setId_responsable(id_responsable);
        tache.setStatut("TERMINE".equalsIgnoreCase(statut));

        return tache;
    }
    //endregion

    //region

    //endregion


}
