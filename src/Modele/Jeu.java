package Modele;

import Global.Configuration;

import java.util.Observable;
import java.util.Random;

public class Jeu extends Observable{

    public static final long seed = System.currentTimeMillis();
    //public static final long seed = 3141; //Testing seed

    Plateau plateau;

    public Jeu(){
        plateau = new Plateau();
        Configuration.instance().logger().info("Seed used: " + seed);
    }

    public Plateau plateau(){return plateau;}

    public void jouerCoup(Coup cp) {
        if (cp == null) {
            throw new IllegalStateException("Coup Impossible");
        } else {
            plateau.jouerCoup(cp);
            //TODO check with Fabien
            notifyObservers();
        }
    }

    public Coup annule() {
        Coup cp = plateau.annuler();
        notifyObservers();
        return cp;
    }

    public Coup refaire() {
        Coup cp = plateau.refaire();
        notifyObservers();
        return cp;
    }

    //Getters and setters
    public static long getSeed() {
        return seed;
    }
}
