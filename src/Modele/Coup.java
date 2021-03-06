package Modele;

import Global.Configuration;

import java.awt.*;
import java.util.ArrayList;

/*
**Coup**:
Type Action
Methode qui execute le coup
 */
public class Coup extends Commande implements Cloneable{
	
	private Action action;
	private Plateau plateau;

	public Coup(Plateau plateau, Action action){
		this.plateau = plateau;
		this.action = action;
	}

	//Appelé seulement après un coup sur le Jeu pour le jouer sur JeuClone
	public boolean innocenter(CarteAlibi card){
		Joueur j = action.getJoueur();
		int sabliersCarte;

		CarteAlibi toRemove = null;
		for(CarteAlibi carte:plateau.getCartesAlibis()){
			if(carte.getSuspect().getNomPersonnage().equals(card.getSuspect().getNomPersonnage()))
				toRemove =  carte;
		}
		if(toRemove!=null){
			action.setCartePioche(toRemove);
			sabliersCarte = toRemove.getSablier();
			plateau.cartesAlibis.remove(toRemove);
		} else {
			return false;
		}
		if (!j.isJack()) {
			action.setOrientationSuspect(card.getSuspect().getOrientation());
			card.getSuspect().innocenterPioche(plateau.grille,plateau.getSuspectsInnocete());
			j.setSablierVisibles(j.getSablierVisibles()+sabliersCarte);
		} else {
			j.setSablierCaches(j.getSablierCaches()+sabliersCarte);
		}
		j.ajouterCarte(card);
		return true;
	}

	//Pre-Condition : Plateau Cartes Alibi non nul
	//Post-Condition : Ajoute une carte de la pioche dans la main du jouer qui joue l'action et la supprime de la pioche
	public boolean innocenter(int i){
		CarteAlibi card;
		Joueur j = action.getJoueur();
		int sabliersCarte = 0;

		if(i==1){
			if(action.cartePioche != null) {
				card = action.cartePioche;
				CarteAlibi toRemove = null;
				for(CarteAlibi carte:plateau.getCartesAlibis()){
					if(carte.getSuspect().getNomPersonnage().equals(card.getSuspect().getNomPersonnage()))
						toRemove =  carte;
				}
				if(toRemove!=null){
					action.setCartePioche(toRemove);
					sabliersCarte = toRemove.getSablier();
					plateau.cartesAlibis.remove(toRemove);
				}
			}
			else {
				card = plateau.piocher();
				if (card==null){
					return false;
				}
				action.setCartePioche(card);
				sabliersCarte = card.getSablier();
			}
			if (!j.isJack()) {
				action.setOrientationSuspect(card.getSuspect().getOrientation());
				card.getSuspect().innocenterPioche(plateau.grille,plateau.getSuspectsInnocete());
				j.setSablierVisibles(j.getSablierVisibles()+sabliersCarte);
			} else {
				j.setSablierCaches(j.getSablierCaches()+sabliersCarte);
			}
			j.ajouterCarte(card);
			return true;
		} else if (i==-1){
			card = action.getCartePioche();
			plateau.addToPioche(card);
			sabliersCarte = card.getSablier();
			if (!j.isJack()) {
				card.getSuspect().suspecterPioche(plateau.grille,plateau.getSuspectsInnocete());
				j.setSablierVisibles(j.getSablierVisibles()-sabliersCarte);
			} else {
				j.setSablierCaches(j.getSablierCaches()-sabliersCarte);
			}
			j.supprimerCarte(card);
			return true;
		} else {
			return false;
		}
	}

	//Pre-Condition : Plateau initialisé
	//Post-Condition : Change l'orientation d'une carte rue
	public boolean rotation(Point position1, int orientation, int type) {
		if(plateau.grille[position1.y][position1.x].getOrientation() == orientation && !(plateau.grille[position1.y][position1.x].getOrientation() == Plateau.NSEO)) return false;
		if (plateau.grille[position1.y][position1.x].getDejaTourne() && type==1){
			Configuration.instance().logger().warning("Carte déjà tournée");
			return false;
		}
		action.setOrientationOld(plateau.grille[position1.y][position1.x].getOrientation());
		plateau.grille[position1.y][position1.x].setOrientation(orientation);
		if (type==1){
			plateau.grille[position1.y][position1.x].setDejaTourne(true);
		} else if (type==-1){
			plateau.grille[position1.y][position1.x].setDejaTourne(false);
		}
		return true;
	}

