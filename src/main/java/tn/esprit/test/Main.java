package tn.esprit.test;

import tn.esprit.models.Evenement;
import tn.esprit.models.Lieu;
import tn.esprit.models.Calendrier;
import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceEvenement;
import tn.esprit.services.ServiceLieu;
import tn.esprit.services.ServiceCalendrier;
import tn.esprit.services.ServiceUtilisateur;

import java.sql.Timestamp;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Créer les services
        ServiceLieu serviceLieu = new ServiceLieu();
        ServiceCalendrier serviceCalendrier = new ServiceCalendrier();
        ServiceEvenement serviceEvenement = new ServiceEvenement();
        ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();

        // Scanner pour la saisie utilisateur
        Scanner scanner = new Scanner(System.in);

        // Ajouter un utilisateur
        Utilisateur utilisateur = new Utilisateur(0, "john_doe", "john.doe@example.com", "hashed_password", true, 1);
        serviceUtilisateur.add(utilisateur);
        System.out.println("Utilisateur ajouté avec succès. ID : " + utilisateur.getUtilisateurId());

        // Saisie manuelle des détails du lieu
        System.out.println("Création d'un nouveau lieu :");

        System.out.print("Nom du lieu : ");
        String nomLieu = scanner.nextLine();

        System.out.print("Adresse du lieu : ");
        String adresseLieu = scanner.nextLine();

        System.out.print("Capacité du lieu : ");
        int capaciteLieu = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne après nextInt()

        // Créer le lieu
        Lieu lieu = new Lieu(0, nomLieu, adresseLieu, capaciteLieu);
        serviceLieu.add(lieu);
        System.out.println("Lieu ajouté avec succès. ID : " + lieu.getLieuId());

        // Saisie manuelle des détails du calendrier
        System.out.println("Création d'un nouveau calendrier :");

        System.out.print("Année : ");
        int annee = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne après nextInt()

        System.out.print("Mois : ");
        int mois = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne après nextInt()

        System.out.print("Jour : ");
        int jour = scanner.nextInt();
        scanner.nextLine(); // Pour consommer la nouvelle ligne après nextInt()

        // Créer le calendrier
        Calendrier calendrier = new Calendrier(0, annee, mois, jour);
        serviceCalendrier.add(calendrier);
        System.out.println("Calendrier ajouté avec succès. ID : " + calendrier.getCalendrierId());

        // Saisie manuelle des détails de l'événement
        System.out.println("Création d'un nouvel événement :");

        System.out.print("Nom de l'événement : ");
        String nomEvenement = scanner.nextLine();

        System.out.print("Description de l'événement : ");
        String descriptionEvenement = scanner.nextLine();

        System.out.print("Date de début (format AAAA-MM-JJ HH:MM:SS) : ");
        String dateDebutStr = scanner.nextLine();
        Timestamp dateDebut = Timestamp.valueOf(dateDebutStr);

        System.out.print("Date de fin (format AAAA-MM-JJ HH:MM:SS) : ");
        String dateFinStr = scanner.nextLine();
        Timestamp dateFin = Timestamp.valueOf(dateFinStr);

        // Créer l'événement
        Evenement evenement = new Evenement(
                0, // evenement_id (sera généré automatiquement)
                nomEvenement, // nom
                descriptionEvenement, // description
                dateDebut, // date_debut
                dateFin, // date_fin
                lieu.getLieuId(), // lieu_id (clé étrangère)
                calendrier.getCalendrierId(), // calendrier_id (clé étrangère)
                utilisateur.getUtilisateurId() // createur_evenement (clé étrangère vers Utilisateur)
        );

        // Ajouter l'événement à la base de données
        serviceEvenement.add(evenement);
        System.out.println("Événement ajouté avec succès. ID : " + evenement.getEvenementId());

        // Afficher tous les événements
        System.out.println("Liste des événements : " + serviceEvenement.getAll());

        // Fermer le scanner
        scanner.close();
    }
}