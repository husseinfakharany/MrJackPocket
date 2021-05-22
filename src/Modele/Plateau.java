package Modele;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

public class Plateau extends Historique<Coup> implements Cloneable{

    public Joueur jack;
    public Joueur enqueteur;
    public Joueur joueurCourant;
    int numTour;
    public SuspectCouleur idJack;


    public CarteRue [][] grille;
    public CarteRue [][] temponGrille;

    //TODO static may cause error?
    static List<JetonActions> jetonsActions;
    static List<CarteAlibi> cartesAlibis;
    static List<Integer> orientationsRues;
    static List<Suspect> suspects;
    static List<Enqueteur> enqueteurs;

    public static final int NSEO = 0b1111;
    public static final int NSE = 0b1110;
    public static final int NSO = 0b1101;
    public static final int SEO = 0b0111;
    public static final int NEO = 0b1011;

    static final int N = 0b1000;
    static final int S = 0b0100;
    static final int E = 0b0010;
    static final int O = 0b0001;

    static final int SHERLOCK = 0;
    static final int WATSON = 1;
    static final int TOBBY = 2;

    public Plateau(){
        jack = new Joueur(true, "Hussein", 0,false,false);
        enqueteur = new Joueur(false, "Fabien", 0, false, true);
        numTour = 1;
        grille = new CarteRue[3][3];
        temponGrille = new CarteRue[3][3];
        joueurCourant = enqueteur;

        initialiseOrientationsRues();
        initialiseSuspects();
        initialiseEnqueteurs();
        initialiseJetonsActions();
        initialiseGrille(); //Initialise et mélange la grille du premier tour
        initialiseCarteAlibis();
        piocherJack();
    }


    private void initialiseOrientationsRues(){
        orientationsRues = new ArrayList<>();
        orientationsRues.add(NSO);
        orientationsRues.add(NSE);
        orientationsRues.add(NEO);
        orientationsRues.add(SEO);
    }

    private void initialiseSuspects(){
        suspects = new ArrayList<>();
        for(SuspectNom e:SuspectNom.values()){
            suspects.add(new Suspect(e,null));
        }
    }

    private void initialiseEnqueteurs() {
        enqueteurs = new ArrayList<>();
        enqueteurs.add(SHERLOCK,new Enqueteur(EnqueteurNom.SHERLOCK,null,Enqueteur.ABSENT));
        enqueteurs.add(WATSON,new Enqueteur(EnqueteurNom.WATSON,null,Enqueteur.ABSENT));
        enqueteurs.add(TOBBY,new Enqueteur(EnqueteurNom.TOBBY,null,Enqueteur.ABSENT));

    }

    private void initialiseJetonsActions(){
        jetonsActions = new ArrayList<>();
        //Création des 4 jetons actions en dûr
        jetonsActions.add(new JetonActions(Actions.DEPLACER_WATSON,Actions.DEPLACER_TOBBY));
        jetonsActions.add(new JetonActions(Actions.DEPLACER_JOKER,Actions.ROTATION_DISTRICT));
        jetonsActions.add(new JetonActions(Actions.ECHANGER_DISTRICT,Actions.ROTATION_DISTRICT));
        jetonsActions.add(new JetonActions(Actions.DEPLACER_SHERLOCK,Actions.INNOCENTER_CARD));
    }

    private void initialiseCarteAlibis(){
        cartesAlibis = new ArrayList<>();
        for(Suspect s: suspects){
            cartesAlibis.add(new CarteAlibi(s));
        }
    }
    
    private void initialiseGrille(){

        int i,j;
        ArrayList<Integer> suspectIndices = new ArrayList<>();
        for (i=0; i<9; i++){
            suspectIndices.add(i);
        }
        Collections.shuffle(suspectIndices);

        j=0;
        for(int l=0; l<3; l++){
            for(int c=0; c<3; c++){
                grille[l][c] = new CarteRue(new Point(c,l), suspects().get(suspectIndices.get(j)));
                j++;
            }
        }


        //Orientations initiaux (enqueteurs face aux murs)
        grille[0][0].setOrientation(NSE);
        enqueteurs.get(SHERLOCK).setPositionSurCarte(O);
        grille[0][0].setEnqueteur(enqueteurs.get(SHERLOCK));
        System.out.println("Expected 1: " + grille[0][0].getPosEnqueteur(enqueteurs.get(SHERLOCK)));


        grille[0][2].setOrientation(NSO);
        grille[0][2].setEnqueteur(enqueteurs.get(WATSON));
        enqueteurs.get(WATSON).setPositionSurCarte(E);
        System.out.println("Expected 2: " + grille[0][2].getPosEnqueteur(enqueteurs.get(WATSON)));

        grille[2][1].setOrientation(NEO);
        enqueteurs.get(TOBBY).setPositionSurCarte(S);
        grille[2][1].setEnqueteur(enqueteurs.get(TOBBY));

    }


    //Mélange ou inverse les cartes actions (depend du numéro du tour)
    void melangeJetonsActions(){
        Random rd = new Random();
        int act;
        JetonActions current;
        for(act=0; act<4; act++){
            current = jetonsActions.get(act);
            if (numTour%2 == 1){ //Mélange des jétons actions
                current.setEstRecto(rd.nextBoolean()); //Lancement de chaque jeton
            } else {
               //Inversement de chaque jeton
                current.setEstRecto(!current.estRecto());
            }
        }

    }

    static List<Integer> orientationsRues(){
        return orientationsRues;
    }

    
    public  void changerJoueur() {
		if(joueurCourant.isJack() ) {
            enqueteur.setTurn(true);
			joueurCourant = enqueteur;
		}else {
            jack.setTurn(true);
			joueurCourant = jack;
		}
		
	}

	public Coup determinerCoup(Actions act){
        //Coup res = new Coup(this,act,); //TODO Modify Coup constructor before implementation
        //TODO if already played or cannot be rotated return null
        //return res;
        return null;
    }

    public void jouerCoup(Coup cp) {
        nouveau(cp);
    }
    
    static List<Suspect> suspects() {
        return suspects;
    }

    public void piocherJack(){
        Random rd = new Random();
        int index = rd.nextInt(cartesAlibis.size());
        CarteAlibi JackCard = cartesAlibis.get(index);
        cartesAlibis.remove(index);
        JackCard.getSuspect().setIsJack(true);
        JackCard.getSuspect().setPioche(true);
        idJack = JackCard.getCouleur();
    }

    public CarteAlibi piocher(){
        Random rd = new Random();
        int index = rd.nextInt(cartesAlibis.size());
        CarteAlibi card = cartesAlibis.get(index);
        cartesAlibis.remove(index);
        card.getSuspect().setPioche(true);
        return card;
    }
    //TODO complete case where rest of alibi cards are useless
    public boolean finJeu(){
        if (numTour>8) {
            return true;
        } else {
            if (jack.getSablier()==6){
                jack.setWinner(true);
                return true;
            } else {
                if (enqueteur.getSablier()==3){
                    enqueteur.setWinner(true);
                    return true;
                }
            }
        }
        return false;
    }

    public void reinitialiser(){
      
        jack.setSablier(0);
        jack.setTurn(false);
        jack.setWinner(false);
        enqueteur.setSablier(0);
        enqueteur.setTurn(true);
        enqueteur.setWinner(false);
        joueurCourant = enqueteur;
        setNumTour(0);
        initialiseSuspects();
        initialiseGrille();
        initialiseCarteAlibis();
        piocherJack();
    }

    //Getters and setters
    void setNumTour(int numTour){
        this.numTour = numTour;
    }
}
