package esprit.tn.test;


import esprit.tn.services.ServicePanier;

public class Main {
    public static void main(String[] args) {
        ServicePanier sp = new ServicePanier();
        sp.getAll().forEach(System.out::println);
    }
}
