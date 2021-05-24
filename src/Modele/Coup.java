package Modele;

import java.awt.*;
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
		int carteRueX = card.getSuspect().getPosition().x;
		int carteRueY = card.getSuspect().getPosition().y;
		int sabliersJoueur = action.getJoueur().getSablier();
		int sabliersCarte = card.getSablier();
		plateau.grille[carteRueY][carteRueX].setOrientation(plateau.NSEO);
		action.getJoueur().setSablier(sabliersJoueur+sabliersCarte);
		return true;
	}

	public boolean rotation(Point position1, int orientation) {
		action.setOrientationOld(plateau.grille[position1.y][position1.x].getOrientation());
		plateau.grille[position1.y][position1.x].setOrientation(orientation);
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

		//On échange que l'orientation et le suspect
		carteRue1.setOrientation(orientation2);
		carteRue1.setSuspect(suspect2);

		carteRue2.setOrientation(tmpOrientation);
		carteRue2.setSuspect(tmpSuspect);

		return true;
	}

	//enqueteur = {0 = SHERLOCK,1 = WATSON,2 = TOBBY,3 = NONE (Only for Jack)}
	//deplacement = {0 (Only for Jack),1,2}
	//TODO add conventions to determinerCoup()

	public boolean deplacer(int numEnqueteur, int deplacement, int sens){
		boolean res = true;
		for(int i=0; i<deplacement;i++){
			res = avancer(numEnqueteur, sens);
		}
		return res;
	}
	private boolean avancer(int numEnqueteur, int sens) {
		Enqueteur enqueteur = plateau.enqueteurs.get(numEnqueteur);
		int positionX = enqueteur.getPosition().x;
		int positionY = enqueteur.getPosition().y;
		int posSurCarte = enqueteur.getPositionSurCarte();
		//CAS carte rue sur 1 des 4 coins de la grille
		if ((positionX==0 || positionX ==2) && (positionY==0 || positionY==2)) {
			switch (posSurCarte) {
				case 1:
					if (sens==-1){
						enqueteur.setPositionSurCarte(4);
						return true;
					}
					enqueteur.setPositionSurCarte(8);
					return true;
				case 2:
					if (sens==-1){
						enqueteur.setPositionSurCarte(8);
						return true;
					}
					enqueteur.setPositionSurCarte(4);
					return true;
				case 4:
					if (sens==-1){
						enqueteur.setPositionSurCarte(2);
						return true;
					}
					enqueteur.setPositionSurCarte(1);
					return true;
				case 8:
					if (sens==-1){
						enqueteur.setPositionSurCarte(1);
						return true;
					}
					enqueteur.setPositionSurCarte(2);
					return true;
			}
		}
		//CAS general
		plateau.grille[positionY][positionX].removeEnqueteur(enqueteur);
		switch (posSurCarte){
			//est sur l'ouest de la grille (0,2) ou (0,1)
			case 1:
				plateau.grille[positionY-sens][positionX].setEnqueteur(enqueteur);
				return true;
			//est sur l'est de la grille (2,0) ou (2,1)
			case 2:
				plateau.grille[positionY+sens][positionX].setEnqueteur(enqueteur);
				return true;
			//est sur le sud de la grille (2,2) ou (1,2)
			case 4:
				plateau.grille[positionY][positionX-sens].setEnqueteur(enqueteur);
				return true;
			//est sur le nord de la grille (0,0) ou (1,0)
			case 8:
				plateau.grille[positionY][positionX+sens].setEnqueteur(enqueteur);
				return true;
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
			case DEPLACER_WATSON:
			case DEPLACER_SHERLOCK:
			case DEPLACER_TOBBY:
				return deplacer(action.getNumEnqueteur(),action.getDeplacement(),1);
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
				break;
			case DEPLACER_TOBBY:
				break;
			case DEPLACER_SHERLOCK:
				break;
			case DEPLACER_WATSON:
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
						action.setOrientationNew(plateau.grille[l][c].orientation);
						break;
				}
				break;
		}
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
