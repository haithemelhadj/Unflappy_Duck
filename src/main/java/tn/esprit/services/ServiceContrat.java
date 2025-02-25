package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Contrat;
import tn.esprit.models.Offre;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceContrat implements IService<Contrat>{
    private final Connection cnx;

    public ServiceContrat(){
        cnx = MyDatabase.getInstance().getCnx();
    }
    @Override
    public void add(Contrat contrat) {
        String qry = "INSERT INTO offre(service_id, offre_id, date_debut, date_fin, description, statut, echeancier_paiement) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1,contrat.getService_id());
            pstm.setInt(2,contrat.getOffre_id());
            pstm.setDate(3, contrat.getDate_debut());
            pstm.setDate(4, contrat.getDate_fin());
            pstm.setString(5, contrat.getDescription());
            pstm.setString(6, contrat.getStatut().name());
            pstm.setString(7, contrat.getEcheancier_paiement());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Contrat> getAll() {
        List<Contrat> contrats = new ArrayList<>();
        String qry = "SELECT * FROM `offre`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                contrats.add(new Contrat(
                        rs.getInt("contrat_id"),
                        rs.getInt("service_id"),
                        rs.getInt("offre_id"),
                        rs.getDate("date_debut"),
                        rs.getDate("date_fin"),
                        rs.getString("description"),
                        Contrat.Status.valueOf(rs.getString("statut")),
                        rs.getString("echeancier_paiement"),
                        rs.getTimestamp("cree_le"),
                        rs.getTimestamp("mis_a_jour_le")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return contrats;
    }


    @Override
    public void update(Contrat contrat) {
        String qry = "UPDATE offre SET service_id=?, offre_id=?, date_debut=?, date_fin=?, description=?, statut=?, echeancier_paiement=? WHERE contrat_id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1,contrat.getService_id());
            pstm.setInt(2,contrat.getOffre_id());
            pstm.setDate(3, contrat.getDate_debut());
            pstm.setDate(4, contrat.getDate_fin());
            pstm.setString(5, contrat.getDescription());
            pstm.setString(6, contrat.getStatut().name());
            pstm.setString(7, contrat.getEcheancier_paiement());
            pstm.setInt(8, contrat.getContrat_id());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Contrat contrat) {
        String qry = "DELETE FROM contrat WHERE contrat_id =?;";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, contrat.getOffre_id());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
