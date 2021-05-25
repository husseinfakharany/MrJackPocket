package Modele;

import java.awt.*;

public class Action {

    Actions action;
    Joueur joueur;
    Point position1;
    Point position2;
    int orientationNew;
    int orientationOld;
    int numEnqueteur;
    int deplacement;

    public Action(Joueur joueur){
        this.action = null;
        this.position1 = null;
        this.position2 = null;
        this.orientationOld = -1;
        this.orientationNew = -1;
        this.deplacement = -1;
        this.joueur = joueur;
    }

    public boolean estValide(){
        if(action == null) return false;
        switch (action){
            case DEPLACER_JOKER:
                if (joueur.isJack()) {
                    return (deplacement == -1 ||deplacement == 0 || deplacement == 1 || deplacement == 2) && numEnqueteur >= 0 && numEnqueteur <= 2;
                } else {
                    return (deplacement == 1 || deplacement == 2) && numEnqueteur >= 0 && numEnqueteur <= 2;
                }
            case DEPLACER_TOBBY:
            case DEPLACER_WATSON:
            case DEPLACER_SHERLOCK:
                return deplacement==1 || deplacement==2;
            case INNOCENTER_CARD:
                return true;
            case ECHANGER_DISTRICT:
                return getPosition1() != null && getPosition2() !=null;
            case ROTATION_DISTRICT:
                return getPosition1() != null &&
                    (  getOrientationNew() == Plateau.NSE || getOrientationNew() == Plateau.NSO
                    || getOrientationNew() == Plateau.SEO || getOrientationNew() == Plateau.NEO);
        }
        return false;
    }

    public void reinitialiser(){
        this.action = null;
        this.position1 = null;
        this.position2 = null;
        this.orientationOld = -1;
        this.orientationNew = -1;
        this.deplacement = -1;
        this.joueur = null;
    }

    //Getters and setters
    public Actions getAction(){
        return action;
    }

    public void setAction(Actions action){
        this.action = action;
    }

    public Joueur getJoueur(){
        return joueur;
    }

    public void setJoueur(Joueur joueur){
        this.joueur = joueur;
    }

    public Point getPosition1(){
        return position1;
    }

    public void setPosition1(Point position){
        this.position1 = position;
    }

    public Point getPosition2(){
        return position2;
    }

    public void setPosition2(Point position){
        this.position2 = position;
    }

    public int getOrientationNew(){
        return orientationNew;
    }

    public void setOrientationNew(int orientationNew){
        this.orientationNew = orientationNew;
    }

    public int getOrientationOld(){
        return orientationOld;
    }

    public void setOrientationOld(int orientationOld){
        this.orientationOld = orientationOld;
    }

    public int getNumEnqueteur(){
        return numEnqueteur;
    }

    public void setNumEnqueteur(int numEnqueteur){
        this.numEnqueteur = numEnqueteur;
    }

    public int getDeplacement(){
        return deplacement;
    }

    public void setDeplacement(int deplacement){
        this.deplacement = deplacement;
    }

}