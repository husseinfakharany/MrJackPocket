package Modele;

import java.awt.*;
/*
**Coup**:
Type Action
Methode qui execute le coup
 */
public class Coup extends Commande{
	
	private Actions action;
    private Point position1;
    private Point position2;
	private Plateau plateau;
    private int orientation1;
    private int orientation2;
    private int deplacement =0;

   
    //entier : 1 ou 2
	
    //CAS Où ACTION EQUIVEAUT à ECHANGE OU ROTATION
	//TODO Modify constructor
	public Coup(Plateau plateau, Actions action,Point position1,Point position2,int orientation1,int orientation2,int deplacement) {

		this.plateau = plateau;
		this.action = action;
		this.position1 = position1;
		this.position2 = position2;
		this.orientation1 = orientation1;
		this.orientation2 = orientation2;
	    this.deplacement=deplacement;
	
	}

	/*
	public void rotation(int orientation,Point position) {

		plateau.grille[position.y][ position.x].setOrientation(orientation);

	}

	public void echanger(int orientation1,int orientation2, Point position1,Point position2) {
		CarteRue tmp = plateau.grille[position1.y][position1.x];
		plateau.grille[position1.y][position1.x] =plateau.grille[position2.y][position2.x];
		plateau.grille[position1.y][position1.x].setPositionEnqueteur(tmp.getPositionEnqueteur());
		tmp.setPositionEnqueteur(plateau.grille[position2.y][position2.x].getPositionEnqueteur());
		plateau.grille[position1.y][position1.x].setOrientation(orientation2);
		plateau.grille[position2.y][position2.x] = tmp;
		plateau.grille[position2.y][position2.x].setOrientation(orientation1);

	}
	
		//l'orientation du dectective les indice i et j de sa grille et la grille
	//TODO Voir si il faut metre ajour toutes les cartes de la colone ou la ligne concernee et egalement considerer le fait d'avoir plusieurs enquetteur au meme endroit
	public void deplacer(int i,int j,CarteRue grille[][]) {
		
		//deplacement coté Nord
		 if(i==0 && grille[i][j].getPositionEnqueteur()== 8) {
			 if((deplacement+j)<3 ) {
				 grille[i][j+deplacement].setPositionEnqueteur(grille[i][j].getPositionEnqueteur());
				 grille[i][j+deplacement].setEnqueteur(grille[i][j].getEnqueteur());
				 grille[i][j+deplacement].getEnqueteur().setNomPersonnage(grille[i][j].getEnqueteur().getNomPersonnage());
			     
				 grille[i][j].setEnqueteur(null);
				 grille[i][j].setPositionEnqueteur(0b0000);
			 }
			 if((deplacement+j)==3 ) {
				 grille[i][2].setPositionEnqueteur(0b0010);
				 grille[i][2].setEnqueteur(grille[i][j].getEnqueteur());
				 grille[i][2].getEnqueteur().setNomPersonnage(grille[i][j].getEnqueteur().getNomPersonnage());
				 
				 grille[i][j].setEnqueteur(null);
				 grille[i][j].setPositionEnqueteur(0b0000);
				
			 }
			 if((deplacement+j)==4) {
				 grille[1][2].setPositionEnqueteur(grille[i][j].getPositionEnqueteur());
				 grille[1][2].setEnqueteur(grille[i][j].getEnqueteur());
				 grille[1][2].getEnqueteur().setNomPersonnage(grille[i][j].getEnqueteur().getNomPersonnage());
				 
				 
				 
				 grille[i][j].setEnqueteur(null);
				 grille[i][j].setPositionEnqueteur(0b0000);
				 //TODO GERER ORIENTATION ENQUETEUR
			 }
			 
		 }
		 
		 //deplacement coté Est
		 if(j==2  && grille[i][j].getPositionEnqueteur()== 2) {
			 
			 if((deplacement+i)<3 ) {
				 grille[i+deplacement][j].setPositionEnqueteur(grille[i][j].getPositionEnqueteur());
				 grille[i+deplacement][j].setEnqueteur(grille[i][j].getEnqueteur());
				 grille[i+deplacement][j].getEnqueteur().setNomPersonnage(grille[i][j].getEnqueteur().getNomPersonnage());
				 
				 grille[i][j].setEnqueteur(null);
				 grille[i][j].setPositionEnqueteur(0b0000);
			 }
			 if((deplacement+i)==3 ) {
				 grille[2][2].setPositionEnqueteur(0b0100);
				 grille[2][2].setEnqueteur(grille[i][j].getEnqueteur());
				 grille[2][2].getEnqueteur().setNomPersonnage(grille[i][j].getEnqueteur().getNomPersonnage());
				 
				 grille[i][j].setEnqueteur(null);
				 grille[i][j].setPositionEnqueteur(0b0000);
				
			 }
			 if((deplacement+j)==4) {
				 grille[2][1].setPositionEnqueteur(0b0100);
				 grille[2][1].setEnqueteur(grille[i][j].getEnqueteur());
				 grille[2][1].getEnqueteur().setNomPersonnage(grille[i][j].getEnqueteur().getNomPersonnage());
				 
				 grille[i][j].setEnqueteur(null);
				 grille[i][j].setPositionEnqueteur(0b0000);
				
			 }
		 }
		 
		 //coté Ouest
		 if(i==2 && grille[i][j].getPositionEnqueteur()==4) {
			 if((j-deplacement)>=0) {
				 grille[i][j-deplacement].setPositionEnqueteur(grille[i][j].getPositionEnqueteur());
				 grille[i][j-deplacement].setEnqueteur(grille[i][j].getEnqueteur());
				 grille[i][j-deplacement].getEnqueteur().setNomPersonnage(grille[i][j].getEnqueteur().getNomPersonnage());
			     
				 grille[i][j].setEnqueteur(null);
				 grille[i][j].setPositionEnqueteur(0b0000);
			 }
			 if((j-deplacement)==-1 ) {
				 grille[i][0].setPositionEnqueteur(0b0001);
				 grille[i][0].setEnqueteur(grille[i][j].getEnqueteur());
				 grille[i][0].getEnqueteur().setNomPersonnage(grille[i][j].getEnqueteur().getNomPersonnage());
				 
				 grille[i][j].setEnqueteur(null);
				 grille[i][j].setPositionEnqueteur(0b0000);
				
			 }
			 if((j-deplacement)==-2) {
				 grille[1][0].setPositionEnqueteur(0b0001);
				 grille[1][0].setEnqueteur(grille[i][j].getEnqueteur());
				 grille[1][0].getEnqueteur().setNomPersonnage(grille[i][j].getEnqueteur().getNomPersonnage());
				 
				 grille[i][j].setEnqueteur(null);
				 grille[i][j].setPositionEnqueteur(0b0000);
				 
			 }
			 
			 
		 }
		 //deplacement coté ouest
		 if(j==0 && grille[i][j].getPositionEnqueteur()==1) {
			 if((i-deplacement)>=0) {
				 grille[i-deplacement][j].setPositionEnqueteur(grille[i][j].getPositionEnqueteur());
				 grille[i-deplacement][j].setEnqueteur(grille[i][j].getEnqueteur());
				 grille[i-deplacement][j].getEnqueteur().setNomPersonnage(grille[i][j].getEnqueteur().getNomPersonnage());
			     
				 grille[i][j].setEnqueteur(null);
				 grille[i][j].setPositionEnqueteur(0b0000);
			 }
			 if((i-deplacement)==-1 ) {
				 grille[0][j].setPositionEnqueteur(0b1110);
				 grille[0][j].setEnqueteur(grille[i][j].getEnqueteur());
				 grille[0][j].getEnqueteur().setNomPersonnage(grille[i][j].getEnqueteur().getNomPersonnage());
				 
				 grille[i][j].setEnqueteur(null);
				 grille[i][j].setPositionEnqueteur(0b0000);
				
			 }
			 if((i-deplacement)==-2) {
				 grille[0][1].setPositionEnqueteur(0b1110);
				 grille[0][1].setEnqueteur(grille[i][j].getEnqueteur());
				 grille[0][1].getEnqueteur().setNomPersonnage(grille[i][j].getEnqueteur().getNomPersonnage());
				 
				 grille[i][j].setEnqueteur(null);
				 grille[i][j].setPositionEnqueteur(0b0000);
				 
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
