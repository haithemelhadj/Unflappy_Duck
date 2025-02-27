package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Evenement;
import tn.esprit.models.Offre;
import tn.esprit.models.Service;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceService implements IService<Service> {
    private final Connection cnx;

    public ServiceService(){
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Service service){
        //create Qry SQL
        //execute Qry
        String qry = "INSERT INTO service(freelance_id, titre, description, expertise, duree_jours, prix, mode_paiement) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, service.getFreelancer().getId());
            pstm.setString(2, service.getTitre());
            pstm.setString(3, service.getDescription());
            pstm.setString(4, service.getExpertise());
            pstm.setInt(5, service.getDuree_jours());
            pstm.setBigDecimal(6, service.getPrix());
            pstm.setString(7, service.getMode_paiement().toString());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Service> getAll() {
        List<Service> services = new ArrayList<>();
        String qry = "SELECT * FROM `service`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                services.add(new Service(
                        rs.getInt("service_id"),
                        new ServiceUtilisateur().getUtilisateurById(rs.getInt("freelance_id")),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("expertise"),
                        rs.getInt("duree_jours"),
                        rs.getBigDecimal("prix"),
                        Service.MethodePaiement.valueOf(rs.getString("mode_paiement")),
                        rs.getTimestamp("cree_le"),
                        rs.getTimestamp("mis_a_jour_le")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return services;
    }

    public Service getServiceById(int id) throws SQLException {
        String query = "SELECT * FROM service WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new Service(
                            rs.getInt("service_id"),
                            new ServiceUtilisateur().getUtilisateurById(rs.getInt("freelance_id")),
                            rs.getString("titre"),
                            rs.getString("description"),
                            rs.getString("expertise"),
                            rs.getInt("duree_jours"),
                            rs.getBigDecimal("prix"),
                            Service.MethodePaiement.valueOf(rs.getString("mode_paiement")),
                            rs.getTimestamp("cree_le"),
                            rs.getTimestamp("mis_a_jour_le")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void update(Service service) {
        String qry = "UPDATE service SET freelance_id=?, titre=?, description=?, expertise=?, duree_jours=?, prix=?, mode_paiement=? WHERE service_id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, service.getFreelancer().getId());
            pstm.setString(2, service.getTitre());
            pstm.setString(3, service.getDescription());
            pstm.setString(4, service.getExpertise());
            pstm.setInt(5, service.getDuree_jours());
            pstm.setBigDecimal(6, service.getPrix());
            pstm.setString(7, service.getMode_paiement().name());
            pstm.setInt(8, service.getService_id());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Service service) {
        String qry = "DELETE FROM service WHERE service_id =?;";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, service.getService_id());

            pstm.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
