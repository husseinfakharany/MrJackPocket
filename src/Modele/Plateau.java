package Modele;

import Global.Configuration;

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

public class Plateau extends Historique<Coup> implements Cloneable {

    Random rand;
    public Joueur jack;
    public Joueur enqueteur;
    public Joueur joueurCourant;
    public boolean jackVisible;
    private int numTour;
    int numAction;
    public SuspectCouleur idJack;
    Jeu jeu;
    private boolean afficherVerdict;


    public CarteRue [][] grille;

    public ArrayList<JetonActions> jetonsActions;
    ArrayList<CarteAlibi> cartesAlibis;

    private ArrayList<Suspect> suspects;
    ArrayList<Suspect> suspectsInnocete;
    public ArrayList<Enqueteur> enqueteurs;

    static ArrayList<Integer> orientationsRues;
    public static final int NSEO = 0b1111; //15
    public static final int NSE = 0b1110;  //12
    public static final int NSO = 0b1101;  //13
    public static final int SEO = 0b0111;  //7
    public static final int NEO = 0b1011;  //11

    static final int N = 0b1000;
    static final int S = 0b0100;
    static final int E = 0b0010;
    static final int O = 0b0001;

    public static final int SHERLOCK = 0;
    public static final int WATSON = 1;
    public static final int TOBBY = 2;

