package Modele;

import java.awt.*;

public class Action {

    Actions action;
    Joueur joueur;
    Point position1;
    Point position2;
    int orientation1;
    int orientation2;
    int deplacement;



    Action(Actions action, Point position1, Point position2, int orientation1, int orientation2, int deplacement, Joueur joueur){
        this.action = action;
        this.position1 = position1;
        this.position2 = position2;
        this.orientation1 = orientation1;
        this.orientation2 = orientation2;
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

    public int getOrientation1(){
        return orientation1;
    }

    public void setOrientation1(int orientation){
        this.orientation1 = orientation;
    }

    public int getOrientation2(){
        return orientation2;
    }

    public void setOrientation2(int orientation){
        this.orientation2 = orientation;
    }

    public int getDeplacement(){
        return deplacement;
    }

    public void setDeplacement(int deplacement){
        this.deplacement = deplacement;
    }
}