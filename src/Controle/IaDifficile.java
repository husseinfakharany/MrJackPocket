package Controle;

import Modele.*;

import java.util.ArrayList;

public class IaDifficile extends IA{
    Jeu j;
    Coup cp;
    Action aJouer;
    boolean isJack;
    int difficulte;

    public IaDifficile(Jeu j, boolean isJack) {
        this.j=j;
        this.isJack = isJack;
        if(difficulte>3) difficulte=3;
        if(difficulte<0) difficulte=0;
    }

    public  int tourMinMaxJack(Jeu j){
        Joueur joueurCourant = j.plateau().joueurCourant;
        int valeur = Integer.MIN_VALUE;
        Action action = new Action(joueurCourant);
        action.setAction( j.plateau().getActionJeton(0));
        Coup cp = new Coup(j.plateau(), null);
        aJouer = new Action(joueurCourant);

        //Jamais anticiper ou tirer des jetons car un regarde que le prochain coup
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

                int score = ScoreConfig.scoreSuspectVisiblesJackCache(j);
                if(tourMinMaxNotJack(j) > valeur){
                    aJouer = a;
                    valeur = score;
                }
            }
            j.annule();
        }

        return valeur;
    }

    public  int tourMinMaxNotJack(Jeu j){
        Joueur joueurCourant = j.plateau().joueurCourant;
        int valeur = Integer.MAX_VALUE;
        Action action = new Action(joueurCourant);
        action.setAction( j.plateau().getActionJeton(0));
        cp = new Coup(j.plateau(), null);
        aJouer = new Action(joueurCourant);

        //Jamais anticiper ou tirer des jetons car un regarde que le prochain coup
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

                int score = ScoreConfig.scoreSuspectVisiblesJackCache(j);
                if(tourMinMaxJack(j) < valeur ){
                    aJouer = a;
                    valeur = score;
                }
            }
            j.annule();
        }

        return valeur;
    }


    @Override
    public Coup coupIA(Jeu j) {
              int score;
                //Appel récursif ici pour le minimax

                if(j.plateau().joueurCourant.isJack()) {
                   score= tourMinMaxJack(j);
                }
                else {
                  score= tourMinMaxNotJack(j);
                }

               cp.setAction(aJouer);
               return cp;


    }
}