    public Plateau(Jeu j){
        rand = new Random(Jeu.getSeed());
        jack = new Joueur(true, "Hussein", 0,0,false,false);
        enqueteur = new Joueur(false, "Fabien", 0,0, false, true);
        numTour = 0;
        grille = new CarteRue[3][3];
        joueurCourant = enqueteur;
        jeu = j;
        setAfficherVerdict(false);

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

    public Plateau(Jeu j, ArrayList<SuspectNom> suspectIndicesSauv, ArrayList<Integer> orientationSauv, SuspectNom nomJackSauv){
        rand = new Random(Jeu.getSeed());
        jack = new Joueur(true, "Hussein", 0,0,false,false);
        enqueteur = new Joueur(false, "Fabien", 0,0, false, true);
        numTour = 0;
        grille = new CarteRue[3][3];
        joueurCourant = enqueteur;
        jeu = j;
        setAfficherVerdict(false);

        initialiseOrientationsRues();
        initialiseSuspects();
        initialiseEnqueteurs();
        initialiseJetonsActions();
        forceGrille(suspectIndicesSauv, orientationSauv); 
        initialiseCarteAlibis();
        forceJack(nomJackSauv);
        initialiseTour();
    }

    public boolean tousJetonsJoues(){
        return jeu.plateau().getJeton(0).getDejaJoue() && jeu.plateau().getJeton(1).getDejaJoue() &&
                jeu.plateau().getJeton(2).getDejaJoue() && jeu.plateau().getJeton(3).getDejaJoue();
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
        suspectsInnocete = new ArrayList<>();
        for(SuspectNom e:SuspectNom.values()){
            getSuspects().add(new Suspect(e,null));
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
        for(Suspect s: getSuspects()){
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
        jackVisible = false;
        for(CarteRue[] ligne:grille){
            for(CarteRue carte: ligne){
                carte.setDejaTourne(false);
            }
        }
        melangeJetonsActions();
    }

    private void forceGrille(ArrayList<SuspectNom> suspectIndicesSauv, ArrayList<Integer> orientationSauv){
        int j;
        j=0;
        for(int l=0; l<3; l++){
            for(int c=0; c<3; c++){
                for(Suspect s: getSuspects()){
                    if(s.getNomPersonnage() == suspectIndicesSauv.get(j)){
                        grille[l][c] = new CarteRue(new Point(c,l), s);
                        grille[l][c].setOrientation(orientationSauv.get(j));
                    }
                }
                j++;
            }
        }

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

    //retourne vrai si c'est la fin du tour
    public boolean actionPlus(){
        boolean res=false;
        if(numTour<=7 && numAction<4){
            numAction ++;
        }
        if (numAction == 4){
            if(numTour != 7){
                numAction = 0;
                numTour++;
                changerJoueur();
            }
            res =true;
        }
        System.out.println("Numéro Tour: " + numTour);
        System.out.println("Numéro Action: " + numAction);
        if (numAction==1 || numAction==3){
            changerJoueur();
        }
        return res;
    }

    //true si un retour en arrière est mathématiquement possible
    public boolean actionMoins(){
        boolean res=true;
        if (numAction==1 || numAction==3){
            changerJoueur();
        }
        if(numTour>=0 && numAction>=0){
            numAction --;
        }
        if (numAction == -1){
            if(numTour == 0){
                numAction = 0;
                res = false;
            }
            else {
                numAction = 3;
                numTour--;
            }
        }
        System.out.println("Numéro Tour: " + numTour);
        System.out.println("Numéro Action: " + numAction);
        return res;
    }

    void resetJetons(){
        for(Coup cp: passe.subList(passe.size()-4, passe.size()) ) {
            if (getJeton(cp.getAction().getNumAction()).getAction1().equals(cp.getAction().getAction())) {
                getJeton(cp.getAction().getNumAction()).setEstRecto(true);
            } else if (getJeton(cp.getAction().getNumAction()).getAction2().equals(cp.getAction().getAction())) {
                getJeton(cp.getAction().getNumAction()).setEstRecto(false);
            } else {
                Configuration.instance().logger().warning("Problème de réinistialisation des jetons");
            }
            getJeton(cp.getAction().getNumAction()).setDejaJoue(true);
        }
    }

    public boolean prochainTour(){
        if (finJeu()){
            Configuration.instance().logger().info("Fin du Jeu");
            return true;
        } else {
            jackVisible = false;
            melangeJetonsActions();
            jeu.notifierObserveurs();
        }
        return false;
    }

    //Mélange ou inverse les cartes actions (depend du numéro du tour)
    void melangeJetonsActions(){
        if (numTour%2 == 0) jetJetons();
        else inverserJetons();
    }

    void jetJetons() {
        for(JetonActions act:jetonsActions){
            act.setEstRecto(rand.nextBoolean()); //Lancement de chaque jeton
            act.setDejaJoue(false);
        }
    }

    void forceJetons(ArrayList<Boolean> jetonsActionsSauv) {
        int i = 0;
        for(JetonActions act:jetonsActions){
            act.setEstRecto(jetonsActionsSauv.get(i)); //Lancement de chaque jeton
            act.setDejaJoue(false);
            i++;
        }
    }

    void inverserJetons() {
        for(JetonActions act:jetonsActions){
            boolean estRecto = !act.estRecto();
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

    public boolean jouerCoup(Coup cp) {
        return nouveau(cp);
    }


    public void piocherJack(){
        int index = rand.nextInt(cartesAlibis.size());
        CarteAlibi JackCard = cartesAlibis.get(index);
        cartesAlibis.remove(index);
        JackCard.getSuspect().setIsJack(true);
        JackCard.getSuspect().setPioche(true);
        idJack = JackCard.getCouleur();
    }

    public void forceJack(SuspectNom nomJackSauv){
        int index=-1;
        for(int i=0; i<cartesAlibis.size(); i++){
            if (cartesAlibis.get(i).getSuspect().getNomPersonnage() == nomJackSauv){
                index = i;
            }
        }
        CarteAlibi JackCard = cartesAlibis.get(index);
        cartesAlibis.remove(index);
        JackCard.getSuspect().setIsJack(true);
        JackCard.getSuspect().setPioche(true);
        idJack = JackCard.getCouleur();
    }

    public CarteAlibi piocher(){
        int index = rand.nextInt(cartesAlibis.size());
        CarteAlibi card = cartesAlibis.get(index);
        cartesAlibis.remove(index);
        card.getSuspect().setPioche(true);
        return card;
    }

    public void addToPioche(CarteAlibi card){
        cartesAlibis.add(card);
        card.getSuspect().setPioche(false);
    }

    public ArrayList<Suspect> visibles(){
        ArrayList<Suspect> res = new ArrayList<>();
        boolean jackSeen = false;
        for(Enqueteur e:enqueteurs){
            for(Suspect s:e.visible(grille)){
                if (s.getIsJack()){
                    jackSeen = true;
                }
                res.add(s);
            }
        }
        jackVisible = jackSeen;
        return res;
    }

    //Cette fonction retourne vrai s'il reste qu'une seule carte non innonctée (Jack)
    public boolean verdictTour(boolean updatePlateau){
        ArrayList<Suspect> res = visibles();
        //Si jack est visible par un des trois enqueteurs
        int nbInnocent = suspectsInnocete.size();
        if (jackVisible) {
            for (Suspect s : getSuspects()) {
                if (!res.contains(s)) {
                    //Innonceter retourne la carte rue du suspect
                    if(updatePlateau) s.innoceter(grille, suspectsInnocete);
                    else if (!suspectsInnocete.contains(s)) nbInnocent++;
                }
            }
        } else {
            if(updatePlateau) jack.setSablierVisibles(jack.getSablierVisibles() + 1);
            for (Suspect s : res) {
                if(updatePlateau) s.innoceter(grille, suspectsInnocete);
                else if (!suspectsInnocete.contains(s)) nbInnocent++;
            }
        }
        if (suspectsInnocete.size()==8 || nbInnocent >= 8){
            enqueteur.setWinner(true);
            return numAction==4;
        }
        return false;
    }

    //Cette fonction retourne vrai s'il reste qu'une seule carte non innonctée (Jack)
    public boolean annuleVerdict(){
        ArrayList<Suspect> res = visibles();
        //Si jack est visible par un des trois enqueteurs
        int nbInnocent = suspectsInnocete.size();
        if (jackVisible) {
            for (Suspect s : getSuspects()) {
                if (!res.contains(s)) {
                    s.suspecter(grille, suspectsInnocete);
                }
            }
        } else {
            jack.setSablierVisibles(jack.getSablierVisibles() - 1);
            for (Suspect s : res) {
                s.suspecter(grille, suspectsInnocete);
            }
        }
        return false;
    }

    //Fonction appelée apres la fin du tour
    public boolean finJeu(boolean updatePlateau, boolean notifierInterface){
        boolean res = verdictTour(updatePlateau);
        if (numTour>=7) {
            res = numAction==4;
        }
        if (jack.getSablier()>=6){
            jack.setWinner(true);
            res =true;
        }
        if(notifierInterface)jeu.notifierObserveurs();
        return res;
    }

    public boolean finJeu(){
        return finJeu(true,true);
    }

    public void reinitialiser(){

        jack = new Joueur(true, "Hussein", 0,0,false,false);
        enqueteur = new Joueur(false, "Fabien", 0,0, false, true);

        jack.setSablier(0,0);
        jack.setTurn(false);
        jack.setWinner(false);
        enqueteur.setSablier(0,0);
        enqueteur.setTurn(true);
        enqueteur.setWinner(false);
        joueurCourant = enqueteur;
        setNumTour(0);
        setNumAction(0);
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

    @Override
    public Plateau clone() throws CloneNotSupportedException {
        Plateau copy;
        try {
            copy = (Plateau) super.clone();

        } catch (CloneNotSupportedException e) {
            Configuration.instance().logger().severe("Bug interne : Plateau non clonable");
            return null;
        }

        copy.enqueteur = enqueteur.clone();
        copy.idJack = idJack;
        copy.jack = jack.clone();
        copy.jackVisible = jackVisible;
        if(joueurCourant.isJack()) copy.joueurCourant = copy.jack;
        else copy.joueurCourant = copy.enqueteur;
        copy.numAction = numAction;
        copy.numTour = numTour;
        copy.rand = new Random(Jeu.getSeed());

        copy.grille = new CarteRue[3][3];
        for (int i = 0; i<3; i++){
            for (int j = 0; j<3; j++){
                copy.grille[i][j] = grille[i][j].clone();
            }
        }

        copy.jetonsActions = new ArrayList<>();
        for(JetonActions j : jetonsActions) {
            copy.jetonsActions.add(j.clone());
        }
        copy.cartesAlibis = (ArrayList<CarteAlibi>) cartesAlibis.clone();
        copy.suspects = new ArrayList<>();
        for(Suspect s : getSuspects()) {
            copy.getSuspects().add(s.clone());
        }
        copy.suspectsInnocete = new ArrayList<>();
        for(Suspect s : suspectsInnocete) {
            copy.suspectsInnocete.add(s.clone());
        }
        copy.enqueteurs = new ArrayList<>();
        for(Enqueteur e : enqueteurs) {
            copy.enqueteurs.add(e.clone());
        }

        //No need to copy content, array empty
        copy.passe = new ArrayList<>();

        copy.futur = new ArrayList<>();

        return copy;
    }

    public static Point suivant(Point p){
        if(p.y==0) p.x = p.x+1;
        if(p.x==4) p.y = p.y+1;
        if(p.y==4) p.x = p.x-1;
        if(p.x==0) {
            p.y = p.y-1;
            if(p.y==0) p.x = p.x+1;
        }
        return p;
    }

    public static Point suivant(int c, int l){
        if(l==0) c = c+1;
        if(c==4) l = l+1;
        if(l==4) c = c-1;
        if(c==0) {
            l = l-1;
            if(l==0) c = c+1;
        }
        return new Point(c,l);
    }

    public static Point calculPosition(Point pos, int orientation){
        Point res = (Point) pos.clone();
        res.x = res.x+1;
        res.y = res.y+1;
        if(orientation == Enqueteur.EST) res.x = res.x+1;
        if(orientation == Enqueteur.OUEST) res.x = res.x-1;
        if(orientation == Enqueteur.SUD) res.y = res.y+1;
        if(orientation == Enqueteur.NORD) res.y = res.y-1;
        return res;
    }

    //Getters and setters

    public ArrayList<Suspect> getSuspects() {
        return suspects;
    }

    public ArrayList<CarteAlibi> getCartesAlibis() {
        return cartesAlibis;
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

    public ArrayList<Suspect> getSuspectsInnocete(){
        return suspectsInnocete;
    }

    public boolean isAfficherVerdict() {
        return afficherVerdict;
    }

    public void setAfficherVerdict(boolean afficherVerdict) {
        this.afficherVerdict = afficherVerdict;
        jeu.notifierObserveurs();
    }
}
