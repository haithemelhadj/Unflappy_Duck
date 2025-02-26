package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.MembresEquipe;
import tn.esprit.models.Tache;
//import tn.esprit.models.enums.Statut;
import tn.esprit.models.enums.roles;
import tn.esprit.utils.MyDatabase;

import javax.management.relation.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceMembresEquipe implements IService<MembresEquipe> {
    private Connection cnx ;
    public ServiceMembresEquipe() {
        cnx = MyDatabase.getInstance().getCnx();
    }
    //region CRUD
    @Override
    public void add(MembresEquipe MembresEquipe) {
        //create Qry SQL
        //execute Qry
        String qry ="INSERT INTO `membre_equipe`(`id`, `equipe_id`, `utilisateur_id`, `role`) VALUES (?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1,MembresEquipe.getId());
            pstm.setInt(2,MembresEquipe.getEquipe_id());
            pstm.setInt(3,MembresEquipe.getUtilisateur_id());
            pstm.setString(4,MembresEquipe.getRole().toString());


            pstm.executeUpdate();
            System.out.println("MembresEquipe added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<MembresEquipe> getAll() {
        //create Qry sql
        //execution
        //Mapping data


        List<MembresEquipe> membresEquipes = new ArrayList<>();
        String qry ="SELECT * FROM `membre_equipe`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()){
                MembresEquipe p = new MembresEquipe();
                p.setId(rs.getInt(1));
                p.setEquipe_id(rs.getInt(2));
                p.setUtilisateur_id(rs.getInt(3));
                //p.setRole(rs.getString());
                //p.setRole(roles.valueOf(rs.getString(4)));
                p.setRole(rs.getString(4));



                membresEquipes.add(p);
            }



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return membresEquipes;
    }

    @Override
    public void update(MembresEquipe MembresEquipe) {
        try {
            String req ="UPDATE `membre_equipe` SET " +
                    "`equipe_id`='" +MembresEquipe.getEquipe_id()+
                    "',`utilisateur_id`='" +MembresEquipe.getUtilisateur_id()+
                    "',`role`='" +MembresEquipe.getRole().toString()+
                    "'WHERE id= '" +MembresEquipe.getId()+"'";

            Statement st = cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("equipes updated");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void delete(MembresEquipe MembresEquipe) {
        try{
            String req=" DELETE FROM `membre_equipe` WHERE id= '" +MembresEquipe.getId()+"'";
            Statement st= cnx.createStatement();
            st.executeUpdate(req);
            System.out.println("membres_equipe supprimé");
        }
        catch (SQLException ex){
            System.out.println("membres_equipe non supprimé!!!");
            System.out.println(ex.getMessage());
        }
    }

    //endregion

    //region
    //endregion
}
