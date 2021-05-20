package Modele;

import jdk.jfr.internal.PlatformRecorder;

import java.util.Observable;

public class Jeu extends Observable{

    Plateau plateau;

    public Jeu(){
        plateau = new Plateau();
    }

    public Plateau plateau(){return plateau;}

    //TODO complete determinerCoup
    public Coup determinerCoup(Actions act) {
        return plateau.determinerCoup(act);
    }


    public void jouerCoup(Coup cp) {
        if (cp == null) {
            throw new IllegalStateException("Coup Impossible");
        } else {
            plateau.jouerCoup(cp);
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
}
