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

    @Override
    public void add(Utilisateur utilisateur) {
        String qry = "INSERT INTO `Utilisateur` (`pseudo`, `email`, `mot_de_passe_hache`, `est_actif`, `role_id`) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, utilisateur.getPseudo());
            pstm.setString(2, utilisateur.getEmail());
            pstm.setString(3, utilisateur.getMotDePasseHache());
            pstm.setBoolean(4, utilisateur.isEstActif());
            pstm.setInt(5, utilisateur.getRoleId());

            pstm.executeUpdate();

            // Récupérer l'ID généré automatiquement
            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    utilisateur.setUtilisateurId(generatedKeys.getInt(1)); // Mettre à jour l'ID dans l'objet Utilisateur
                }
            }

            System.out.println("Utilisateur ajouté avec succès. ID : " + utilisateur.getUtilisateurId());
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public List<Utilisateur> getAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String qry = "SELECT * FROM `Utilisateur`";
        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setUtilisateurId(rs.getInt("utilisateur_id"));
                u.setPseudo(rs.getString("pseudo"));
                u.setEmail(rs.getString("email"));
                u.setMotDePasseHache(rs.getString("mot_de_passe_hache"));
                u.setEstActif(rs.getBoolean("est_actif"));
                u.setRoleId(rs.getInt("role_id"));

                utilisateurs.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return utilisateurs;
    }

    @Override
    public void update(Utilisateur utilisateur) {
        String qry = "UPDATE `Utilisateur` SET `pseudo` = ?, `email` = ?, `mot_de_passe_hache` = ?, `est_actif` = ?, `role_id` = ? WHERE `utilisateur_id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, utilisateur.getPseudo());
            pstm.setString(2, utilisateur.getEmail());
            pstm.setString(3, utilisateur.getMotDePasseHache());
            pstm.setBoolean(4, utilisateur.isEstActif());
            pstm.setInt(5, utilisateur.getRoleId());
            pstm.setInt(6, utilisateur.getUtilisateurId());

            pstm.executeUpdate();
            System.out.println("Utilisateur mis à jour avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    public void delete(Utilisateur utilisateur) {
        String qry = "DELETE FROM `Utilisateur` WHERE `utilisateur_id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, utilisateur.getUtilisateurId());
            pstm.executeUpdate();
            System.out.println("Utilisateur supprimé avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
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