	//Pre-Condition : Plateau initialisé
	//Post-Condition : Echange les arguments des carte rues au coordonnées Point 1 et Point 2
	public boolean echanger(Point position1, Point position2) {

		CarteRue carteRue1 = plateau.grille[position1.y][position1.x];
		int orientation1 = carteRue1.getOrientation();
		Suspect suspect1 = carteRue1.getSuspect();

		CarteRue carteRue2 = plateau.grille[position2.y][position2.x];
		int orientation2 = carteRue2.getOrientation();
		Suspect suspect2 = carteRue2.getSuspect();

		Point tmpPosition = suspect1.getPosition();

		suspect1.setPosition(suspect2.getPosition());
		suspect2.setPosition(tmpPosition);

		//On échange que l'orientation et le suspect
		carteRue1.setOrientation(orientation2);
		carteRue1.setSuspect(suspect2);

		carteRue2.setOrientation(orientation1);
		carteRue2.setSuspect(suspect1);

		return true;
	}

	//Pre-Condition : Plateau initialisé
	//Post-Condition : Déplace l'enqueteur du nombre indiqué par déplacement dans le sens des aiguilles d'une montre
	public boolean deplacer(int numEnqueteur, int deplacement, int sens){
		boolean res = true;
		for(int i=0; i<deplacement;i++){
			res = res && avancer(numEnqueteur, sens);
		}
		return res;
	}

