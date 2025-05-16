package tn.esprit.test;

import tn.esprit.models.Utilisateur;
import tn.esprit.models.enums.userRoles;
import tn.esprit.services.ServiceUtilisateur;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize database connection
        MyDatabase db = MyDatabase.getInstance();
        Connection connection = db.getCnx();
/*
        try {
            // Initialize ServiceUtilisateur
            ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();

            // Create a new Utilisateur
            Utilisateur newUtilisateur = new Utilisateur();
            newUtilisateur.setNom("John Doe");
            newUtilisateur.setEmail("john.doe@example.com");
            newUtilisateur.setPassword("password123"); // Ensure this is hashed before storing
            newUtilisateur.setRole(userRoles.ROLE_USER);
            newUtilisateur.setBio("A passionate gamer.");
            newUtilisateur.setPhotoProfil("profile.jpg");
            newUtilisateur.setXp(100);
            newUtilisateur.setNiveau(1);
            newUtilisateur.setXpRequis(200);

            // Add the new Utilisateur to the database
            serviceUtilisateur.add(newUtilisateur);
            System.out.println("Added new utilisateur: " + newUtilisateur);

            // Retrieve all Utilisateurs
            List<Utilisateur> utilisateurs = serviceUtilisateur.getAll();
            System.out.println("All utilisateurs:");
            for (Utilisateur utilisateur : utilisateurs) {
                System.out.println(utilisateur);
            }

            // Retrieve a Utilisateur by ID
            int idToFetch = newUtilisateur.getId(); // Use the ID of the newly added user
            Utilisateur utilisateurById = serviceUtilisateur.getbyId(idToFetch);
            if (utilisateurById != null) {
                System.out.println("Utilisateur with ID " + idToFetch + ": " + utilisateurById);
            } else {
                System.out.println("Utilisateur with ID " + idToFetch + " not found.");
            }

            // Update a Utilisateur
            if (!utilisateurs.isEmpty()) {
                Utilisateur utilisateurToModify = utilisateurs.get(0);
                utilisateurToModify.setBio("Updated bio.");
                serviceUtilisateur.update(utilisateurToModify);
                System.out.println("Modified utilisateur: " + utilisateurToModify);
            }

            // Delete a Utilisateur
            int idToDelete = newUtilisateur.getId(); // Use the ID of the newly added user
            serviceUtilisateur.delete(newUtilisateur);
            System.out.println("Deleted utilisateur with ID " + idToDelete);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**/
    }
}