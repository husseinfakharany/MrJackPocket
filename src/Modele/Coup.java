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


	public void innoncenter(){
		CarteAlibi card = plateau.piocher();
		int carteRueX = card.getSuspect().getPosition().x;
		int carteRueY = card.getSuspect().getPosition().y;
		int sabliersJoueur = action.getJoueur().getSablier();
		int sabliersCarte = card.getSablier();
		plateau.grille[carteRueY][carteRueX].setOrientation(plateau.NSEO);
		action.getJoueur().setSablier(sabliersJoueur+sabliersCarte);
	}

	public void rotation() {
		plateau.grille[action.getPosition1().y][action.position1.x].setOrientation(action.getOrientation1());
	}



	public void echanger() {

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

	}
	
	//l'orientation du dectective les indice i et j de sa grille et la grille
	//TODO Voir si il faut mettre a jour toutes les cartes de la colone ou la ligne concernee et egalement considerer le fait d'avoir plusieurs enquetteur au meme endroit
	public void deplacer(int i,int j,CarteRue grille[][]) {
		 CarteRue tempon; 
		 int k=0;
		 tempon=grille[i][j];
		//deplacement coté Nord
		 if(i==0 && grille[i][j].getPositionEnqueteur()== 8) {
			
			 if((deplacement+j)<3 ) {
					while(i<3) {
					 
					 grille[i][j+deplacement].setPositionEnqueteur(tempon.getPositionEnqueteur());
					 grille[i][j+deplacement].setEnqueteur(tempon.getEnqueteur());
					 grille[i][j+deplacement].getEnqueteur().setNomPersonnage(tempon.getEnqueteur().getNomPersonnage());
				     
					 grille[i][j].setEnqueteur(null);
					 grille[i][j].setPositionEnqueteur(0b0000);
					 
				     i++;
				}
			 }	
			 if((deplacement+j)==3 ) {
				  k=0;
				  while(k<3) {
					 grille[k][j].setEnqueteur(null);
					 grille[k][j].setPositionEnqueteur(0b0000);
					 
					 grille[i][k].setPositionEnqueteur(0b0010);
					 grille[i][k].setEnqueteur(tempon.getEnqueteur());
					 grille[i][k].getEnqueteur().setNomPersonnage(tempon.getEnqueteur().getNomPersonnage());
					 
					 
					 k++;
				  }
			 }
			 if((deplacement+j)==4) {
				 k=0;
				 while(k<3) { 
					 
					 grille[k][j].setEnqueteur(null);
					 grille[k][j].setPositionEnqueteur(0b0000);
					 
					 grille[1][k].setPositionEnqueteur(tempon.getPositionEnqueteur());
					 grille[1][k].setEnqueteur(tempon.getEnqueteur());
					 grille[1][k].getEnqueteur().setNomPersonnage(tempon.getEnqueteur().getNomPersonnage());
				    
					 k++;
				 }
			 }
			 
		 }
		 
		 //deplacement coté Est
		 if(j==2  && grille[i][j].getPositionEnqueteur()== 2) {
			
			 if((deplacement+i)<3 ) {
				 j=0;
				 while(j<3) {
					 grille[i+deplacement][j].setPositionEnqueteur(tempon.getPositionEnqueteur());
					 grille[i+deplacement][j].setEnqueteur(tempon.getEnqueteur());
					 grille[i+deplacement][j].getEnqueteur().setNomPersonnage(tempon.getEnqueteur().getNomPersonnage());
					 
					 grille[i][j].setEnqueteur(null);
					 grille[i][j].setPositionEnqueteur(0b0000);
					 j++;
				 }
			 }
			 if((deplacement+i)==3 ) {
				 
				 k=0;
				 while(k<3) {
					 grille[i][k].setEnqueteur(null);
					 grille[i][k].setPositionEnqueteur(0b0000);
					 
					 grille[k][j].setPositionEnqueteur(0b0100);
					 grille[k][j].setEnqueteur(tempon.getEnqueteur());
					 grille[k][j].getEnqueteur().setNomPersonnage(tempon.getEnqueteur().getNomPersonnage());
					 k++;
				 } 
				
				
			 }
			 if((deplacement+j)==4) {
				 k=0;
				 while(k<0) {
					 grille[i][k].setEnqueteur(null);
					 grille[i][k].setPositionEnqueteur(0b0000);
					 
					 grille[k][1].setPositionEnqueteur(0b0100);
					 grille[k][1].setEnqueteur(tempon.getEnqueteur());
					 grille[k][1].getEnqueteur().setNomPersonnage(tempon.getEnqueteur().getNomPersonnage());
					 k++;
			        }
		       }
		 }
		 
		 //coté Sud
		 if(i==2 && grille[i][j].getPositionEnqueteur()==4) {
			 if((j-deplacement)>=0) {
				k=0;
				while(k<3){
					 grille[k][j-deplacement].setPositionEnqueteur(tempon.getPositionEnqueteur());
					 grille[k][j-deplacement].setEnqueteur(tempon.getEnqueteur());
					 grille[k][j-deplacement].getEnqueteur().setNomPersonnage(tempon.getEnqueteur().getNomPersonnage());

					 grille[k][j].setEnqueteur(null);
					 grille[k][j].setPositionEnqueteur(0b0000);
					 k++;
				 }
			 }
			 if((j-deplacement)==-1 ) {
				
				 while(k<3) {
					 
					 
					 grille[k][j].setEnqueteur(null);
					 grille[k][j].setPositionEnqueteur(0b0000);
					 grille[i][k].setPositionEnqueteur(0b0001);
					 grille[i][k].setEnqueteur(tempon.getEnqueteur());
					 grille[i][k].getEnqueteur().setNomPersonnage(tempon.getEnqueteur().getNomPersonnage());
					 
					
					 k++;
				 }
			 }
			 if((j-deplacement)==-2) {
				
				 while(k<3) {
					 grille[k][j].setEnqueteur(null);
					 grille[k][j].setPositionEnqueteur(0b0000);	 
				 
					 grille[1][k].setPositionEnqueteur(0b0001);
					 grille[1][k].setEnqueteur(tempon.getEnqueteur());
					 grille[1][k].getEnqueteur().setNomPersonnage(tempon.getEnqueteur().getNomPersonnage());
				     k++;
				 }
				 
			 }
			 
			 
		 }
		 //deplacement coté ouest
		 if(j==0 && grille[i][j].getPositionEnqueteur()==1) {
			 if((i-deplacement)>=0) {
				 while(j<3) {
					 grille[i-deplacement][j].setPositionEnqueteur(tempon.getPositionEnqueteur());
					 grille[i-deplacement][j].setEnqueteur(tempon.getEnqueteur());
					 grille[i-deplacement][j].getEnqueteur().setNomPersonnage(tempon.getEnqueteur().getNomPersonnage());
				     
					 grille[i][j].setEnqueteur(null);
					 grille[i][j].setPositionEnqueteur(0b0000);
					 
					 j++;
				 }
			 }
			 if((i-deplacement)==-1 ) {
				 while(k<3) {
					 grille[i][k].setEnqueteur(null);
					 grille[i][k].setPositionEnqueteur(0b0000);
					 grille[k][0].setPositionEnqueteur(0b1110);
					 grille[k][0].setEnqueteur(tempon.getEnqueteur());
					 grille[k][0].getEnqueteur().setNomPersonnage(tempon.getEnqueteur().getNomPersonnage());
					 
					
					 k++;
					 
				 }
			 }
			 if((i-deplacement)==-2) {
				 while(k<3) {
					 
					 grille[i][k].setEnqueteur(null);
					 grille[i][k].setPositionEnqueteur(0b0000);
					 
					 grille[k][1].setPositionEnqueteur(0b1110);
					 grille[k][1].setEnqueteur(tempon.getEnqueteur());
					 grille[k][1].getEnqueteur().setNomPersonnage(tempon.getEnqueteur().getNomPersonnage());
					 
					 
					 k++;
				 } 
			 }
		 }
		 
	} */
	
	
	public Actions getAction() {
		return action;
	}

	public void setAction(Actions action) {
		this.action = action;
	}

	public Point getPosition1() {
		return position1;
	}

	public void setPosition1(Point position1) {
		this.position1 = position1;
	}

	public Point getPosition2() {
		return position2;
	}

	public void setPositionQuartier2(Point position2) {
		this.position2 = position2;
	}

	public int getOrientation1() {
		return orientation1;
	}

	public void setOrientation1(int orientation) {
		this.orientation1 = orientation;
	}

	public int getDeplacement() {
		return deplacement;
	}

	public void setDeplacement(int deplacement) {
		this.deplacement = deplacement;
	}


	public int getOrientation2() {
		return orientation2;
	}


	public void setOrientation2(int orientation2) {
		this.orientation2 = orientation2;
	}


	@Override
	void execute() {
		switch(action){
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
