package Controle;

import Modele.*;
import Vue.InterfaceGraphique;

import java.util.ArrayList;


public class IAMeilleureProchain extends IA{
    Jeu j;
    boolean isJack;
    int difficulte;
    private int profondeurIA, coefDispersionJack, coefTempoJack, coefProtegeSuspect,
            coefEloigneEnqueteurs, coefJackAvantTout, coefProtegeMain, coefMaxSabliers, coefPiocherSherlock,
            coefDiviserDeux, coefVoirPlus;

    public IAMeilleureProchain(Jeu j, boolean isJack) {
        this.j=j;
        this.isJack = isJack;
        if(difficulte>3) difficulte=3;
        if(difficulte<0) difficulte=0;
    }

    public void setCoeff(InterfaceGraphique ig){
        if(isJack){
            coefDispersionJack =ig.getCoefDispersionJack();
            coefTempoJack=ig.getCoefTempoJack();
            coefProtegeSuspect = ig.getCoefProtegeSuspect();
            coefEloigneEnqueteurs = ig.getCoefEloigneEnqueteurs();
            coefJackAvantTout = ig.getCoefJackAvantTout();
            coefProtegeMain = ig.getCoefProtegeMain();
            coefMaxSabliers = ig.getCoefMaxSabliers();
        } else {
            coefPiocherSherlock = ig.getCoefPiocherSherlock();
            coefDiviserDeux = ig.getCoefDiviserDeux();
            coefVoirPlus = ig.getCoefVoirPlus();
        }
    }

    public int score(Action a){
        int res;
        if(isJack){
            res = coefDispersionJack * ScoreConfig.scoreDispersionEnqueteur(j,-1) +
            coefTempoJack * ScoreConfig.scoreJackVisiblePourSauver(j) +
            coefProtegeSuspect * ScoreConfig.scoreSuspectCaches(j) +
            coefEloigneEnqueteurs * ScoreConfig.scoreEloignement(j) +
            coefJackAvantTout * ScoreConfig.scoreSuspectVisiblesJackCache(j) +
            coefProtegeMain * ScoreConfig.scoreSuspectMainCache(j) +
            coefMaxSabliers * ScoreConfig.scoreSablierJack(j,a.getAction());
            return res;
        } else {
            res = coefPiocherSherlock * ScoreConfig.scoreSuspectCaches(j,a.getAction()) +
            coefDiviserDeux* ScoreConfig.scoreSuspectElimine(j) +
            coefVoirPlus * ScoreConfig.scoreSuspectVisibles(j);
        }
        return res;
    }

    @Override
    public Coup coupIA() {
        Joueur joueurCourant = j.plateau().joueurCourant;
        int valeur = Integer.MIN_VALUE;
        Coup cp = new Coup(j.plateau(), null);
        Action aJouer = new Action(joueurCourant);
        aJouer.setNumAction(0);

        for(int i=0; i<4; i++){
            JetonActions jetonAct = j.plateau().jetonsActions.get(i);
            if(!jetonAct.getDejaJoue()) {
                for (Action a : Action.listeAction(jetonAct.getActionJeton(), joueurCourant)) {
                    cp.setAction(a);
                    if (j.jouerCoup(cp)){
                        //Appel récursif ici pour le minimax
                        if (j.plateau().joueurCourant.isJack()) {
                            int score = ScoreConfig.scoreSablierJack(j,a.getAction());
                            if (valeur <= score) {
                                aJouer = a;
                                aJouer.setNumAction(i);
                                valeur = score;
                            }
                        } else {
                            int score = ScoreConfig.scoreSuspectElimine(j);
                            if (valeur <= score) {
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
        //j.jouerCoup(cp);

        return cp;
    }
}
