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



    Action(Actions action, Point position1, Point position2, int orientationNew, int numEnqueteur, int deplacement, Joueur joueur){
        this.action = action;
        this.position1 = position1;
        this.position2 = position2;
        this.orientationNew = orientationNew;
        this.orientationOld = 0;
        this.numEnqueteur = numEnqueteur;
        this.deplacement = deplacement;
        this.joueur = joueur;
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