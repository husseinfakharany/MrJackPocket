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

    static final int MUROUEST = 0B0111;
    static final int MUREST = 0B1110;
    static final int MURNORD = 0B0111;
    static final int MURSUD = 0B1011;

    public Jeu(){
        jack = new Joueur(true, "Hussein", 0,false,false);
        enqueteur = new Joueur(false, "Fabien", 0, false, false);
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
        orientationsRues.add(MUREST);
        orientationsRues.add(MUROUEST);
        orientationsRues.add(MURSUD);
        orientationsRues.add(MURNORD);
    }

    private void initialiseSuspects(){
        suspects = new ArrayList<>();
        for(SuspectNom e:SuspectNom.values()){
            suspects.add(new Suspect(e,null));
        }
    }

    void initialiserGrille(){

        for(int l=0; l<3; l++){
            for(int c=0; c<3; c++){
                //TODO may cause error
                grille[l][c] = new CarteRue(new Point(c,l));
            }
        }
        //Orientations initiaux (enqueteurs face aux murs)
        //TODO use getters and setters
        grille[0][0].orientation = 0b1110;
        grille[0][0].positionEnqueteur = 0b0001;
        grille[0][0].enqueteur.nomPersonnage = EnqueteurNom.SHERLOCK;
        grille[0][2].orientation = 0b1101;
        grille[0][2].positionEnqueteur = 0b0010;
        grille[0][2].enqueteur.nomPersonnage = EnqueteurNom.WATSON;
        grille[2][1].orientation = 0b1011;
        grille[2][1].positionEnqueteur = 0b0100;
        grille[2][1].enqueteur.nomPersonnage = EnqueteurNom.TOBBY;

    }

    static List<Integer> orientationsRues(){
        return orientationsRues;
    }

    static List<Suspect> suspects() {
        return suspects;
    }


}
