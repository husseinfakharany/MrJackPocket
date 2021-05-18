package Modele;

import java.awt.*;

/*
**Suspect**:
Nom Personnage
Position
Boolean caché ou pas
 */
public class Suspect {
	
    private SuspectNom nomPersonnage;
    private Point position; //position sur la grille
    private Boolean carteCache;
    private Boolean pioche;

    //TODO implement getters and setters
    Suspect(SuspectNom nomPerso, Point position){
        nomPersonnage = nomPerso;
        this.setPosition(position);
        setCarteCache(false);
        setPioche(false);
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
   
   
}
