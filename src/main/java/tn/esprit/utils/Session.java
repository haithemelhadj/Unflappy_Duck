package tn.esprit.utils;

import tn.esprit.models.Utilisateur;

import java.time.LocalDateTime;

public class Session {
    private static Utilisateur activeUser;

    public static void start(Utilisateur user){
        activeUser = user;
    }
    public static void end(){
        activeUser = null;
    }

    public static boolean isActive(){
        return activeUser != null;
    }

    public static Utilisateur getActiveUser() {
        return activeUser;
    }
}
