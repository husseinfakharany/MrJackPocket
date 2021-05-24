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

   
    //entier : 1 ou 2
	
    //CAS Où ACTION EQUIVEAUT à ECHANGE OU ROTATION
	//TODO Modify constructor
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

	public boolean rotation() {
		plateau.grille[action.getPosition1().y][action.position1.x].setOrientation(action.getOrientation1());
		return true;
	}



	public boolean echanger() {

		CarteRue carteRue1 = plateau.grille[action.getPosition1().y][action.getPosition1().x];
		int orientation1 = carteRue1.getOrientation();
		Suspect suspect1 = carteRue1.getSuspect();

		CarteRue carteRue2 = plateau.grille[action.getPosition2().y][action.getPosition2().x];
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


	//TODO check
	//l'orientation du dectective les indice i et j de sa grille et la grille
	//TODO Voir si il faut mettre a jour toutes les cartes de la colone ou la ligne concernee et egalement considerer le fait d'avoir plusieurs enquetteur au meme endroit
	//enqueteur = {0 = SHERLOCK,1 = WATSON,2 = TOBBY,3 = NONE (Only for Jack)}
	//deplacement = {0 (Only for Jack),1,2}
	//TODO add conventions to determinerCoup()
	public boolean deplacer(int numEnqueteur, int deplacement) {
		Enqueteur enqueteur = plateau.enqueteurs.get(numEnqueteur);
		int positionX = enqueteur.getPosition().x;
		int positionY = enqueteur.getPosition().y;
		int posSurCarte = enqueteur.getPositionSurCarte();
		if (deplacement==0){
			return true;
		} else {
			//if cas limite
			if ((positionX==0 || positionX ==2) && (positionY==0 || positionY==2)) {
				switch (posSurCarte) {
					case 1:
						enqueteur.setPositionSurCarte(8);
						return true;
					case 2:
						enqueteur.setPositionSurCarte(4);
						return true;
					case 4:
						enqueteur.setPositionSurCarte(1);
						return true;
					case 8:
						enqueteur.setPositionSurCarte(2);
						return true;
				}
			}
			//if cas general
			plateau.grille[positionY][positionX].removeEnqueteur(enqueteur);
			switch (posSurCarte){
				//est sur l'ouest de la grille (0,2) ou (0,1)
				case 1:
					plateau.grille[positionY--][positionX].setEnqueteur(enqueteur);
					return true;
				//est sur l'est de la grille (2,0) ou (2,1)
				case 2:
					plateau.grille[positionY++][positionX].setEnqueteur(enqueteur);
					return true;
				//est sur le sud de la grille (2,2) ou (1,2)
				case 4:
					plateau.grille[positionY][positionX--].setEnqueteur(enqueteur);
					return true;
				//est sur le nord de la grille (0,0) ou (1,0)
				case 8:
					plateau.grille[positionY][positionX++].setEnqueteur(enqueteur);
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

	//TODO implement execute
	@Override
	void execute() {
		switch(action.getAction()){
			case DEPLACER_JOKER:
				break;
			case DEPLACER_TOBBY:
				break;
			case DEPLACER_SHERLOCK:
				break;
			case DEPLACER_WATSON:
				break;
			case INNOCENTER_CARD:
				break;
			case ECHANGER_DISTRICT:
				break;
			case ROTATION_DISTRICT:
				break;
			default:
				throw new IllegalStateException("Unexecpted action");
		}

	}

	@Override
	void desexecute() {
	}
}
