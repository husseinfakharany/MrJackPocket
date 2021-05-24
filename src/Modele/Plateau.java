package Modele;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
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

    Random rd ;
    public Joueur jack;
    public Joueur enqueteur;
    public Joueur joueurCourant;
    int numTour;
    public SuspectCouleur idJack;


    public CarteRue [][] grille;

    //TODO static may cause error?
    static ArrayList<JetonActions> jetonsActions;
    static ArrayList<CarteAlibi> cartesAlibis;
    static ArrayList<Integer> orientationsRues;
    static ArrayList<Suspect> suspects;
    static ArrayList<Suspect> suspectsInnoncete;
    static ArrayList<Enqueteur> enqueteurs;


    public static final int NSEO = 0b1111; //15
    public static final int NSE = 0b1110;  //12
    public static final int NSO = 0b1101;  //13
    public static final int SEO = 0b0111;  //7
    public static final int NEO = 0b1011;  //11

    static final int N = 0b1000;
    static final int S = 0b0100;
    static final int E = 0b0010;
    static final int O = 0b0001;

    static final int SHERLOCK = 0;
    static final int WATSON = 1;
    static final int TOBBY = 2;

    public Plateau(){
        rd = new Random();
        jack = new Joueur(true, "Hussein", 0,false,false);
        enqueteur = new Joueur(false, "Fabien", 0, false, true);
        numTour = 1;
        grille = new CarteRue[3][3];
        joueurCourant = enqueteur;

        initialiseOrientationsRues();
        initialiseSuspects();
        initialiseEnqueteurs();
        initialiseJetonsActions();
        initialiseGrille(); //Initialise et mélange la grille du premier tour
        initialiseCarteAlibis();
        piocherJack();
        jetJetons();
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
        suspectsInnoncete = new ArrayList<>();
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
        System.out.println("Expected 1: " + enqueteurs.get(SHERLOCK).getPositionSurCarte());
        System.out.println("Expected 0,0: " + enqueteurs.get(SHERLOCK).getPosition());


        grille[0][2].setOrientation(NSO);
        grille[0][2].setEnqueteur(enqueteurs.get(WATSON));
        enqueteurs.get(WATSON).setPositionSurCarte(E);
        System.out.println("Expected 2: " + grille[0][2].getPosEnqueteur(enqueteurs.get(WATSON)));
        System.out.println("Expected 2: " + enqueteurs.get(WATSON).getPositionSurCarte());
        System.out.println("Expected 2,0: " + enqueteurs.get(WATSON).getPosition());

        grille[2][1].setOrientation(NEO);
        enqueteurs.get(TOBBY).setPositionSurCarte(S);
        grille[2][1].setEnqueteur(enqueteurs.get(TOBBY));

    }


    //Mélange ou inverse les cartes actions (depend du numéro du tour)
    void melangeJetonsActions(){
        if (numTour%2 == 1) inverserJetons();
        else jetJetons();
    }

    void jetJetons() {
        int act;
        JetonActions current;
        for (act = 0; act < 4; act++) {
            current = jetonsActions.get(act);
            current.setEstRecto(rd.nextBoolean()); //Lancement de chaque jeton
        }
    }

    void inverserJetons() {
        JetonActions current;
        int act;
        for(act=0; act<4; act++){
            current = jetonsActions.get(act);
            current.setEstRecto(!current.estRecto());

        }
    }

    static ArrayList<Integer> orientationsRues(){
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
        //Coup res = new Coup(this,act); //TODO Modify Coup constructor before implementation
        //TODO if already played or cannot be rotated and cannot change 2 cards with 1 already inno cented return null
        //return res;
        return null;
    }

    public void jouerCoup(Coup cp) {
        nouveau(cp);
    }
    
    static ArrayList<Suspect> suspects() {
        return suspects;
    }

    public void piocherJack(){
        int index = rd.nextInt(cartesAlibis.size());
        CarteAlibi JackCard = cartesAlibis.get(index);
        cartesAlibis.remove(index);
        JackCard.getSuspect().setIsJack(true);
        JackCard.getSuspect().setPioche(true);
        idJack = JackCard.getCouleur();
    }

    public CarteAlibi piocher(){
        int index = rd.nextInt(cartesAlibis.size());
        CarteAlibi card = cartesAlibis.get(index);
        cartesAlibis.remove(index);
        card.getSuspect().setPioche(true);
        return card;
    }

    public boolean jackVisible(Enqueteur e){
        for(Suspect s:e.Visible(grille)){
            if (s.getIsJack()){
                return true;
            }
        }
        return false;
    }

    //Cet fonction retourne vrai s'il reste qu'une seule carte non innonctée (Jack)
    public boolean verdictTour(){
        for(Enqueteur e:enqueteurs){
            if (!jackVisible(e)){
                //Si Jack n'est pas visible, tous les suspects visibles sont innoncentés
                for(Suspect s:e.Visible(grille)){
                    s.setInnocente(true);
                    s.retournerCarteRue(grille);
                    suspectsInnoncete.add(s);
                }
            } else {
                //Pour chaque suspect sur la grille, s'il n'est pas visible pas cet enqueteur, il est innocenté
                for(Suspect s:suspects){
                    if (!e.Visible(grille).contains(s)){
                        s.setInnocente(true);
                        s.retournerCarteRue(grille);
                        suspectsInnoncete.add(s);
                    }
                }
            }
        }
        if (suspectsInnoncete.size()==8){
            return true;
        }
        return false;
    }

    //Fonction appelée apres la fin du tour
    public boolean finJeu(){
        if (numTour>=8) {
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
        return verdictTour();
    }

    public Actions getActionJeton(int num){
        if(num < 0 || num > 3) throw new IllegalStateException("Indice des jetons non comprises entre 0 et 4");
        JetonActions jeton = jetonsActions.get(num);
        if(jeton.estRecto()){
            return jeton.getAction1();
        } else {
            return jeton.getAction2();
        }
    }
    public JetonActions getJeton(int num){
        if(num < 0 || num > 3) throw new IllegalStateException("Indice des jetons non comprises entre 0 et 4");
        return jetonsActions.get(num);
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
        jetJetons();

        afficherConfig();
    }

    private void afficherConfig(){
        int compteurCarte = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println("Carte " + compteurCarte);
                System.out.print("Orientation: " + grille[i][j].getOrientation());
                System.out.print(" - Position: " + grille[i][j].getPosition());
                System.out.print(" - Position personnage: " + grille[i][j].getSuspect().getPosition());
                System.out.print(" - Nom Personnage: " + grille[i][j].getSuspect().getNomPersonnage());
                System.out.print(" - Carte Cachée: " + grille[i][j].getSuspect().getInnocente());
                System.out.print(" - Personnage pioché: " + grille[i][j].getSuspect().getPioche());
                System.out.println();
                compteurCarte++;
            }
        }
        System.out.println( "Nb carte(s) :" +compteurCarte );

        System.out.println( getActionJeton(0) );
        System.out.println( getActionJeton(1) );
        System.out.println( getActionJeton(2) );
        System.out.println( getActionJeton(3) );
    }

    //Getters and setters
    void setNumTour(int numTour){
        this.numTour = numTour;
    }

}
