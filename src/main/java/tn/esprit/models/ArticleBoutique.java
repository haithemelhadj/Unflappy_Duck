package tn.esprit.models;

import java.math.BigDecimal;
import java.util.Collection;

public class ArticleBoutique {
    private int id;
    private String nom;
    private Type type;
    private BigDecimal prix;

    // Constructors
        public ArticleBoutique() {
    }

    public ArticleBoutique(int id, String nom, Type type, BigDecimal prix) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.prix = prix;
    }

    public ArticleBoutique(String nom, Type type, BigDecimal prix) {
        this.nom = nom;
        this.type = type;
        this.prix = prix;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    // toString method for easy debugging
    @Override
    public String toString() {
        return "ArticleBoutique{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", prix=" + prix +
                '}';
    }

    public enum Type{
        article, ticket;


      }
    }
