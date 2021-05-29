package Modele;

import Global.Configuration;

import java.util.Observable;

public class Jeu extends Observable implements Cloneable{

    //public static final long seed = System.currentTimeMillis();
    public static final long seed = 1622191690051L; //Testing seed

    Plateau plateau;

    public Jeu(){
        plateau = new Plateau(this);
        Configuration.instance().logger().info("Seed used: " + seed);
    }

    public Plateau plateau(){return plateau;}

    public boolean jouerCoup(Coup cp) {
        boolean res;
        if (cp == null) {
            throw new IllegalStateException("Coup Impossible");
        } else {
            res = plateau.jouerCoup(cp);
            notifyObservers();
        }
        return res;
    }

    @Override
    public Jeu clone() throws CloneNotSupportedException {
        Jeu copy;
        try {
            copy = (Jeu) super.clone();

        } catch (CloneNotSupportedException e) {
            Configuration.instance().logger().severe("Bug interne : Jeu non clonable");
            return null;
        }
        copy.plateau = plateau.clone();
        return copy;
    }

    void notifierObserveurs(){
        setChanged();
        notifyObservers();
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