	//Pre-Condition : Plateau initialisé
	//Post-Condition : Déplace l'enqueteur d'une case dans le sens des aiguilles d'une montre
	private boolean avancer(int numEnqueteur, int sens) {
		Enqueteur enqueteur = plateau.enqueteurs.get(numEnqueteur);
		int positionX = enqueteur.getPosition().x;
		int positionY = enqueteur.getPosition().y;
		int posSurCarte = enqueteur.getPositionSurCarte();

		switch(posSurCarte){
			case 1:
				//CAS carte rue sur 1 des 4 coins de la grille
				if (positionX==0 && positionY==0 && sens==1){
					enqueteur.setPositionSurCarte(8);
					return true;
				} else if (positionX==0 && positionY==2 && sens==-1){
					enqueteur.setPositionSurCarte(4);
					return true;
				} else {
					plateau.grille[positionY][positionX].removeEnqueteur(enqueteur);
					plateau.grille[positionY-sens][positionX].setEnqueteur(enqueteur);
					return true;
				}
			case 2:
				if (positionX==2 && positionY==2 && sens==1){
					enqueteur.setPositionSurCarte(4);
					return true;
				} else if (positionX==2 && positionY==0 && sens==-1){
					enqueteur.setPositionSurCarte(8);
					return true;
				} else {
					plateau.grille[positionY][positionX].removeEnqueteur(enqueteur);
					plateau.grille[positionY+sens][positionX].setEnqueteur(enqueteur);
					return true;
				}
			case 4:
				if (positionX==0 && positionY==2 && sens==1){
					enqueteur.setPositionSurCarte(1);
					return true;
				} else if (positionX==2 && positionY==2 && sens==-1){
					enqueteur.setPositionSurCarte(2);
					return true;
				} else {
					plateau.grille[positionY][positionX].removeEnqueteur(enqueteur);
					plateau.grille[positionY][positionX-sens].setEnqueteur(enqueteur);
					return true;
				}
			case 8:
				if (positionX==2 && positionY==0 && sens==1){
					enqueteur.setPositionSurCarte(2);
					return true;
				} else if (positionX==0 && positionY==0 && sens==-1){
					enqueteur.setPositionSurCarte(1);
					return true;
				}
				else {
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

	//Pre-Condition : Coup(this) initialisé + l>=0 et l<=4 ET c>=0 et c<=4
	//Post-Condition : Mets les variables aux valeurs cohérentes avec un clic au coordonnées (c,l) sur le district
	public void ajouterArguments(int l, int c){
		if(action == null || action.getAction() == null) {
			Configuration.instance().logger().warning("Impossible d'ajouter un argument à une action pas initialisé");
			return;
		}
		switch (action.getAction()){
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
				l--;
				c--;
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
				l--;
				c--;
				if(action.getOrientationNew() == Plateau.NSEO || plateau.grille[l][c].orientation == Plateau.NSEO ) action.setOrientationNew(Plateau.NSEO);
				if(action.getOrientationNew() == -1 || action.getOrientationNew() == Plateau.NSEO) action.setOrientationNew(plateau.grille[l][c].orientation);
				switch (action.getOrientationNew()){
					case Plateau.SEO:
						action.setPosition1(new Point(c,l));
						action.setOrientationNew(Plateau.NSO);
						break;
					case Plateau.NSO:
						action.setPosition1(new Point(c,l));
						action.setOrientationNew(Plateau.NEO);
						break;
					case Plateau.NEO:
						action.setPosition1(new Point(c,l));
						action.setOrientationNew(Plateau.NSE);
						break;
					case Plateau.NSE:
						action.setPosition1(new Point(c,l));
						action.setOrientationNew(Plateau.SEO);
						break;
					case Plateau.NSEO:
						action.setOrientationNew(Plateau.NSEO);
						if(action.getPosition1() != null && action.getPosition1().getX() == c && action.getPosition1().getY() == l){
							action.setPosition1(new Point(-1,-1));
							return;
						}
						else action.setPosition1(new Point(c,l));
						break;
					default:
						break;
				}
				action.setOrientationOld(plateau.grille[action.getPosition1().y][action.getPosition1().x].getOrientation());
				break;
		}
	}

	//Pre-Condition : l>=0 et l<=4 ET c>=0 et c<=4
	//Post-Condition : Calcul la distance entre l'enqueteur du coup actuel et la case (c,l)
	public int calculDepl(int numEnqueteur,int l,int c){
		Enqueteur enqueteur = plateau.enqueteurs.get(numEnqueteur);
		Point positionE = Plateau.calculPosition(enqueteur.position,enqueteur.getPositionSurCarte());
		Point suivant = Plateau.suivant(positionE);
		if (suivant.getX() == c && suivant.getY() == l) return 1;
		suivant = Plateau.suivant(suivant);
		if (suivant.getX() == c && suivant.getY() == l) return 2;
		Configuration.instance().logger().info("Position invalide pour l'action en cours");
		return -1;
	}


	//Pre-Condition : l>=0 et l<=4 ET c>=0 et c<=4
	//Post-Condition : Trouve l'enquêteur dans la case précedente où un autre enqueteur que celui qui est contenu dans action.numEnqueteur
	public int calculEnqueteur(int l, int c){
		ArrayList<Enqueteur> enqueteurs;

		if (l==4){
			if (c==3){
				enqueteurs = plateau.grille[l-2][c-1].getEnqueteurs();
				for (Enqueteur e: enqueteurs){
					if (e.getPositionSurCarte()==Enqueteur.EST && e.getNumEnqueteur() != action.numEnqueteur ){
						enqueteurs.add(enqueteurs.size()-1,e);
						enqueteurs.remove(e);
						return e.getNumEnqueteur();
					}
				}
			} else {
				enqueteurs = plateau.grille[l-2][c].getEnqueteurs();
				for (Enqueteur e: enqueteurs){
					if (e.getPositionSurCarte()==Enqueteur.SUD && e.getNumEnqueteur() != action.numEnqueteur ){
						enqueteurs.add(enqueteurs.size()-1,e);
						enqueteurs.remove(e);
						return e.getNumEnqueteur();
					}
				}
			}
		}
		if (c==4){
			if (l==1){
				enqueteurs = plateau.grille[l-1][c-2].getEnqueteurs();
				for (Enqueteur e: enqueteurs){
					if (e.getPositionSurCarte()==Enqueteur.NORD && e.getNumEnqueteur() != action.numEnqueteur ){
						enqueteurs.add(enqueteurs.size()-1,e);
						enqueteurs.remove(e);
						return e.getNumEnqueteur();
					}
				}
			} else {
				enqueteurs = plateau.grille[l-2][c-2].getEnqueteurs();
				for (Enqueteur e: enqueteurs){
					if (e.getPositionSurCarte()==Enqueteur.EST && e.getNumEnqueteur() != action.numEnqueteur ){
						enqueteurs.add(enqueteurs.size()-1,e);
						enqueteurs.remove(e);
						return e.getNumEnqueteur();
					}
				}
			}
		}
		if (l==0){
			if (c==1){
				enqueteurs = plateau.grille[l][c-1].getEnqueteurs();
				for (Enqueteur e: enqueteurs){
					if (e.getPositionSurCarte()==Enqueteur.OUEST && e.getNumEnqueteur() != action.numEnqueteur ){
						enqueteurs.add(enqueteurs.size()-1,e);
						enqueteurs.remove(e);
						return e.getNumEnqueteur();
					}
				}
			} else {
				enqueteurs = plateau.grille[l][c-2].getEnqueteurs();
				for (Enqueteur e: enqueteurs){
					if (e.getPositionSurCarte()==Enqueteur.NORD && e.getNumEnqueteur() != action.numEnqueteur ){
						enqueteurs.add(enqueteurs.size()-1,e);
						enqueteurs.remove(e);
						return e.getNumEnqueteur();
					}
				}
			}
		}
		if (c==0){
			if (l==3){
				enqueteurs = plateau.grille[l-1][c].getEnqueteurs();
				for (Enqueteur e: enqueteurs){
					if (e.getPositionSurCarte()==Enqueteur.SUD && e.getNumEnqueteur() != action.numEnqueteur ){
						enqueteurs.add(enqueteurs.size()-1,e);
						enqueteurs.remove(e);
						return e.getNumEnqueteur();
					}
				}
			} else {
				enqueteurs = plateau.grille[l][c].getEnqueteurs();
				for (Enqueteur e: enqueteurs){
					if (e.getPositionSurCarte()==Enqueteur.OUEST && e.getNumEnqueteur() != action.numEnqueteur ){
						enqueteurs.add(enqueteurs.size()-1,e);
						enqueteurs.remove(e);
						return e.getNumEnqueteur();
					}
				}
			}
		}
		Configuration.instance().logger().info("Position invalide pour l'action en cours");
		return -1;
	}

	//Pre-Condition :action non nul
	//Post-Condition : Remet les valeurs par défauts de l'action
	public void reinitialiser(){
		action.reinitialiser();
	}


	//Pre-Condition :action non nul
	//Post-Condition : Modifie le plateau en cohérence avec les information du coup
	@Override
	boolean execute() {
		boolean res =false;
		switch(action.getAction()){
			case DEPLACER_JOKER:
			case DEPLACER_WATSON:
			case DEPLACER_SHERLOCK:
			case DEPLACER_TOBBY:
				res = deplacer(action.getNumEnqueteur(),action.getDeplacement(),1);
				break;
			case INNOCENTER_CARD:
				if (action.ia){
					res = innocenter(action.getCartePioche());
				} else {
					res = innocenter(1);
				}
				break;
			case ECHANGER_DISTRICT:
				//L'ordre des parametres est purement esthetique
				if(action.getPosition1().equals(action.getPosition2())) return false;
				res = echanger(action.getPosition1(), action.getPosition2());
				break;
			case ROTATION_DISTRICT:
				res = rotation(action.getPosition1(),action.getOrientationNew(),1);
				break;
			default:
				throw new IllegalStateException("Unexpected action");
		}

		if (res){
			plateau.actionPlus();
			plateau.getJeton(action.getNumAction()).setDejaJoue(true);
		}

		return res;
	}

	//Pre-Condition :action non nul
	//Post-Condition : Modifie le plateau en cohérence avec les information du coup
	@Override
	boolean desexecute() {
		boolean res=false;

		if(plateau.numAction==0 && plateau.getNumTour()>0) {
			plateau.resetJetons();
			plateau.annuleVerdict();
		}
		switch (action.getAction()){
			case DEPLACER_JOKER:
			case DEPLACER_WATSON:
			case DEPLACER_SHERLOCK:
			case DEPLACER_TOBBY:
				res = deplacer(action.getNumEnqueteur(), action.getDeplacement(),-1);
				break;
			case INNOCENTER_CARD:
				res = innocenter(-1);
				break;
			case ECHANGER_DISTRICT:
				//L'ordre des parametres est purement esthetique
				res = echanger(action.getPosition2(), action.getPosition1());
				break;
			case ROTATION_DISTRICT:
				res = rotation(action.getPosition1(),action.getOrientationOld(),-1);
				break;
			default:
				throw new IllegalStateException("Unexpected action");
		}

		if (res){
			plateau.actionMoins();
			plateau.getJeton(action.getNumAction()).setDejaJoue(false);
		}

		return res;
	}

	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
	}
}
