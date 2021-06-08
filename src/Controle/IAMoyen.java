package Controle;

import Modele.*;
import Vue.InterfaceGraphique;


public class IAMoyen extends IA{
    Jeu j;
    boolean isJack;
    private int coefDispersionJack;
    private int coefTempoJack;
    private int coefProtegeSuspect;
    private int coefEloigneEnqueteurs;
    private int coefJackAvantTout;
    private int coefProtegeMain;
    private int coefMaxSabliers;
    private int coefPiocherSherlock;
    private int coefDiviserDeux;
    private int coefVoirPlus;


    public IAMoyen(Jeu j, boolean isJack) {
        this.j=j;
        this.isJack = isJack;
        if(isJack){
            setCoefDispersionJack(0);
            setCoefTempoJack(0);
            setCoefProtegeSuspect(1);
            setCoefEloigneEnqueteurs(0);
            setCoefJackAvantTout(0);
            setCoefProtegeMain(0);
            setCoefMaxSabliers(3);
        } else {
            setCoefPiocherSherlock(0);
            setCoefDiviserDeux(1);
            setCoefVoirPlus(0);
        }
    }

    public void setCoeff(InterfaceGraphique ig){
        if(isJack){
            setCoefDispersionJack(ig.getCoefDispersionJack());
            setCoefTempoJack(ig.getCoefTempoJack());
            setCoefProtegeSuspect(ig.getCoefProtegeSuspect());
            setCoefEloigneEnqueteurs(ig.getCoefEloigneEnqueteurs());
            setCoefJackAvantTout(ig.getCoefJackAvantTout());
            setCoefProtegeMain(ig.getCoefProtegeMain());
            setCoefMaxSabliers(ig.getCoefMaxSabliers());
        } else {
            setCoefPiocherSherlock(ig.getCoefPiocherSherlock());
            setCoefDiviserDeux(ig.getCoefDiviserDeux());
            setCoefVoirPlus(ig.getCoefVoirPlus());
        }
    }

    public void prochainTour(){
        j.plateau().prochainTour();
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
        } else {
            res = coefPiocherSherlock * ScoreConfig.scorePiocheSuspectCaches(j,a.getAction()) +
            coefDiviserDeux* ScoreConfig.scoreSuspectElimine(j) +
            coefVoirPlus * ScoreConfig.scoreSuspectVisibles(j);
        }
        return res;
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

        for(int i=0; i<4; i++){
            JetonActions jetonAct = j.plateau().jetonsActions.get(i);
            if(!jetonAct.getDejaJoue()) {
                for (Action a : Action.listeAction(jetonAct.getActionJeton(), joueurCourant)) {
                    cp.setAction(a);
                    if (j.jouerCoup(cp)){

                        if (j.plateau().joueurCourant.isJack()) {
                            score = score(a);
                            if (valeur <= score) {
                                aJouer = a;
                                aJouer.setNumAction(i);
                                valeur = score;
                            }
                        } else {
                            score = score(a);
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

    public void setCoefDispersionJack(int coefDispersionJack) {
        this.coefDispersionJack = coefDispersionJack;
    }

    public void setCoefTempoJack(int coefTempoJack) {
        this.coefTempoJack = coefTempoJack;
    }

    public void setCoefProtegeSuspect(int coefProtegeSuspect) {
        this.coefProtegeSuspect = coefProtegeSuspect;
    }

    public void setCoefEloigneEnqueteurs(int coefEloigneEnqueteurs) {
        this.coefEloigneEnqueteurs = coefEloigneEnqueteurs;
    }

    public void setCoefJackAvantTout(int coefJackAvantTout) {
        this.coefJackAvantTout = coefJackAvantTout;
    }

    public void setCoefProtegeMain(int coefProtegeMain) {
        this.coefProtegeMain = coefProtegeMain;
    }

    public void setCoefMaxSabliers(int coefMaxSabliers) {
        this.coefMaxSabliers = coefMaxSabliers;
    }

    public void setCoefPiocherSherlock(int coefPiocherSherlock) {
        this.coefPiocherSherlock = coefPiocherSherlock;
    }

    public void setCoefDiviserDeux(int coefDiviserDeux) {
        this.coefDiviserDeux = coefDiviserDeux;
    }

    public void setCoefVoirPlus(int coefVoirPlus) {
        this.coefVoirPlus = coefVoirPlus;
    }
}
