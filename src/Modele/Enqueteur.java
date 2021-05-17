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
    Point position;
    EnqueteurNom nomPersonnage;

    //TODO implement getters and setters
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
}
