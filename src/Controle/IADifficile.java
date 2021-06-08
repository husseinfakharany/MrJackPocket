package Controle;

import Modele.*;
import Vue.InterfaceGraphique;

public class IADifficile extends IA{

    private int profondeur;

    public IADifficile(Jeu j, boolean isJack) {
        super(j,isJack);
        profondeur = 3;
    }
    public IADifficile(Jeu j,int profondeur, boolean isJack) {
        super(j,isJack);
        this.profondeur = profondeur;
    }

    public void setCoeff(InterfaceGraphique ig){
        setProfondeur(ig.getProfondeurIA());
        super.setCoeff(ig);
    }

    public void prochainTour(){
        j.plateau().prochainTour();
    }

    public int minMax(int d, Action act){

        Joueur joueurCourant = j.plateau().joueurCourant;
        int valeur;
        if (joueurCourant.isJack()){
            if (d==0){
                return score(act);
            } else {
                valeur = Integer.MIN_VALUE;
            }
        } else {
            if (d==0){
                return score(act);
            } else {
                valeur = Integer.MAX_VALUE;
            }
        }
        Coup cp = new Coup(j.plateau(), null);
        Action aJouer = new Action(joueurCourant);
        aJouer.setNumAction(0);
        int score;

        for(int i=0; i<4; i++) {
            JetonActions jetonAct = j.plateau().jetonsActions.get(i);
            if (!jetonAct.getDejaJoue()) {
                for (Action a : Action.listeAction(jetonAct.getActionJeton(), joueurCourant)) {
                    cp.setAction(a);
                    if (j.jouerCoup(cp)){
                        //Appel récursif pour le minimax
                        if (joueurCourant.isJack()) {
                            score = minMax(d-1, a);
                            if (valeur <= score) {
                                aJouer = a;
                                aJouer.setNumAction(i);
                                valeur = score;
                            }
                        } else {
                            score = minMax(d-1, a);
                            if (valeur >= score) {
                                aJouer = a;
                                aJouer.setNumAction(i);
                                valeur = score;
                            }
                        }
                        j.annule();
                    }
                }
            }
        }
        return valeur;
    }


    @Override
    public Coup coupIA() {
        Joueur joueurCourant = j.plateau().joueurCourant;
        int valeur;
        if (joueurCourant.isJack()){
            valeur = Integer.MIN_VALUE;
        } else {
            valeur = Integer.MAX_VALUE;
        }
        Coup cp = new Coup(j.plateau(), null);
        Action aJouer = new Action(joueurCourant);
        aJouer.setNumAction(0);
        int score;

        for(int i=0; i<4; i++) {
            JetonActions jetonAct = j.plateau().jetonsActions.get(i);
            if (!jetonAct.getDejaJoue()) {
                for (Action a : Action.listeAction(jetonAct.getActionJeton(), joueurCourant)) {
                    cp.setAction(a);
                    if (j.jouerCoup(cp)){
                        //Appel récursif ici pour le minimax
                        if (j.plateau().joueurCourant.isJack()) {
                            score = minMax(profondeur, a);
                            if (valeur <= score) {
                                aJouer = a;
                                aJouer.setNumAction(i);
                                valeur = score;
                            }
                        } else {
                            score = minMax(profondeur, a);
                            if (valeur >= score) {
                                aJouer = a;
                                aJouer.setNumAction(i);
                                valeur = score;
                            }
                        }
                        j.annule();
                    }
                }
            }
        }

        cp.setAction(aJouer);
        return cp;

    }

    public void setProfondeur(int profondeur) {
        this.profondeur = profondeur;
    }
}


