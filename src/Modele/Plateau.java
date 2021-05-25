package Modele;

import java.awt.*;
import java.beans.PropertyEditorSupport;
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
    public boolean jackVisible;
    int numTour;
    int numAction;
    public SuspectCouleur idJack;


    public static CarteRue [][] grille;

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
        jack = new Joueur(true, "Hussein", 0,0,false,false);
        enqueteur = new Joueur(false, "Fabien", 0,0, false, true);
        numTour = 0;
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
        initialiseTour();
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
        jetonsActions.add(0,new JetonActions(Actions.DEPLACER_WATSON,Actions.DEPLACER_TOBBY));
        jetonsActions.add(1,new JetonActions(Actions.DEPLACER_JOKER,Actions.ROTATION_DISTRICT));
        jetonsActions.add(2,new JetonActions(Actions.ECHANGER_DISTRICT,Actions.ROTATION_DISTRICT));
        jetonsActions.add(3,new JetonActions(Actions.DEPLACER_SHERLOCK,Actions.INNOCENTER_CARD));
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
                grille[l][c] = new CarteRue(new Point(c,l), getSuspects().get(suspectIndices.get(j)));
                j++;
            }
        }


        //Orientations initiaux (enqueteurs face aux murs)
        jackVisible = false;
        grille[0][0].setOrientation(NSE);
        enqueteurs.get(SHERLOCK).setPositionSurCarte(O);
        grille[0][0].setEnqueteur(enqueteurs.get(SHERLOCK)); //Modified for test (original = [0][0],E)
        System.out.println("Expected 1: " + grille[0][0].getPosEnqueteur(enqueteurs.get(SHERLOCK)));
        System.out.println("Expected 1: " + enqueteurs.get(SHERLOCK).getPositionSurCarte());
        System.out.println("Expected 0,0: " + enqueteurs.get(SHERLOCK).getPosition());


        grille[0][2].setOrientation(NSO);
        grille[0][2].setEnqueteur(enqueteurs.get(WATSON)); //Modified for test (original = [0][2])
        enqueteurs.get(WATSON).setPositionSurCarte(E);
        System.out.println("Expected 2: " + grille[0][2].getPosEnqueteur(enqueteurs.get(WATSON)));
        System.out.println("Expected 2: " + enqueteurs.get(WATSON).getPositionSurCarte());
        System.out.println("Expected 2,0: " + enqueteurs.get(WATSON).getPosition());

        grille[2][1].setOrientation(NEO);
        enqueteurs.get(TOBBY).setPositionSurCarte(S);
        grille[2][1].setEnqueteur(enqueteurs.get(TOBBY)); //Modified for test (original = [2][1])

    }

    void initialiseTour(){
        numTour++;
        numAction = 0;
        jackVisible = false;
        melangeJetonsActions();
    }

    //retourne vrai si c'est la fin du jeu
    public boolean actionJouee(){
        System.out.println("Numéro Tour: " + numTour);
        System.out.println("Numéro Action: " + numAction);
        if (numAction==1 || numAction==3){
            changerJoueur();
        } else if (numAction == 4){
            if (finJeu()){
                System.out.println("Fin du jeu");
                return true;
            } else {
                initialiseTour();
            }
        }
        return false;
    }

    //Mélange ou inverse les cartes actions (depend du numéro du tour)
    void melangeJetonsActions(){
        if (numTour%2 == 1) inverserJetons();
        else jetJetons();
    }

    void jetJetons() {
        for(JetonActions act:jetonsActions){
            act.setEstRecto(rd.nextBoolean()); //Lancement de chaque jeton
            act.setDejaJoue(false);
        }
    }

    void inverserJetons() {
        for(JetonActions act:jetonsActions){
            Boolean estRecto = !act.estRecto();
            act.setEstRecto(estRecto);
            act.setDejaJoue(false);
        }
    }

    public void changerJoueur() {
		if(joueurCourant.isJack() ) {
            enqueteur.setTurn(true);
			joueurCourant = enqueteur;
		}else {
            jack.setTurn(true);
			joueurCourant = jack;
		}
		
	}

    public void jouerCoup(Coup cp) {
        nouveau(cp);
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

    public ArrayList<Suspect> visibles(){
        ArrayList<Suspect> res = new ArrayList<>();
        for(Enqueteur e:enqueteurs){
            for(Suspect s:e.visible(grille)){
                if (s.getIsJack()){
                    jackVisible = true;
                }
                res.add(s);
            }
        }
        return res;
    }

    //Cet fonction retourne vrai s'il reste qu'une seule carte non innonctée (Jack)
    public boolean verdictTour(){
        ArrayList<Suspect> res = visibles();
        //Si jack est visible par un des trois enqueteurs
        if(jackVisible){
            enqueteur.setSablierVisibles(enqueteur.getSablierVisibles()+1);
            for(Suspect s:suspects){
                if(!res.contains(s)){
                    //Innonceter retourne la carte rue du suspect
                    s.innonceter(grille,suspectsInnoncete);
                }
            }
        } else {
            jack.setSablierCaches(jack.getSablierCaches()+1);
            for (Suspect s:res) {
                s.innonceter(grille,suspectsInnoncete);
            }
        }
        if (suspectsInnoncete.size()==8){
            enqueteur.setWinner(true);
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
            }
        }
        return verdictTour();
    }

    public void reinitialiser(){

        jack.setSablier(0,0);
        jack.setTurn(false);
        jack.setWinner(false);
        enqueteur.setSablier(0,0);
        enqueteur.setTurn(true);
        enqueteur.setWinner(false);
        joueurCourant = enqueteur;
        setNumTour(0);
        initialiseSuspects();
        initialiseGrille();
        initialiseCarteAlibis();
        piocherJack();
        jetJetons();
        initialiseTour();

        afficherConfig();
    }

    public void afficherConfig(){
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
                if(!grille[i][j].getEnqueteurs().isEmpty()){
                    System.out.print(" - Enqueteur: " + grille[i][j].getEnqueteurs().get(0).getNomPersonnage().toString());
                    System.out.print(" - Enqueteur: " + grille[i][j].getEnqueteurs().get(0).getPositionSurCarte());
                }
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

    static ArrayList<Suspect> getSuspects() {
        return suspects;
    }

    public Actions getActionJeton(int num){
        if(num < 0 || num > 3) throw new IllegalStateException("Indice des jetons non comprises entre 0 et 3");
        JetonActions jeton = jetonsActions.get(num);
        if(jeton.estRecto()){
            return jeton.getAction1();
        } else {
            return jeton.getAction2();
        }
    }

    public JetonActions getJeton(int num){
        if(num < 0 || num > 3) throw new IllegalStateException("Indice des jetons non comprises entre 0 et 3");
        return jetonsActions.get(num);
    }

    public int getNumTour(){
        return numTour;
    }


    void setNumTour(int numTour){
        this.numTour = numTour;
    }

    public int getTaillePioche(){
        return cartesAlibis.size();
    }

    static ArrayList<Integer> getOrientationsRues(){
        return orientationsRues;
    }

    public void setNumAction(int numAction){
        this.numAction = numAction;
    }
    public int getNumAction() {
        return this.numAction;
    }

    public ArrayList<Suspect> getSuspectsInnoncete(){
        return suspectsInnoncete;
    }
}
