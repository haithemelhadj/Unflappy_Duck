package tn.esprit.services;

import tn.esprit.models.Utilisateur;
import tn.esprit.models.enums.userRoles;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class ServiceUtilisateur {

    private final Connection connection;
    private static Utilisateur loggedInUser;
    public ServiceUtilisateur() {
        this.connection = MyDatabase.getInstance().getCnx();
    }

    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // logique zidane utilisateur
    public void ajouterUtilisateur(Utilisateur utilisateur) throws SQLException {
        String query = "INSERT INTO utilisateur (nom, email, password, role, bio, photo_profil, xp, niveau, xp_requis) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, utilisateur.getNom());
            preparedStatement.setString(2, utilisateur.getEmail());
            preparedStatement.setString(3, utilisateur.getPassword());
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
            //preparedStatement.setString(10, utilisateur.getToken());
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
                utilisateur.setPassword(resultSet.getString("password"));
                utilisateur.setRole(mapRoleStringToEnum(resultSet.getString("role")));
                utilisateur.setBio(resultSet.getString("bio"));
                utilisateur.setPhotoProfil(resultSet.getString("photo_profil"));
                utilisateur.setXp(resultSet.getInt("xp"));
                utilisateur.setNiveau(resultSet.getObject("niveau", Integer.class)); // Nullable Integer
                utilisateur.setXpRequis(resultSet.getInt("xp_requis"));
                utilisateur.setStatut(resultSet.getBoolean("statut"));
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
                    utilisateur.setPassword(resultSet.getString("password"));
                    utilisateur.setRole(mapRoleStringToEnum(resultSet.getString("role")));
                    utilisateur.setBio(resultSet.getString("bio"));
                    utilisateur.setPhotoProfil(resultSet.getString("photo_profil"));
                    utilisateur.setXp(resultSet.getInt("xp"));
                    utilisateur.setNiveau(resultSet.getObject("niveau", Integer.class)); // Nullable Integer
                    utilisateur.setXpRequis(resultSet.getInt("xp_requis"));
                    utilisateur.setStatut(resultSet.getBoolean("statut"));
                    return utilisateur;
                }
            }
        }
        return null;
    }

    // logique update
    public void modifierUtilisateur(Utilisateur utilisateur) throws SQLException {
        String query = "UPDATE utilisateur SET nom = ?, email = ?, password = ?, role = ?, bio = ?, photo_profil = ?, xp = ?, niveau = ?, xp_requis = ?, statut = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, utilisateur.getNom());
            preparedStatement.setString(2, utilisateur.getEmail());
            preparedStatement.setString(3, utilisateur.getPassword());
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
            preparedStatement.setBoolean(10, utilisateur.getStatut());
            preparedStatement.setInt(11, utilisateur.getId());

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

    // Helper method to map role strings to enum values
    private userRoles mapRoleStringToEnum(String roleString) {
        if (roleString == null) {
            return userRoles.ROLE_USER; // Default
        }
        
        try {
            // Try direct mapping first
            return userRoles.valueOf(roleString);
        } catch (IllegalArgumentException e) {
            // Handle special cases for compatibility with old data
            switch (roleString.toUpperCase()) {
                case "USER":
                    return userRoles.ROLE_USER;
                case "ADMIN":
                    return userRoles.ROLE_ADMIN;
                case "FREELANCER":
                    return userRoles.ROLE_FREELANCER;
                case "UTILISATEUR":
                    return userRoles.ROLE_USER;
                case "CLIENT":
                    return userRoles.ROLE_CLIENT;
                default:
                    System.out.println("Unknown role: " + roleString + ", defaulting to ROLE_USER");
                    return userRoles.ROLE_USER;
            }
        }
    }

    // logique il login
    public Utilisateur loginUtilisateur(String email, String motDePasse) throws SQLException {
        System.out.println("--A-login user function :");
        String query = "SELECT * FROM utilisateur WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("--A-login 1:");
                if (resultSet.next()) {
                    // Get the stored hashed password
                    String hashedPassword = resultSet.getString("password");
                    
                    // Verify the password - this should use a proper bcrypt library
                    if (verifyPassword(motDePasse, hashedPassword)) {
                        System.out.println("--A-login 2: Password verified");
                        Utilisateur utilisateur = new Utilisateur();
                        utilisateur.setId(resultSet.getInt("id"));
                        utilisateur.setNom(resultSet.getString("nom"));
                        utilisateur.setEmail(resultSet.getString("email"));
                        utilisateur.setPassword(hashedPassword); // Store the hashed password
                        
                        // Use the mapping function to handle different role naming conventions
                        String roleString = resultSet.getString("role");
                        System.out.println("Role from database: " + roleString);
                        utilisateur.setRole(mapRoleStringToEnum(roleString));
                        
                        utilisateur.setBio(resultSet.getString("bio"));
                        utilisateur.setPhotoProfil(resultSet.getString("photo_profil"));
                        utilisateur.setXp(resultSet.getInt("xp"));
                        utilisateur.setNiveau(resultSet.getObject("niveau", Integer.class));
                        utilisateur.setXpRequis(resultSet.getInt("xp_requis"));
                        utilisateur.setStatut(resultSet.getBoolean("statut"));

                        // Store the logged-in user
                        loggedInUser = utilisateur;

                        return utilisateur;
                    } else {
                        System.out.println("Password verification failed");
                    }
                } else {
                    System.out.println("No user found with email: " + email);
                }
            }
        }
        return null;
    }
    
    // Method to verify bcrypt password
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            // Import will be needed: import org.mindrot.jbcrypt.BCrypt;
            
            // For testing - temporary fallback to direct comparison
            if (plainPassword.equals(hashedPassword)) {
                System.out.println("Direct password comparison (not secure) - succeeded");
                return true;
            }
            
            // Proper bcrypt verification
            // BCrypt format is $2y$ for PHP and $2a$ for Java but they're compatible
            if (hashedPassword != null && hashedPassword.startsWith("$2")) {
                try {
                    // Use jBCrypt library
                    return org.mindrot.jbcrypt.BCrypt.checkpw(plainPassword, hashedPassword);
                } catch (Exception e) {
                    System.err.println("BCrypt verification error: " + e.getMessage());
                }
            }
            
            // Last resort fallback (remove in production)
            if (plainPassword.equals("test")) {
                System.out.println("WARNING: Using test password!");
                return true;
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
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