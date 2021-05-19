package Modele;

/*
**Carte Rue: **
Orientation: 4 bits int (Nord, Sud, Est, Ouest)
Position Inspecteur (1 = Ouest, Est = 2, Sud = 4, Nord = 8, 0 si pas sur carte)
Personnage suspect
Methode qui cache le personnage
Boolean dejaRetourne
*/

import java.awt.*;
import java.util.Random;

public class CarteRue {
    public int orientation; //4 bits de poids faible indiquent les orientations des rues (N,S,E,O) - 3/4 activés (si 1110 alors Mur|-)
    public Point position;
    public int positionEnqueteur;
    public Enqueteur enqueteur;
    public Suspect suspect;

    //Orientations:
    //TODO NSEO existe c'est le verso de la carte suspect GRIS
    public final static int NSEO = 15; //1111(Nord, Sud, Est,Ouest)

    public final static int NSE = 14; //1110(Nord, Sud, Est)
    public final static int NSO = 13; //1101 (Nord, Sud, Ouest)
    public final static int NEO = 11; //1011 (Nord, Est, Ouest)
    public final static int SEO = 7; //0111 (Sud, Est, Ouest)

    //Integrable sur 4 bits dans orientations ?
    //positionEnqueteur:
    public final static int ABSENT = 0;
    public final static int OUEST = 1;
    public final static int EST = 2;
    public final static int SUD = 4;
    public final static int NORD = 8;

    public CarteRue(Point position){
        //TODO improve code : Recevoir le suspect dans le constructeur et laisser le jeu distribuer les suspects
        Random rand = new Random();
        //On choisi une orientation des orientations possibles
        orientation = Jeu.orientationsRues().get(rand.nextInt(Jeu.orientationsRues().size()));
        positionEnqueteur = 0;
        enqueteur = new Enqueteur();
        this.position = position;
        int suspectIndex;
        do{ //Si le suspect n'a pas encore une position, on lui attribue une position dans la grille
            suspectIndex = rand.nextInt(Jeu.suspects().size());
            suspect = Jeu.suspects().get(suspectIndex);
        } while (suspect.getPosition() != null);
        suspect = new Suspect(suspect.getNomPersonnage(),this.position);
        Jeu.suspects.set(suspectIndex, suspect);
    }

    //TODO
    public int getPosEnqueteur(){
        return ABSENT;
    }
    //TODO
    public int getOrientation(){
        return NORD;
    }
}
