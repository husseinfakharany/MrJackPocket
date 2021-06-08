package Controle;

import Modele.*;

import java.util.ArrayList;

public class IaDifficile extends IA{
    Jeu j;
    boolean isJack;
    int difficulte;

    public IaDifficile(Jeu j, boolean isJack) {
        this.j=j;
        this.isJack = isJack;
        if(difficulte>3) difficulte=3;
        if(difficulte<0) difficulte=0;
    }


    public int minMax(int d){
        if (d==0){
            return 0;
        }
        Joueur joueurCourant = j.plateau().joueurCourant;
        int valeur = Integer.MIN_VALUE;
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
                            score = minMax(d-1) + ScoreConfig.scoreSablierJack(j, a.getAction());
                        } else {
                            score = minMax(d-1) + ScoreConfig.scoreSuspectElimine(j);
                        }
                        if (valeur <= score) {
                            aJouer = a;
                            aJouer.setNumAction(i);
                            valeur = score;
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
        int valeur = Integer.MIN_VALUE;
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
                            score = minMax(2) + ScoreConfig.scoreSablierJack(j, a.getAction());
                        } else {
                            score = minMax(2) + ScoreConfig.scoreSuspectElimine(j);
                        }
                        if (valeur <= score) {
                            aJouer = a;
                            aJouer.setNumAction(i);
                            valeur = score;
                        }
                        j.annule();
                    }
                }
            }
        }

        cp.setAction(aJouer);
        return cp;

    }
}


