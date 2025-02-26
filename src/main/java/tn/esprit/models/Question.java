package tn.esprit.models;

import java.io.Serializable;

public class Question implements Serializable {
    private int qid;
    private int qno;
    private String question;
    private String reponse1;
    private String reponse2;
    private String reponse3;
    private String reponse4;
    private String reponseCorrecte;
    private int xp;

    public Question(int qid, int qno, String question, String reponse1, String reponse2,
                    String reponse3, String reponse4, String reponseCorrecte) {
        this.qid = qid;
        this.qno = qno;
        this.question = question;
        this.reponse1 = reponse1;
        this.reponse2 = reponse2;
        this.reponse3 = reponse3;
        this.reponse4 = reponse4;
        this.reponseCorrecte = reponseCorrecte;
        this.xp = xp;
    }

    public Question() {

    }


    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public int getQno() {
        return qno;
    }

    public void setQno(int qno) {
        this.qno = qno;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse1() {
        return reponse1;
    }

    public void setReponse1(String reponse1) {
        this.reponse1 = reponse1;
    }

    public String getReponse2() {
        return reponse2;
    }

    public void setReponse2(String reponse2) {
        this.reponse2 = reponse2;
    }

    public String getReponse3() {
        return reponse3;
    }

    public void setReponse3(String reponse3) {
        this.reponse3 = reponse3;
    }

    public String getReponse4() {
        return reponse4;
    }

    public void setReponse4(String reponse4) {
        this.reponse4 = reponse4;
    }

    public String getReponseCorrecte() {
        return reponseCorrecte;
    }

    public void setReponseCorrecte(String reponseCorrecte) {
        this.reponseCorrecte = reponseCorrecte;
    }
}
