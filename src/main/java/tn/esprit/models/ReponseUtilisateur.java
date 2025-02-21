package tn.esprit.models;

public class ReponseUtilisateur {
    private int id;
    private int idUtilisateur;
    private int idQuestion;
    private String reponse;
    private String reponseCorrecte;

    public ReponseUtilisateur(int id, int idUtilisateur, int idQuestion, String reponse, String reponseCorrecte) {
        this.id = id;
        this.idUtilisateur = idUtilisateur;
        this.idQuestion = idQuestion;
        this.reponse = reponse;
        this.reponseCorrecte = reponseCorrecte;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }
    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdQuestion() {
        return idQuestion;
    }
    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getReponse() {
        return reponse;
    }
    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getReponseCorrecte() {
        return reponseCorrecte;
    }
    public void setReponseCorrecte(String reponseCorrecte) {
        this.reponseCorrecte = reponseCorrecte;
    }
}
