package Modele;

import java.awt.*;
import java.util.ArrayList;

/*
**Coup**:
Type Action
Methode qui execute le coup
 */
public class Coup extends Commande{
	
	private Action action;
	private Plateau plateau;

	public Coup(Plateau plateau, Action action){
		this.plateau = plateau;
		this.action = action;
	}


	public boolean innoncenter(){
		CarteAlibi card = plateau.piocher();
		Suspect s = card.getSuspect();
		Joueur j = action.getJoueur();
		int sabliersJoueur = action.getJoueur().getSablier();
		int sabliersCarte = card.getSablier();
		s.setInnocente(true);
		s.retournerCarteRue(plateau.grille);
		j.setSablier(sabliersJoueur+sabliersCarte);
		j.ajouterCarte(card);
		return true;
	}

	public boolean rotation(Point position1, int orientation) {
		if (plateau.grille[position1.y][position1.x].getDejaTourne()){
			return false;
		}
		action.setOrientationOld(plateau.grille[position1.y][position1.x].getOrientation());
		plateau.grille[position1.y][position1.x].setOrientation(orientation);
		plateau.grille[position1.y][position1.x].setDejaTourne(true);
		return true;
	}

	public boolean echanger(Point position1, Point position2) {

		CarteRue carteRue1 = plateau.grille[position1.y][position1.x];

		int orientation1 = carteRue1.getOrientation();
		Suspect suspect1 = carteRue1.getSuspect();

		CarteRue carteRue2 = plateau.grille[position2.y][position2.x];
		int orientation2 = carteRue2.getOrientation();
		Suspect suspect2 = carteRue2.getSuspect();

		int tmpOrientation = orientation1;
		Suspect tmpSuspect = suspect1;

		if (carteRue1.getInnocente() || carteRue2.getInnocente()){
			return false;
		}

		//On échange que l'orientation et le suspect
		carteRue1.setOrientation(orientation2);
		carteRue1.setSuspect(suspect2);

		carteRue2.setOrientation(tmpOrientation);
		carteRue2.setSuspect(tmpSuspect);

		return true;
	}

	public boolean deplacer(int numEnqueteur, int deplacement, int sens){
		boolean res = true;
		for(int i=0; i<deplacement;i++){
			res = res && avancer(numEnqueteur, sens);
		}
		return res;
	}

	public boolean deplacer(int numEnqueteur, int deplacement){
		return deplacer(numEnqueteur,deplacement,1);
	}

