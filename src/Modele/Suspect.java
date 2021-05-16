package Modele;

import java.awt.*;

/*
**Suspect**:
Nom Personnage
Position
Boolean caché ou pas
 */
public class Suspect {
    public SuspectNom nomPersonnage;
    public Point position; //position sur la grille
    public Boolean carteCache;
    public Boolean pioche;

    //TODO implement getters and setters
    Suspect(SuspectNom nomPersonnage, Point position){
        this.nomPersonnage = nomPersonnage;
        this.position = position;
        carteCache = false;
        pioche = false;
    }
}
