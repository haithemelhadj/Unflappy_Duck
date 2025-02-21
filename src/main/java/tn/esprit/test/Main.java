package tn.esprit.test;

import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceUtilisateur;


import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        //haithem:
        ServiceUtilisateur sp = new ServiceUtilisateur();
        //Utilisateur u1 = new Utilisateur(2,"nom","prenom",20,"email","mdp", "utilisateur");//,"bio self","photo-url",50,1);
        //sp.delete(u1);

        Utilisateur u1 = new Utilisateur();
        u1.setNom("lowel");
        //sp.ajouterUtilisateur(u1);


        Utilisateur u2 = new Utilisateur(1,"n2","email","mdp","utilisateur","bio2","url",2,1,200);
        u2.setNom("theni");
        sp.add(u2);

        System.out.println(sp.getAll());

/**/

        MyDatabase db = MyDatabase.getInstance();
        Connection connection = db.getCnx();


        try {

            ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();

            Utilisateur newUtilisateur = new Utilisateur();
            newUtilisateur.setNom("John Doe");
            newUtilisateur.setEmail("john.doe@example.com");
            newUtilisateur.setMotDePasse("password123");
            newUtilisateur.setRole("utilisateur");
            newUtilisateur.setBio("A passionate gamer.");
            newUtilisateur.setPhotoProfil("profile.jpg");
            newUtilisateur.setXp(100);
            newUtilisateur.setNiveau(1);
            newUtilisateur.setXpRequis(200);


            serviceUtilisateur.ajouterUtilisateur(newUtilisateur);
            System.out.println("Added new utilisateur!");


            List<Utilisateur> utilisateurs = serviceUtilisateur.getAllUtilisateurs();
            System.out.println("All utilisateurs:");
            for (Utilisateur utilisateur : utilisateurs) {
                System.out.println(utilisateur);
            }


            int idToFetch = 1;
            Utilisateur utilisateurById = serviceUtilisateur.getUtilisateurById(idToFetch);
            if (utilisateurById != null) {
                System.out.println("Utilisateur with ID " + idToFetch + ": " + utilisateurById);
            } else {
                System.out.println("Utilisateur with ID " + idToFetch + " not found.");
            }


            if (!utilisateurs.isEmpty()) {
                Utilisateur utilisateurToModify = utilisateurs.get(0);
                utilisateurToModify.setBio("Updated bio.");
                serviceUtilisateur.modifierUtilisateur(utilisateurToModify);
                System.out.println("Modified utilisateur: " + utilisateurToModify);
            }


            int idToDelete = 1;
            serviceUtilisateur.supprimerUtilisateur(idToDelete);
            System.out.println("Deleted utilisateur with ID " + idToDelete);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**/
    }
}
