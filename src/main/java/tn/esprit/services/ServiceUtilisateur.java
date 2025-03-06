package tn.esprit.services;

import tn.esprit.models.Utilisateur;
import tn.esprit.models.enums.userRoles;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUtilisateur {

    private final Connection connection;
    private static Utilisateur loggedInUser;
    public ServiceUtilisateur() {
        this.connection = MyDatabase.getInstance().getCnx();
    }

    // logique zidane utilisateur
    public void ajouterUtilisateur(Utilisateur utilisateur) throws SQLException {
        String query = "INSERT INTO utilisateur (nom, email, mot_de_passe, role, bio, photo_profil, xp, niveau, xp_requis) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, utilisateur.getNom());
            preparedStatement.setString(2, utilisateur.getEmail());
            preparedStatement.setString(3, utilisateur.getMotDePasse());
            preparedStatement.setString(4, utilisateur.getRole().toString());
            preparedStatement.setString(5, utilisateur.getBio());
            preparedStatement.setString(6, utilisateur.getPhotoProfil());
            preparedStatement.setInt(7, utilisateur.getXp());
            if (utilisateur.getNiveau() != null) {
                preparedStatement.setInt(8, utilisateur.getNiveau());
            } else {
                preparedStatement.setNull(8, Types.INTEGER);
            }
            preparedStatement.setInt(9, utilisateur.getXpRequis());
            preparedStatement.executeUpdate();
        }
    }

    // logique kiben ilkol
    public List<Utilisateur> getAllUtilisateurs() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateur";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(resultSet.getInt("id"));
                utilisateur.setNom(resultSet.getString("nom"));
                utilisateur.setEmail(resultSet.getString("email"));
                utilisateur.setMotDePasse(resultSet.getString("mot_de_passe"));
                utilisateur.setRole(userRoles.valueOf(resultSet.getString("role")));
                utilisateur.setBio(resultSet.getString("bio"));
                utilisateur.setPhotoProfil(resultSet.getString("photo_profil"));
                utilisateur.setXp(resultSet.getInt("xp"));
                utilisateur.setNiveau(resultSet.getObject("niveau", Integer.class)); // Nullable Integer
                utilisateur.setXpRequis(resultSet.getInt("xp_requis"));
                utilisateurs.add(utilisateur);
            }
        }
        return utilisateurs;
    }

    // logique jiven il utilisateur  bil id
    public Utilisateur getUtilisateurById(int id) throws SQLException {
        String query = "SELECT * FROM utilisateur WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(resultSet.getInt("id"));
                    utilisateur.setNom(resultSet.getString("nom"));
                    utilisateur.setEmail(resultSet.getString("email"));
                    utilisateur.setMotDePasse(resultSet.getString("mot_de_passe"));
                    utilisateur.setRole(userRoles.valueOf(resultSet.getString("role")));
                    utilisateur.setBio(resultSet.getString("bio"));
                    utilisateur.setPhotoProfil(resultSet.getString("photo_profil"));
                    utilisateur.setXp(resultSet.getInt("xp"));
                    utilisateur.setNiveau(resultSet.getObject("niveau", Integer.class)); // Nullable Integer
                    utilisateur.setXpRequis(resultSet.getInt("xp_requis"));
                    return utilisateur;
                }
            }
        }
        return null;
    }

    // logique update
    public void modifierUtilisateur(Utilisateur utilisateur) throws SQLException {
        String query = "UPDATE utilisateur SET nom = ?, email = ?, mot_de_passe = ?, role = ?, bio = ?, photo_profil = ?, xp = ?, niveau = ?, xp_requis = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, utilisateur.getNom());
            preparedStatement.setString(2, utilisateur.getEmail());
            preparedStatement.setString(3, utilisateur.getMotDePasse());
            preparedStatement.setString(4, utilisateur.getRole().toString());
            preparedStatement.setString(5, utilisateur.getBio());
            preparedStatement.setString(6, utilisateur.getPhotoProfil());
            preparedStatement.setInt(7, utilisateur.getXp());
            if (utilisateur.getNiveau() != null) {
                preparedStatement.setInt(8, utilisateur.getNiveau());
            } else {
                preparedStatement.setNull(8, Types.INTEGER);
            }
            preparedStatement.setInt(9, utilisateur.getXpRequis());
            preparedStatement.setInt(10, utilisateur.getId());
            preparedStatement.executeUpdate();
        }
    }

    // logique delete
    public void supprimerUtilisateur(int id) throws SQLException {
        String query = "DELETE FROM utilisateur WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
    // logique il login
    public Utilisateur loginUtilisateur(String email, String motDePasse) throws SQLException {
        String query = "SELECT * FROM utilisateur WHERE email = ? AND mot_de_passe = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, motDePasse); // In production: compare hashed values.
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(resultSet.getInt("id"));
                    utilisateur.setNom(resultSet.getString("nom"));
                    utilisateur.setEmail(resultSet.getString("email"));
                    utilisateur.setMotDePasse(resultSet.getString("mot_de_passe"));
                    utilisateur.setRole(userRoles.valueOf(resultSet.getString("role")));
                    utilisateur.setBio(resultSet.getString("bio"));
                    utilisateur.setPhotoProfil(resultSet.getString("photo_profil"));
                    utilisateur.setXp(resultSet.getInt("xp"));
                    utilisateur.setNiveau(resultSet.getObject("niveau", Integer.class));
                    utilisateur.setXpRequis(resultSet.getInt("xp_requis"));

                    // Store the logged-in user
                    loggedInUser = utilisateur;

                    return utilisateur;
                }
            }
        }
        return null;
    }


    public Utilisateur getLoggedInUser() {
        return loggedInUser;
    }


    public void logout() {
        loggedInUser = null;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

}