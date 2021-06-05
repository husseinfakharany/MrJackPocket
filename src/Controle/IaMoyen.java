package Controle;

import Modele.*;

import java.util.ArrayList;

public class IaMoyen extends IA{
    @Override
    public Coup coupIA(Jeu j) {

        Joueur joueurCourant = j.plateau().joueurCourant;
        int valeur = 0;
        Action action = new Action(joueurCourant);
        action.setAction( j.plateau().getActionJeton(0));
        Coup cp = new Coup(j.plateau(), null);
        Action aJouer = new Action(joueurCourant);

        //Jamais anticiper ou tirer des jetons car un regarde que le prochain coup
        ArrayList<Actions> listeAction = new ArrayList<>();
        listeAction.add(j.plateau().getActionJeton(0));
        listeAction.add(j.plateau().getActionJeton(1));
        listeAction.add(j.plateau().getActionJeton(2));
        listeAction.add(j.plateau().getActionJeton(3));

        for(Actions actions : listeAction){
            for(Action a : Action.listeAction(actions,joueurCourant)){
                a.setAction(actions);
                cp.setAction(a);
                j.jouerCoup(cp);
                //Appel récursif ici pour le minimax
                if(j.plateau().joueurCourant.isJack()) {
                    int score = ScoreConfig.scoreSuspectVisiblesJackCache(j);
                    if(!(valeur <= score)){
                        aJouer = a;
                        valeur = score;
                    }
                }
                else {
                    int score = ScoreConfig.scoreSuspectElimine(j);
                    if(!(valeur <= score)){
                        aJouer = a;
                        valeur = score;
                    }
                }
                j.annule();
            }
        }
        cp.setAction(aJouer);
        return cp;
    }

}
