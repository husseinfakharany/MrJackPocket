package Modele;

import java.awt.*;

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

	public void retournerCarteRue(CarteRue[][] grille) {
		if(grille[position.y][position.x].getSuspect().getCouleur() == GRIS) grille[position.y][position.x].setOrientation(Plateau.NSEO);
	}

}
