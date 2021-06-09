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
public class Suspect implements Cloneable {
	
    private SuspectNom nomPersonnage;
    private SuspectCouleur couleur;
    private Point position; //position sur la grille
	private int orientation; //orientation sur la grille
	private int orientationRecto; //orientation sur la grille
    private Boolean innocente;
    private Boolean innocenteParPioche;
    private Boolean isJack;

    public Suspect(SuspectNom nomPerso, Point position){
        nomPersonnage = nomPerso;
        this.setPosition(position);
        this.innocente = false;
        this.innocenteParPioche = false;
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
			Configuration.instance().logger().severe("Bug interne: Suspect non clonable");
			return null;
		}
		copy.nomPersonnage = nomPersonnage;
		copy.couleur = couleur;
		copy.position = position;
		copy.orientation = orientation; //orientation sur la grille
		copy.innocente = innocente;
		copy.innocenteParPioche = innocenteParPioche;
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
	
	public Boolean getInnocenteParPioche() {
		return innocenteParPioche;
	}
	
	public void setInnocenteParPioche(Boolean pioche) {
		this.innocenteParPioche = pioche;
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

	//Pre-Condition : Vide
	//Post-Condition : Innocente le personnage present sur la carte suite à l'appel à témoin
	public void retournerCarteRue(CarteRue[][] grille, int type) {
		if(grille[position.y][position.x].getSuspect().getCouleur() == GRIS && type==1){
			orientationRecto = grille[position.y][position.x].getOrientation();
			grille[position.y][position.x].setOrientation(Plateau.NSEO);

		}
		if(grille[position.y][position.x].getSuspect().getCouleur() == GRIS && type==-1) grille[position.y][position.x].setOrientation(orientationRecto);
	}

	//Pre-Condition : Vide
	//Post-Condition : Innocente le personnage present sur la carte suite à l'appel à témoin
	public void innocenterVerdict(CarteRue[][] grille, ArrayList<Suspect> suspectsInnocente){
		if(!innocente && !suspectsInnocente.contains(this)){
			setInnocente(true);
			suspectsInnocente.add(this);
			retournerCarteRue(grille,1);
		}
	}

	//Pre-Condition : Vide
	//Post-Condition : Innocente le personnage present sur la carte suite à la pioche d'une carte alibi
	public void innocenterPioche(CarteRue[][] grille, ArrayList<Suspect> suspectsInnocente){
    	if(!innocente && !suspectsInnocente.contains(this)){
			setInnocente(true);
			setInnocenteParPioche(true);
			suspectsInnocente.add(this);
			retournerCarteRue(grille,1);
		}
	}

	//Pre-Condition : Vide
	//Post-Condition : Utilisé pour annuler verdict
	public void suspecterVerdict(CarteRue[][] grille, ArrayList<Suspect> suspectsInnocente){
    	if(innocente && !innocenteParPioche){
			setInnocente(false);
			retournerCarteRue(grille,-1);
			while(suspectsInnocente.contains(this)){
				suspectsInnocente.remove(this);
			}
		}
	}

	//Pre-Condition : Vide
	//Post-Condition : Utilisé pour annuler pioche
	public void suspecterPioche(CarteRue[][] grille, ArrayList<Suspect> suspectsInnocente){
    	if(innocente && innocenteParPioche){
    		setInnocente(false);
    		setInnocenteParPioche(false);
    		retournerCarteRue(grille,-1);
			while(suspectsInnocente.contains(this)){
				suspectsInnocente.remove(this);
			}
		}

	}
}
