package Controle;

import Modele.*;

import java.awt.*;
import java.util.Random;


public class IaAleatoire extends IA{
    Random r;
    Jeu j;
    boolean isJack;

    public IaAleatoire(Jeu j,boolean isJack) {
        r = new Random();
        this.j=j;
        this.isJack = isJack;
    }

    @Override
    public Coup coupIA() {

        Joueur joueurCourant = j.plateau().joueurCourant;

        int numJetonAction;

        do{
            numJetonAction =r.nextInt(4);
        }while(j.plateau().getJeton(numJetonAction).getDejaJoue());

        Action action = new Action(joueurCourant);
        action.setAction( j.plateau().getActionJeton(numJetonAction));

        action.setNumAction(numJetonAction);

        switch (action.getAction()){
            case DEPLACER_JOKER:
                action.setNumEnqueteur(r.nextInt(4));
                if(isJack)action.setDeplacement(r.nextInt(2));
                action.setDeplacement(1);
                break;
            case DEPLACER_TOBBY:
                action.setNumEnqueteur(Plateau.TOBBY);
                action.setDeplacement(r.nextInt(2)+1);
                break;
            case DEPLACER_WATSON:
                action.setNumEnqueteur(Plateau.WATSON);
                action.setDeplacement(r.nextInt(2)+1);
                break;
            case INNOCENTER_CARD:
                action.setJoueur(j.plateau().joueurCourant);
                break;
            case DEPLACER_SHERLOCK:
                action.setNumEnqueteur(Plateau.SHERLOCK);
                action.setDeplacement(r.nextInt(2)+1);
                break;
            case ECHANGER_DISTRICT:
                action.setPosition1(new Point(r.nextInt(3),r.nextInt(3)));
                action.setPosition2(new Point(r.nextInt(3),r.nextInt(3)));
                break;
            case ROTATION_DISTRICT:
                Point p;
                int orientation;
                do{
                    p = new Point(r.nextInt(3),r.nextInt(3));
                    action.setPosition1(p);
                }while(j.plateau().grille[p.y][p.x].getOrientation() == Plateau.NSEO);
                do{
                    orientation = r.nextInt(4);
                    action.setOrientationNew( (~(1<<orientation)) & 0b1111 ) ;
                }while(j.plateau().grille[p.y][p.x].getOrientation() == action.getOrientationNew());
                break;
        }
        action.setNumAction(numJetonAction);
        return new Coup(j.plateau(),action);
    }
}
