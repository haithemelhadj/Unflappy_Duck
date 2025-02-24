package tn.esprit.test;


import tn.esprit.services.ServicePanier;

public class Main {
    public static void main(String[] args) {
        ServicePanier sp = new ServicePanier();
        sp.getAll().forEach(System.out::println);
    }
}
