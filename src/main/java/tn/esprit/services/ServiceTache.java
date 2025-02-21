package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Tache;
//import tn.esprit.models.enums.Statut;
import tn.esprit.models.Utilisateur;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceTache implements IService<Tache> {
    private Connection cnx ;

    public ServiceTache() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    //region CRUD
    @Override
    public void add(Tache tache) {
        //System.out.println("service....adding tache....");
        //create Qry SQL
        //execute Qry
        String qry ="INSERT INTO `tache`(`id`, `equipe_id`, `titre`, `description`, `id_responsable`, `statut`) VALUES (?,?,?,?,?,?)" ;
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, tache.getId());
            pstm.setInt(2,tache.getEquipe_id());
            pstm.setString(3,tache.getTitre());
            pstm.setString(4,tache.getDescription());
            //pstm.setInt(5,tache.getId_assignateur());, `id_assignateur`
            pstm.setInt(5,tache.getId_responsable());
            pstm.setString(6,tache.getStatut());


            pstm.executeUpdate();
            System.out.println("tache added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Tache> getAll() {
        List<Tache> taches = new ArrayList<>();
        String qry ="SELECT * FROM `tache`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                Tache p = new Tache();
                p.setId(rs.getInt(1));
                p.setEquipe_id(rs.getInt(2));
                p.setTitre(rs.getString(3));
                p.setDescription(rs.getString(4));
                //p.setId_assignateur(rs.getInt(5));
                p.setId_responsable(rs.getInt(5));
                //p.setStatut(Statut.valueOf(rs.getString(6)));
                p.setStatut(rs.getString(6));


                taches.add(p);
            }



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return taches;
    }


    @Override
    public void update(Tache tache) {
        try {
            String req ="UPDATE `tache` SET " +
                    "`equipe_id`='" +tache.getEquipe_id()+
                    "',`titre`='" +tache.getTitre()+
                    "',`description`='" +tache.getDescription()+
                    //"',`id_assignateur`='" +tache.getId_assignateur()+
                    "',`id_responsable`='" +tache.getId_responsable()+
                    "',`statut`='" +tache.getStatut().toString()+"'WHERE id= '" +tache.getId()+
                    "'";
            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("tache updated");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void delete(Tache tache) {

        try{
            String req=" DELETE FROM `tache` WHERE id= '" +tache.getId()+"'";
            Statement st= cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("tache supprimé");
        }
        catch (SQLException ex){
            System.out.println("tache non supprimé!!!");
            System.out.println(ex.getMessage());
        }
    }
    //endregion

    //region CRUD Specifiques


    //endregion
}
