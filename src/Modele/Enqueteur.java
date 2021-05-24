package Modele;

import java.awt.*;
import java.util.ArrayList;

/*
**Enqueteur**:
Position
Nom Personnage (Sherlock, Watson, Tobby)
Methodes retournant les personnages que l'enqueteur peut voir
 */
public class Enqueteur {

    //positionEnqueteur:
    public final static int ABSENT = 0;
    public final static int OUEST = 1;
    public final static int EST = 2;
    public final static int SUD = 4;
    public final static int NORD = 8;

    Point position;
    int positionSurCarte;
    EnqueteurNom nomPersonnage;

    public Enqueteur(EnqueteurNom nomPersonnage, Point position, int positionSurCarte){
        this.nomPersonnage = nomPersonnage;
        this.position = position;
        this.positionSurCarte = positionSurCarte;
    }

    //TODO implement Visible
    ArrayList<Suspect> Visible(CarteRue [][] grille){
        return null;
    }

    public EnqueteurNom getNomPersonnage(){
        return nomPersonnage;
    }

    public void setNomPersonnage(EnqueteurNom nom ) {
        nomPersonnage = nom;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;

    }


    //TODO setPositionSurCarte changes CarteRue automatically

    public int getPositionSurCarte(){
        return positionSurCarte;
    }

    public void setPositionSurCarte(int positionSurCarte){
        this.positionSurCarte = positionSurCarte;
    }
}
