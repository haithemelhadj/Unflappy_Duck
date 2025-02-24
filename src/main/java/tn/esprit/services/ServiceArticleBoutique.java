package esprit.tn.services;

import esprit.tn.interfaces.IService;
import esprit.tn.models.ArticleBoutique;
import esprit.tn.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Service Implementation: ServiceArticleBoutique
public class ServiceArticleBoutique implements IService<ArticleBoutique>{
    private Connection cnx = MyDatabase.getInstance().getCnx();

    public ServiceArticleBoutique() {
    }

    @Override
    public void add(ArticleBoutique article) {
        String qry = "INSERT INTO `article_boutique`(`nom`, `type`, `prix`) VALUES (?,?,?)";
        try {
            PreparedStatement pstm = this.cnx.prepareStatement(qry);
            pstm.setString(1, article.getNom());
            pstm.setString(2, article.getType().toString());
            pstm.setBigDecimal(3, article.getPrix());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<ArticleBoutique> getAll() {
        List<ArticleBoutique> articles = new ArrayList<>();
        String qry = "SELECT * FROM article_boutique";
        try {
            Statement stm = this.cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                ArticleBoutique article = new ArticleBoutique(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        ArticleBoutique.Type.valueOf(rs.getString("type")),
                        rs.getBigDecimal("prix")
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return articles;
    }

    @Override
    public void update(ArticleBoutique article) {
        String qry = "UPDATE article_boutique SET nom = ?, type = ?, prix = ? WHERE id = ?";
        try {
            PreparedStatement pstm = this.cnx.prepareStatement(qry);
            pstm.setString(1, article.getNom());
            pstm.setString(2, article.getType().toString());
            pstm.setBigDecimal(3, article.getPrix());
            pstm.setInt(4, article.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(ArticleBoutique article) {
        String qry = "DELETE FROM article_boutique WHERE id = ?";
        try {
            PreparedStatement pstm = this.cnx.prepareStatement(qry);
            pstm.setInt(1, article.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
