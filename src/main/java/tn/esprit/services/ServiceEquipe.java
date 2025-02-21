package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Equipe;
import tn.esprit.models.Tache;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEquipe implements IService<Equipe> {
    private Connection cnx ;

    public ServiceEquipe() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    //region CRUD
    @Override
    public void add(Equipe equipe) {
        //create Qry SQL
        //execute Qry
        String qry ="INSERT INTO `equipes`(`id`, `evenement_id`, `nom`, `chef_equipe_id`) VALUES (?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1,equipe.getId() );
            pstm.setInt(2,equipe.getEvenement_id() );
            pstm.setString(3,equipe.getNom() );
            pstm.setInt(4,equipe.getChef_equipe_id());


            pstm.executeUpdate();
            System.out.println("equipe added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<Equipe> getAll() {
        //create Qry sql
        //execution
        //Mapping data


        List<Equipe> equipes = new ArrayList<>();
        String qry ="SELECT * FROM `equipes`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                Equipe p = new Equipe();
                p.setId(rs.getInt(1));
                p.setEvenement_id(rs.getInt(2));
                p.setNom(rs.getString(3));
                p.setChef_equipe_id(rs.getInt(4));
                equipes.add(p);


                equipes.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return equipes;
    }

    @Override
    public void update(Equipe equipe) {
        try {
            String req = "UPDATE `equipes` SET " +
                    "`evenement_id`='" +equipe.getEvenement_id()+
                    "',`nom`='" +equipe.getNom()+
                    "',`chef_equipe_id`='" +equipe.getChef_equipe_id()+"'WHERE id= '" +equipe.getId()+"'";

            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("equipes updated");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void delete(Equipe equipe) {
        try{
            //DELETE FROM `equipes` WHERE 0
            String req=" DELETE FROM `equipes` WHERE id= '" +equipe.getId()+"'";
            Statement st= cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("equipes supprimé");
        }
        catch (SQLException ex){
            System.out.println("equipes non supprimé!!!");
            System.out.println(ex.getMessage());
        }
    }
    //endregion

    //region CRUD Specifiques

    //endregion
}
