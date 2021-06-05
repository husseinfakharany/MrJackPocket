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
        Action action = new Action(joueurCourant);
        action.setAction( j.plateau().getActionJeton(0));
        Coup cp = new Coup(j.plateau(), null);
        Action aJouer = new Action(joueurCourant);

        //Jamais anticiper ou  tirer des jetons car un regarde que le prochain coup
        ArrayList<Actions> listeAction = new ArrayList<>();
        listeAction.add(j.plateau().getActionJeton(0));
        listeAction.add(j.plateau().getActionJeton(1));
        listeAction.add(j.plateau().getActionJeton(2));
        listeAction.add(j.plateau().getActionJeton(3));
        for(Actions actions : listeAction){
            for(Action a : Action.listeAction(actions,joueurCourant)){
                cp.setAction(a);
                j.jouerCoup(cp);
                //Appel récursif ici pour le minimax
                if(j.plateau().joueurCourant.isJack()) {
                    int score = score(a);
                    if(valeur <= score){
                        aJouer = a;
                        valeur = score;
                    }
                }
                else {
                    int score = ScoreConfig.scoreSuspectElimine(j);
                    if(valeur <= score){
                        aJouer = a;
                        valeur = score;
                    }
                }
                j.annule();
            }
        }
        cp.setAction(aJouer);
        int i = listeAction.indexOf(aJouer);
        while(j.plateau().getJeton(i).getDejaJoue()){
           listeAction.remove(i);
            i = listeAction.indexOf(aJouer);
        }
        action.setNumAction(i);
        return cp;
    }
}
