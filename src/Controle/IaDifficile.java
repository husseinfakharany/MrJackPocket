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

    public  int tourMinMaxJack(Jeu j,Action action,Joueur joueurCourant,ArrayList<Actions> listeAction,Coup cp,Action aJouer){

        int valeur = Integer.MIN_VALUE;

        action.setAction( j.plateau().getActionJeton(0));
        cp = new Coup(j.plateau(), null);
        aJouer = new Action(joueurCourant);

        for(Actions actions : listeAction){
            for(Action a : Action.listeAction(actions,joueurCourant)){
                cp.setAction(a);
                j.jouerCoup(cp);
                //Appel récursif ici pour le minimax

                int score = ScoreConfig.scoreSuspectVisiblesJackCache(j);
                if(tourMinMaxNotJack(j,action,joueurCourant,listeAction,cp,aJouer) > valeur){
                    aJouer = a;
                    valeur = score;
                }
            }
            j.annule();
        }
        return valeur;
    }

    public  int tourMinMaxNotJack(Jeu j,Action action,Joueur joueurCourant,ArrayList<Actions> listeAction, Coup cp,Action aJouer){

        int valeur = Integer.MAX_VALUE;
        action.setAction( j.plateau().getActionJeton(0));

        for(Actions actions : listeAction){
            for(Action a : Action.listeAction(actions,joueurCourant)){
                cp.setAction(a);
                j.jouerCoup(cp);
                int score = ScoreConfig.scoreSuspectVisiblesJackCache(j);
                if(tourMinMaxJack(j,action,joueurCourant,listeAction,cp,aJouer) < valeur ){
                    aJouer = a;
                    valeur = score;
                }
            }
            j.annule();
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
        //Jamais anticiper ou  tirer des jetons car un regarde que le prochain coup
        ArrayList<JetonActions> listeJetons = new ArrayList<>();

        for (JetonActions actJeton: j.plateau().jetonsActions){
            listeJetons.add(actJeton);
        };

        //Appel récursif ici pour le minimax
        /*
        if(j.plateau().joueurCourant.isJack()) {
           score= tourMinMaxJack(j,action,joueurCourant,listeAction,cp,aJouer);
        }
        else {
          score= tourMinMaxNotJack(j,action,joueurCourant,listeAction,cp,aJouer);
        }*/


        cp.setAction(aJouer);
        return cp;

    }
}


