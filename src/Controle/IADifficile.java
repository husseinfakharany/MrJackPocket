package Controle;

import Modele.*;
import Vue.InterfaceGraphique;

public class IADifficile extends IA{

    private int profondeur;

    public IADifficile(Jeu j, boolean isJack) {
        super(j,isJack);
        profondeur = 2;
    }

    //Pre-Condition : Vide
    //Post-Condition : Initialise les coeeficients pour la fonction de score
    public void setCoeff(InterfaceGraphique ig){
        super.setCoeff(ig);
    }

    //Pre-Condition : iaS et iaJ initialisé
    //Post-Condition : Joue un coup de l'IA sherlock sur le jeu du controlleur et les copies des deux ia
    public void prochainTour(){
        j.plateau().prochainTour();
    }

    //Pre-Condition : la variable j est une copie du jeu principal et les informations sont égales
    //Post-Condition : Implémentation du minimax seuillé avec fonctions de scoring dans ScoreConfig
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

    //Pre-Condition : la variable j est une copie du jeu principal et les informations sont égales
    //Post-Condition : Renvoie un Coup calculé par l'IA
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