	//TODO Factorise code if possible
	private boolean avancer(int numEnqueteur, int sens) {
		Enqueteur enqueteur = plateau.enqueteurs.get(numEnqueteur);
		int positionX = enqueteur.getPosition().x;
		int positionY = enqueteur.getPosition().y;
		int posSurCarte = enqueteur.getPositionSurCarte();

		switch(posSurCarte){
			case 1:
				//CAS carte rue sur 1 des 4 coins de la grille
				if (positionX==0 && positionY==0){
					if (sens==-1){
						enqueteur.setPositionSurCarte(4);
						return true;
					}
					enqueteur.setPositionSurCarte(8);
					return true;
				} else {
					plateau.grille[positionY][positionX].removeEnqueteur(enqueteur);
					plateau.grille[positionY-sens][positionX].setEnqueteur(enqueteur);
					return true;
				}
			case 2:
				if (positionX==2 && positionY==2){
					if (sens==-1){
						enqueteur.setPositionSurCarte(8);
						return true;
					}
					enqueteur.setPositionSurCarte(4);
					return true;
				} else {
					plateau.grille[positionY][positionX].removeEnqueteur(enqueteur);
					plateau.grille[positionY+sens][positionX].setEnqueteur(enqueteur);
					return true;
				}
			case 4:
				if (positionX==0 && positionY==2){
					if (sens==-1){
						enqueteur.setPositionSurCarte(2);
						return true;
					}
					enqueteur.setPositionSurCarte(1);
					return true;
				} else {
					plateau.grille[positionY][positionX].removeEnqueteur(enqueteur);
					plateau.grille[positionY][positionX-sens].setEnqueteur(enqueteur);
					return true;
				}
			case 8:
				if (positionX==2 && positionY==0){
					if (sens==-1){
						enqueteur.setPositionSurCarte(1);
						return true;
					}
					enqueteur.setPositionSurCarte(2);
					return true;
				} else {
					plateau.grille[positionY][positionX].removeEnqueteur(enqueteur);
					plateau.grille[positionY][positionX+sens].setEnqueteur(enqueteur);
					return true;
				}
		}
		return false;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	boolean execute() {
		switch(action.getAction()){
			case DEPLACER_JOKER:
				return deplacer(action.getNumEnqueteur(),action.getDeplacement(),1);
			case DEPLACER_WATSON:
				return deplacer(1,action.getDeplacement(),1);
			case DEPLACER_SHERLOCK:
				return deplacer(0,action.getDeplacement(),1);
			case DEPLACER_TOBBY:
				return deplacer(2,action.getDeplacement(),1);
			case INNOCENTER_CARD:
				return innoncenter();
			case ECHANGER_DISTRICT:
				//L'ordre des parametres est purement esthetique
				return echanger(action.getPosition1(), action.getPosition2());
			case ROTATION_DISTRICT:
				return rotation(action.getPosition1(),action.getOrientationNew());
			default:
				throw new IllegalStateException("Unexpected action");
		}

	}

	public void ajouterArguments(int l, int c){
		if(action == null) {
			System.out.println("Impossible d'ajouter un argument");
			return;
		}
		switch (action.action){
			case DEPLACER_JOKER:
				action.setDeplacement(1);
				action.setNumEnqueteur(calculEnqueteur(l,c));
				break;
			case DEPLACER_SHERLOCK:
				action.setDeplacement(calculDepl(Plateau.SHERLOCK,l,c));
				action.setNumEnqueteur(Plateau.SHERLOCK);
				break;
			case DEPLACER_WATSON:
				action.setDeplacement(calculDepl(Plateau.WATSON,l,c));
				action.setNumEnqueteur(Plateau.WATSON);
				break;
			case DEPLACER_TOBBY:
				action.setDeplacement(calculDepl(Plateau.TOBBY,l,c));
				action.setNumEnqueteur(Plateau.TOBBY);
				break;
			case ECHANGER_DISTRICT:
				if(action.getPosition1() != null && action.getPosition1().x == c && action.getPosition1().y == l) {
					action.setPosition1(action.getPosition2());
					action.setPosition2(null);
					break;
				}
				if(action.getPosition2() != null && action.getPosition2().x == c && action.getPosition2().y == l) {
					action.setPosition2(null);
					break;
				}
				if(action.getPosition1() == null) action.setPosition1(new Point(c,l));
				else action.setPosition2(new Point(c,l));
				break;
			case INNOCENTER_CARD:
				break;
			case ROTATION_DISTRICT:
				action.setPosition1(new Point(c,l));
				if(action.getOrientationNew() == -1) action.setOrientationNew(plateau.grille[l][c].orientation);
				switch (action.getOrientationNew()){
					case Plateau.SEO:
						action.setOrientationNew(Plateau.NSO);
						break;
					case Plateau.NSO:
						action.setOrientationNew(Plateau.NEO);
						break;
					case Plateau.NEO:
						action.setOrientationNew(Plateau.NSE);
						break;
					case Plateau.NSE:
						action.setOrientationNew(Plateau.SEO);
						break;
					default:
						break;
				}
				break;
		}
	}

	public int calculDepl(int numEnqueteur,int l,int c){
		Enqueteur enqueteur = plateau.enqueteurs.get(numEnqueteur);
		int moveC = c - enqueteur.getPosition().x ;
		int moveL = l - enqueteur.getPosition().y ;
		if(moveC==0 && moveL==0) return 1; //TODO si clique sur le personnage dans un angle le déplace d'un
		if(moveL<0 && enqueteur.getPosition().x !=0) {
			System.out.println("Coup invalide");
			return -1;
		}
		if(moveL>0 && enqueteur.getPosition().x !=2) {
			System.out.println("Coup invalide");
			return -1;
		}
		if(moveC<0 && enqueteur.getPosition().y !=2) {
			System.out.println("Coup invalide");
			return -1;
		}
		if(moveC>0 && enqueteur.getPosition().y !=0) {
			System.out.println("Coup invalide");
			return -1;
		}
		return Math.abs(moveC) + Math.abs(moveL);
	}

	public int calculEnqueteur(int l, int c){
		ArrayList<Enqueteur> enqueteurs;
		if(l==2 && c==0) {
			enqueteurs = plateau.grille[l][c].getEnqueteurs();
			if(!enqueteurs.isEmpty() && enqueteurs.get(0).getPositionSurCarte() == Enqueteur.SUD)
				return enqueteurs.get(0).getNumEnqueteur();
		}
		if(l==0 && c==2) {
			enqueteurs = plateau.grille[l][c].getEnqueteurs();
			if(!enqueteurs.isEmpty() && enqueteurs.get(0).getPositionSurCarte() == Enqueteur.NORD)
				return enqueteurs.get(0).getNumEnqueteur();
		}
		if(l==0 && c==0) {
			enqueteurs = plateau.grille[l][c].getEnqueteurs();
			if(!enqueteurs.isEmpty() && enqueteurs.get(0).getPositionSurCarte() == Enqueteur.OUEST)
				return enqueteurs.get(0).getNumEnqueteur();
		}
		if(l==2 && c==2) {
			enqueteurs = plateau.grille[l][c].getEnqueteurs();
			if(!enqueteurs.isEmpty() && enqueteurs.get(0).getPositionSurCarte() == Enqueteur.EST)
				return enqueteurs.get(0).getNumEnqueteur();
		}
		if(l==0 && c > 0){
			enqueteurs = plateau.grille[l][c-1].getEnqueteurs();
			if(!enqueteurs.isEmpty()) return enqueteurs.get(0).getNumEnqueteur();
		}
		if(l==2 && c < 2){
			enqueteurs = plateau.grille[l][c+1].getEnqueteurs();
			if(!enqueteurs.isEmpty()) return enqueteurs.get(0).getNumEnqueteur();
		}
		if(c==0 && l < 2){
			enqueteurs = plateau.grille[l+1][c].getEnqueteurs();
			if(!enqueteurs.isEmpty()) return enqueteurs.get(0).getNumEnqueteur();
		}
		if(c==2 && l > 0){
			enqueteurs = plateau.grille[l-1][c].getEnqueteurs();
			if(!enqueteurs.isEmpty()) return enqueteurs.get(0).getNumEnqueteur();
		}
		return -1;
	}

	public void reinitialiser(){
		action.reinitialiser();
	}

	@Override
	boolean desexecute() {
		switch (action.getAction()){
			case DEPLACER_JOKER:
			case DEPLACER_WATSON:
			case DEPLACER_SHERLOCK:
			case DEPLACER_TOBBY:
				return deplacer(action.getNumEnqueteur(), action.getDeplacement(),-1);
			case INNOCENTER_CARD:
				//On n'a pas le droit de desexecuter innoncenter_card
				//TODO print in logger error
				return false;
			case ECHANGER_DISTRICT:
				//L'ordre des parametres est purement esthetique
				return echanger(action.getPosition2(), action.getPosition1());
			case ROTATION_DISTRICT:
				return rotation(action.getPosition1(),action.getOrientationOld());
			default:
				throw new IllegalStateException("Unexpected action");
		}
	}
}
