package Controle;

import Modele.Action;
import Modele.Coup;
import Modele.Jeu;
import Vue.InterfaceGraphique;

public abstract class IA {


    Jeu j;
    boolean isJack;

    public IA(Jeu j, boolean isJack){
        this.j=j;
        this.isJack = isJack;
        if(isJack){
            setCoefDispersionJack(0);
            setCoefTempoJack(1);
            setCoefProtegeSuspect(2);
            setCoefEloigneEnqueteurs(0);
            setCoefJackAvantTout(2);
            setCoefProtegeMain(1);
            setCoefMaxSabliers(2);
            setCoefSauverPlus(1);
        } else {
            setCoefPiocherSherlock(1);
            setCoefDiviserDeux(2);
            setCoefVoirPlus(0);
        }
    }

    public abstract Coup coupIA();

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
    private int coefSauverPlus;

    public int score(Action a){
        int res;
        if(isJack){
            res = coefDispersionJack * ScoreConfig.scoreDispersionEnqueteur(j,-1) +
                    coefTempoJack * ScoreConfig.scoreJackVisiblePourSauver(j) +
                    coefProtegeSuspect * ScoreConfig.scoreSuspectCaches(j) +
                    coefEloigneEnqueteurs * ScoreConfig.scoreEloignement(j) +
                    coefJackAvantTout * ScoreConfig.scoreSuspectVisiblesJackCache(j) +
                    coefProtegeMain * ScoreConfig.scoreSuspectMainCache(j) +
                    coefMaxSabliers * ScoreConfig.scoreSablierJack(j,a.getAction()) +
                    coefSauverPlus * ScoreConfig.scoreSauverPlus(j);
        } else {
            res = coefPiocherSherlock * ScoreConfig.scorePiocheSuspectCaches(j,a.getAction()) +
                    coefDiviserDeux* ScoreConfig.scoreSuspectElimine(j) +
                    coefVoirPlus * ScoreConfig.scoreSuspectVisibles(j);
        }
        return res;
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
            setCoefSauverPlus(ig.getCoefSauverPlus());
        } else {
            setCoefPiocherSherlock(ig.getCoefPiocherSherlock());
            setCoefDiviserDeux(ig.getCoefDiviserDeux());
            setCoefVoirPlus(ig.getCoefVoirPlus());
        }
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

    public int getCoefSauverPlus() {
        return coefSauverPlus;
    }

    public void setCoefSauverPlus(int coefSauverPlus) {
        this.coefSauverPlus = coefSauverPlus;
    }
}