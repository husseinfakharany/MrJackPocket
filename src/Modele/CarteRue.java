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

    /*
    Orientations:
    1110 = 14 (Nord, Sud, Est)
    1101 = 13 (Nord, Sud, Ouest)
    1011 = 11 (Nord, Est, Ouest)
    0111 = 7  (Sud, Est, Ouest)
    */

    public CarteRue(Point position){
        //TODO improve code
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
}
