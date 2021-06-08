package Controle;

import Modele.*;
import Vue.InterfaceGraphique;

public class IaDifficile extends IA{
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
    private int profondeur;

    public IaDifficile(Jeu j, boolean isJack) {
        this.j=j;
        this.isJack = isJack;
        profondeur = 2;
    }

    public void setCoeff(InterfaceGraphique ig){
        setProfondeur(ig.getProfondeurIA());
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
                            score = minMax(profondeur) + ScoreConfig.scoreSablierJack(j, a.getAction());
                        } else {
                            score = minMax(profondeur) + ScoreConfig.scoreSuspectElimine(j);
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

    public void setProfondeur(int profondeur) {
        this.profondeur = profondeur;
    }
}


