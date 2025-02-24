package tn.esprit.models;

public class Calendrier {
    private int calendrierId;
    private int annee;
    private int mois;
    private int jour;

    // Constructeurs
    public Calendrier() {}

    public Calendrier(int annee, int mois, int jour) {
        this.annee = annee;
        this.mois = mois;
        this.jour = jour;
    }


    public Calendrier(int calendrierId, int annee, int mois, int jour) {
        this.calendrierId = calendrierId;
        this.annee = annee;
        this.mois = mois;
        this.jour = jour;
    }

    // Getters et Setters
    public int getCalendrierId() { return calendrierId; }
    public void setCalendrierId(int calendrierId) { this.calendrierId = calendrierId; }
    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }
    public int getMois() { return mois; }
    public void setMois(int mois) { this.mois = mois; }
    public int getJour() { return jour; }
    public void setJour(int jour) { this.jour = jour; }

    @Override
    public String toString() {
        return "Calendrier{" +
                "calendrierId=" + calendrierId +
                ", annee=" + annee +
                ", mois=" + mois +
                ", jour=" + jour +
                '}';
    }
}