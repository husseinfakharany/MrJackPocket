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
    private Boolean carteCache;
    private Boolean pioche;

    //TODO implement getters and setters
    Suspect(SuspectNom nomPerso, Point position){
        nomPersonnage = nomPerso;
        this.setPosition(position);
        setCarteCache(false);
        setPioche(false);
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
	
	public Boolean getCarteCache() {
		return carteCache;
	}
	
	public void setCarteCache(Boolean carteCache) {
		this.carteCache = carteCache;
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
   
}
