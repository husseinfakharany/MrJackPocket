package Modele;

import java.awt.*;
import java.util.*;
import java.util.List;

/*
**Jeu**:
Joueurs
Joueur courant
Compteur tour {1...8} (si pair Jack commence, sinon inverse)
Methode qui change de joueur
Liste jetons (bonne face)
Methode qui mélange les jetons (si tour impair, sinon inverser jetons)
Liste carte alibis restants
Methode pour piocher carte alibi
Contenu (grille) 3x3 cartes rue (Initialiser grille (enqueteur en face d'un mur)
Appel fonction jouer jetons actions (coup)
Afficher automatiquement si Jack est visible
Cacher/retirer les suspects vus (si Jack pas visible, sinon inverse)
Attribuer le sablier du tour courant
 */

public class Jeu extends Observable{
    Joueur jack;
    Joueur enqueteur;
    int numTour;
    List<JetonActions> jetonActions;
    List<CarteAlibi> carteAlibis;
    public CarteRue [][] grille;
    //TODO static may cause error?
    static List<Integer> orientationsRues;
    static List<Suspect> suspects;

    public Jeu(){
        jack = new Joueur();
        enqueteur = new Joueur();
        numTour = 1;
        initialiseOrientationsRues();
        initialiseSuspects();
        grille = new CarteRue[3][3];
        initialiserGrille(); //Initialise et mélange la grille du premier tour
        //melangeCartesAlibi(); //Melange les cartes alibi
        //melangeJetonsActions(); //Mélange ou inverse les cartes actions (depend du numéro du tour)
    }

    private void initialiseOrientationsRues(){
        orientationsRues = new ArrayList<>();
        orientationsRues.add(14);
        orientationsRues.add(13);
        orientationsRues.add(11);
        orientationsRues.add(7);
    }

    private void initialiseSuspects(){
        suspects = new ArrayList<>();
        //On ajoute les 9 suspects en dur
        suspects.add(new Suspect("Ronaldo",null));
        suspects.add(new Suspect("Messi",null));
        suspects.add(new Suspect("Salah",null));
        suspects.add(new Suspect("Mbappe",null));
        suspects.add(new Suspect("Neymar",null));
        suspects.add(new Suspect("Trezeguet",null));
        suspects.add(new Suspect("Zidan",null));
        suspects.add(new Suspect("Giroud",null));
        suspects.add(new Suspect("Mane",null));
    }

    void initialiserGrille(){
        for(int l=0; l<3; l++){
            for(int c=0; c<3; c++){
                //TODO may cause error
                grille[l][c] = new CarteRue(new Point(c,l));
            }
        }
        //Orientations initiaux (enqueteurs face aux murs)
        grille[0][0].orientation = 0b1110;
        grille[0][0].positionEnqueteur = 0b0001;
        grille[0][2].orientation = 0b1101;
        grille[0][2].positionEnqueteur = 0b0010;
        grille[2][1].orientation = 0b1011;
        grille[2][1].positionEnqueteur = 0b0100;

    }

    static List<Integer> orientationsRues(){
        return orientationsRues;
    }

    static List<Suspect> suspects() {
        return suspects;
    }


}
