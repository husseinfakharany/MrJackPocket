package Controle;

import Modele.*;

import java.util.ArrayList;


public class IAMeilleureProchain extends IA{
    Jeu j;
    boolean isJack;
    int difficulte;

    public IAMeilleureProchain(Jeu j, boolean isJack) {
        this.j=j;
        this.isJack = isJack;
        if(difficulte>3) difficulte=3;
        if(difficulte<0) difficulte=0;
    }

    @Override
    public Coup coupIA(Jeu j) {
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
                    int score = ScoreConfig.scoreSuspectVisiblesJackCache(j);
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
