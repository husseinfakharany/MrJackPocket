package Modele;

import Global.Configuration;

import java.awt.*;
import java.util.ArrayList;

import static Modele.SuspectCouleur.*;

/*
**Suspect**:
Nom Personnage
Position
Boolean caché ou pas
 */
public class Suspect {
	
    private SuspectNom nomPersonnage;
    private SuspectCouleur couleur;
    private Point position; //position sur la grille
	private int orientation; //orientation sur la grille
	private int orientationRecto; //orientation sur la grille
    private Boolean innocente;
    private Boolean pioche;
    private Boolean isJack;

    public Suspect(SuspectNom nomPerso, Point position){
        nomPersonnage = nomPerso;
        this.setPosition(position);
        this.innocente = false;
        this.pioche = false;
		switch (nomPersonnage){
			case MADAME:
				couleur = ROSE;
				break;
			case JOHN_PIZER:
				couleur = BLANC;
				break;
			case JOHN_SMITH:
				couleur = JAUNE;
				break;
			case JEREMY_BERT:
				couleur = ORANGE;
				break;
			case JOSEPH_LANE:
				couleur = GRIS;
				break;
			case MISS_STEALTHY:
				couleur = VERT;
				break;
			case WILLIAM_GULL:
				couleur = VIOLET;
				break;
			case SERGENT_GOODLEY:
				couleur = NOIR;
				break;
			case INSPECTOR_LESTRADE:
				couleur = BLEU;
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + nomPersonnage);
		}
		isJack = false;
    }

	@Override
	public Suspect clone() throws CloneNotSupportedException {
		Suspect copy;
		try {
			copy = (Suspect) super.clone();

		} catch (CloneNotSupportedException e) {
			Configuration.instance().logger().severe("Bug interne: CarteRue non clonable");
			return null;
		}
		copy.nomPersonnage = nomPersonnage;
		copy.couleur = couleur;
		copy.position = position;
		copy.orientation = orientation; //orientation sur la grille
		copy.innocente = innocente;
		copy.pioche = pioche;
		copy.isJack = isJack;
		return copy;
	}

   public SuspectNom getNomPersonnage(){
    	return nomPersonnage;
    }
   
   public void setNomPersonnage(SuspectNom nom ) {
	   nomPersonnage = nom;
   }

	public Point getPosition() {
		return position;
	}
	
	public void setPosition(Point position) {
		this.position = position;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation){
    	this.orientation = orientation;
	}
	
	public Boolean getInnocente() {
		return innocente;
	}
	
	public void setInnocente(Boolean innocente) {
		this.innocente = innocente;
	}
	
	public Boolean getPioche() {
		return pioche;
	}
	
	public void setPioche(Boolean pioche) {
		this.pioche = pioche;
	}

	public SuspectCouleur getCouleur(){
    	return couleur;
	}

	public void setIsJack(Boolean isJack){
    	this.isJack = isJack;
    }

	public Boolean getIsJack(){
    	return isJack;
	}

	public void retournerCarteRue(CarteRue[][] grille, int type, int orientation) {
		if(grille[position.y][position.x].getSuspect().getCouleur() == GRIS && type==1){
			orientationRecto = grille[position.y][position.x].getOrientation();
			grille[position.y][position.x].setOrientation(Plateau.NSEO);

		}
		if(grille[position.y][position.x].getSuspect().getCouleur() == GRIS && type==-1) grille[position.y][position.x].setOrientation(orientationRecto);
	}

	public void innoceter(CarteRue[][] grille, ArrayList<Suspect> suspectsInnocete){
		setInnocente(true);
		retournerCarteRue(grille,1,this.orientation);
		if (!suspectsInnocete.contains(this)){
			suspectsInnocete.add(this);
		}
	}
	public void suspecter(CarteRue[][] grille, ArrayList<Suspect> suspectsInnocete){
		setInnocente(false);
		retournerCarteRue(grille,-1,this.orientation);
		suspectsInnocete.remove(this);
	}


	public void rendreSuspect(CarteRue[][] grille, ArrayList<Suspect> suspectsInnocete, int orientation){
    	setInnocente(false);
    	retournerCarteRue(grille,-1,orientation);
		while(suspectsInnocete.contains(this)){
			suspectsInnocete.remove(this);
		}
	}

}
