package tn.esprit.models;

import java.time.LocalDate;

public class Reponse {
    private int idrep;
    private LocalDate dateReponse;
    private String description;
    private int idReclamation;

    public Reponse(int id, LocalDate dateReponse, String description, int idReclamation) {
        this.idrep = id;
        this.dateReponse = dateReponse;
        this.description = description;
        this.idReclamation = idReclamation;
    }


    public int getIdrep() { return idrep; }
    public void setIdrep(int idrep) { this.idrep = idrep; }

    public LocalDate getDateReponse() { return dateReponse; }
    public void setDateReponse(LocalDate dateReponse) { this.dateReponse = dateReponse; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getIdReclamation() { return idReclamation; }
    public void setIdReclamation(int idReclamation) { this.idReclamation = idReclamation; }
}
