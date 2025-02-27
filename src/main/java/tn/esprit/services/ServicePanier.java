package tn.esprit.services;

import esprit.tn.interfaces.IService;
import esprit.tn.models.ArticleBoutique;
import esprit.tn.models.Panier;
import esprit.tn.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePanier implements IService<Panier> {
    private Connection cnx = MyDatabase.getInstance().getCnx();

    public ServicePanier() {
    }

    @Override
    public void add(Panier panier) {
        String qry = "INSERT INTO `panier`(`utilisateur_id`,`article_id`, `quantite`) VALUES (?,?,?)";
        try {
            PreparedStatement pstm = this.cnx.prepareStatement(qry);
            pstm.setInt(1, panier.getUtilisateur_id());
            pstm.setInt(2, panier.getArticle_id());
            pstm.setInt(3, panier.getQuantite());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Panier> getAll() {
        List<Panier> paniers = new ArrayList<>();
        String qry = "SELECT * FROM panier";
        try {
            Statement stm = this.cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                paniers.add(new Panier(
                        rs.getInt("id"),
                        rs.getInt("utilisateur_id"),
                        rs.getInt("article_id"),
                        rs.getInt("quantite")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return paniers;
    }

    @Override
    public void update(Panier panier) {
        String qry = "UPDATE panier SET `utilisateur_id`=?, `article_id`=?, `quantite`=? WHERE `id` = ?";
        try {
            PreparedStatement pstm = this.cnx.prepareStatement(qry);
            pstm.setInt(1, panier.getUtilisateur_id());
            pstm.setInt(2, panier.getArticle_id());
            pstm.setInt(3, panier.getQuantite());
            pstm.setInt(4, panier.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Panier panier) {
        String qry = "DELETE FROM panier WHERE `id` = ?";
        try {
            PreparedStatement pstm = this.cnx.prepareStatement(qry);
            pstm.setInt(1, panier.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
