package Controle;

import Modele.*;

import java.util.Random;


public class IaAleatoire extends IA{
    Random r;
    Jeu j;
    IaAleatoire(Jeu j) {
        r = new Random();
        this.j=j;
    }

    Coup coupIA(Jeu j) {

        Joueur joueurCourant = j.plateau().joueurCourant;
        Coup coup;
        int numJetonAction =r.nextInt(4);

        Action action = new Action(joueurCourant);
        action.setAction( j.plateau().getActionJeton(numJetonAction));
        return new Coup(j.plateau(),action);
    }
}
