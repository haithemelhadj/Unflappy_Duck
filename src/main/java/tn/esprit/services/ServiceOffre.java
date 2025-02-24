package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Offre;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOffre implements IService<Offre> {
    private final Connection cnx;

    public ServiceOffre(){
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Offre offre){
        String qry = "INSERT INTO offre(client_id, titre, description, budget, type_contrat, statut, expire_le) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1,offre.getClient_id());
            pstm.setString(2, offre.getTitre());
            pstm.setString(3, offre.getDescription());
            pstm.setBigDecimal(4, offre.getBudget());
            pstm.setString(5, offre.getType_contrat().name());
            pstm.setString(6, offre.getStatut().name());
            pstm.setDate(7, offre.getExpire_le());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Offre> getAll() {
        List<Offre> offres = new ArrayList<>();
        String qry = "SELECT * FROM `offre`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                offres.add(new Offre(
                        rs.getInt("offre_id"),
                        rs.getInt("client_id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getBigDecimal("budget"),
                        Offre.TypeContrat.valueOf(rs.getString("type_contrat")),
                        Offre.Status.valueOf(rs.getString("statut")),
                        rs.getTimestamp("cree_le"),
                        rs.getDate("expire_le")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return offres;
    }

    @Override
    public void update(Offre offre) {
        String qry = "UPDATE offre SET client_id=?, titre=?, description=?, budget=?, type_contrat=?, statut=?, expire_le=? WHERE offre_id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1,offre.getClient_id());
            pstm.setString(2, offre.getTitre());
            pstm.setString(3, offre.getDescription());
            pstm.setBigDecimal(4, offre.getBudget());
            pstm.setString(5, offre.getType_contrat().name());
            pstm.setString(6, offre.getStatut().name());
            pstm.setDate(7, offre.getExpire_le());
            pstm.setInt(8, offre.getOffre_id());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Offre offre) {
        String qry = "DELETE FROM offre WHERE offre_id =?;";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, offre.getOffre_id());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
