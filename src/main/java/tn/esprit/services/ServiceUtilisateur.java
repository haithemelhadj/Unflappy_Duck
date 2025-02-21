package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Tache;
import tn.esprit.models.Utilisateur;
//import tn.esprit.models.enums.Statut;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUtilisateur implements IService<Utilisateur> {

    private Connection connection;

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
            preparedStatement.setString(4, utilisateur.getRole());
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
                utilisateur.setRole(resultSet.getString("role"));
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
                    utilisateur.setRole(resultSet.getString("role"));
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
            preparedStatement.setString(4, utilisateur.getRole());
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
                    utilisateur.setRole(resultSet.getString("role"));
                    utilisateur.setBio(resultSet.getString("bio"));
                    utilisateur.setPhotoProfil(resultSet.getString("photo_profil"));
                    utilisateur.setXp(resultSet.getInt("xp"));
                    utilisateur.setNiveau(resultSet.getObject("niveau", Integer.class));
                    utilisateur.setXpRequis(resultSet.getInt("xp_requis"));
                    return utilisateur;
                }
            }
        }
        return null;
    }



    //haithem: added Iservice and emplimented methods 'ich" (only add & get)
    @Override
    public void add(Utilisateur utilisateur) {
        //create Qry SQL
        //execute Qry
        //"SELECT `id`, `nom`, `email`, `mot_de_passe`, `role`, `bio`, `photo_profil`, `xp`, `niveau`, `xp_requis` FROM `utilisateur` "
        String qry ="INSERT INTO `utilisateur`(`id`, `nom`, `email`, `mot_de_passe`, `role`, `bio`, `photo_profil`, `xp`, `niveau`, `xp_requis`) VALUES (?,?,?,?,?,?,?,?,?,?)";
        //String qry ="INSERT INTO `tache`(`id`, `equipe_id`, `titre`, `description`, `id_responsable`, `statut`) VALUES (?,?,?,?,?,?)" ;
        try {
            PreparedStatement pstm = connection.prepareStatement(qry);
            pstm.setInt(1, utilisateur.getId());
            pstm.setString(2,utilisateur.getNom());
            pstm.setString(3,utilisateur.getEmail());
            pstm.setString(4,utilisateur.getMotDePasse());
            pstm.setString(5,utilisateur.getRole());
            pstm.setString(6,utilisateur.getBio());
            pstm.setString(7,utilisateur.getPhotoProfil());
            pstm.setInt(8,utilisateur.getXp());
            pstm.setInt(9,utilisateur.getNiveau());
            pstm.setInt(10,utilisateur.getXpRequis());


            pstm.executeUpdate();
            System.out.println("tache added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List getAll() {
        List<Utilisateur> utilisateur = new ArrayList<>();
        String qry ="SELECT * FROM `utilisateur`";

        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                Utilisateur p = new Utilisateur();
                p.setId(rs.getInt(1));
                p.setNom(rs.getString(2));
                p.setEmail(rs.getString(3));
                p.setMotDePasse(rs.getString(4));
                p.setRole(rs.getString(5));
                p.setBio(rs.getString(6));
                p.setPhotoProfil(rs.getString(7));
                p.setXp(rs.getInt(8));
                p.setNiveau(rs.getInt(9));
                p.setXpRequis(rs.getInt(10));



                utilisateur.add(p);
            }



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return utilisateur;
    }

    @Override
    public void update(Utilisateur utilisateur) {

    }

    @Override
    public void delete(Utilisateur utilisateur) {

    }

}
