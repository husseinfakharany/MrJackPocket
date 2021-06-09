package Controle;

import Modele.*;


public class IAMoyen extends IA{

    public IAMoyen(Jeu j, boolean isJack) {
        super(j,isJack);
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

    //Pre-Condition : iaS et iaJ initialisé
    //Post-Condition : Joue un coup de l'IA sherlock sur le jeu du controlleur et les copies des deux ia
    public void prochainTour(){
        j.plateau().prochainTour();
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

        for(int i=0; i<4; i++){
            JetonActions jetonAct = j.plateau().jetonsActions.get(i);
            if(!jetonAct.getDejaJoue()) {
                for (Action a : Action.listeAction(jetonAct.getActionJeton(), joueurCourant)) {
                    cp.setAction(a);
                    if (j.jouerCoup(cp)){
                        if (joueurCourant.isJack()) {
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


}
