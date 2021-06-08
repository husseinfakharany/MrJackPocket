package Modele;

import Global.Configuration;

import java.awt.*;
import java.util.ArrayList;

/*
**Enqueteur**:
Position
Nom Personnage (Sherlock, Watson, Tobby)
Methodes retournant les personnages que l'enqueteur peut voir
 */
public class Enqueteur implements Cloneable{

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

    ArrayList<Suspect> visible(CarteRue [][] grille){
        ArrayList<Suspect> resultat = new ArrayList<>();
        int i = position.y;
        int j = position.x;
        boolean bool = true;

        switch (positionSurCarte){
            case NORD:
                while (bool && (grille[i][j].getOrientation() & Plateau.N)==Plateau.N){
                    if (!grille[i][j].getSuspect().getInnocente()){
                        resultat.add(grille[i][j].getSuspect());
                    }
                    //Si pas fin de la grille et le sud de la carte est ouvert
                    if (i<2 && ((grille[i][j].getOrientation() & Plateau.S)==Plateau.S)){
                          i++;
                    } else {
                        bool = false;
                    }
                }
                break;
            case SUD:
                while (bool && (grille[i][j].getOrientation() & Plateau.S)==Plateau.S){
                    if (!grille[i][j].getSuspect().getInnocente()){
                        resultat.add(grille[i][j].getSuspect());
                    }
                    //Si pas fin de la grille et le nord de la carte est ouvert
                    if (i>0 && ((grille[i][j].getOrientation() & Plateau.N)==Plateau.N)){
                        i--;
                    } else {
                        bool = false;
                    }
                }
                break;
            case EST:
                while (bool && (grille[i][j].getOrientation() & Plateau.E)==Plateau.E){
                    if (!grille[i][j].getSuspect().getInnocente()){
                        resultat.add(grille[i][j].getSuspect());
                    }
                    //Si pas fin de la grille et l'ouest de la carte est ouvert
                    if (j>0 && ((grille[i][j].getOrientation() & Plateau.O)==Plateau.O)){
                        j--;
                    } else {
                        bool = false;
                    }
                }
                break;
            case OUEST:
                while (bool && (grille[i][j].getOrientation() & Plateau.O)==Plateau.O){
                    if (!grille[i][j].getSuspect().getInnocente()){
                        resultat.add(grille[i][j].getSuspect());
                    }
                    //Si pas fin de la grille et l'ouest de la carte est ouvert
                    if (j<2 && ((grille[i][j].getOrientation() & Plateau.E)==Plateau.E)){
                        j++;
                    } else {
                        bool = false;
                    }
                }
                break;
        }
        return resultat;
    }

    @Override
    public Enqueteur clone() throws CloneNotSupportedException {
        Enqueteur copy;
        try {
            copy = (Enqueteur) super.clone();

        } catch (CloneNotSupportedException e) {
            Configuration.instance().logger().severe("Bug interne: Enqueteur non clonable");
            return null;
        }
        copy.position = (Point) position.clone();
        copy.positionSurCarte = positionSurCarte;
        copy.nomPersonnage = nomPersonnage;
        return copy;
    }

    public EnqueteurNom getNomPersonnage(){
        return nomPersonnage;
    }

    public int getNumEnqueteur(){
        switch (nomPersonnage){
            case SHERLOCK:
                return 0;
            case WATSON:
                return 1;
            case TOBBY:
                return 2;
            default:
                return -1;
        }
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

    public int getPositionSurCarte(){
        return positionSurCarte;
    }

    public void setPositionSurCarte(int positionSurCarte){
        this.positionSurCarte = positionSurCarte;
    }
}
