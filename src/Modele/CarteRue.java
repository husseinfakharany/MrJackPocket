package Modele;

/*
**Carte Rue: **
Orientation: 4 bits int (Nord, Sud, Est, Ouest)
Position Inspecteur (1 = Ouest, Est = 2, Sud = 4, Nord = 8, 0 si pas sur carte)
Personnage suspect
Methode qui cache le personnage
Boolean dejaRetourne
*/

import Global.Configuration;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class CarteRue implements Cloneable {
    public int orientation; //4 bits de poids faible indiquent les orientations des rues (N,S,E,O) - 3/4 activés (si 1110 alors Mur|-)
    public Point position;
    public ArrayList<Enqueteur> enqueteurs; //liste des enqueteurs sur cette carte (seulement pour les cartes des bords)
    public Suspect suspect;
    public boolean dejaTourne;

    public CarteRue(Point position, Suspect suspect){
        Random rand = new Random(Jeu.getSeed());
        //On choisi une orientation des orientations possibles
        orientation = Plateau.getOrientationsRues().get(rand.nextInt(Plateau.getOrientationsRues().size()));
        enqueteurs = new ArrayList<>();
        this.position = position;
        this.suspect = suspect;
        this.suspect.setPosition(this.position);
        this.dejaTourne = false;
    }

	@Override
	public CarteRue clone() throws CloneNotSupportedException {
		CarteRue copy;
		try {
			copy = (CarteRue) super.clone();

		} catch (CloneNotSupportedException e) {
			Configuration.instance().logger().severe("Bug interne: CarteRue non clonable");
			return null;
		}
		copy.orientation = orientation;
		copy.position = (Point) position.clone();
		copy.enqueteurs = new ArrayList<>();
		for(Enqueteur e : enqueteurs) {
			copy.enqueteurs.add(e.clone());
		}
		copy.suspect = suspect.clone();
		copy.dejaTourne = dejaTourne;
		return copy;
	}

	public int getOrientation() {
		return orientation;
	}

	void setOrientation(int orientation) {
		this.orientation = orientation;
		this.suspect.setOrientation(this.orientation);
	}

	public ArrayList<Enqueteur> getEnqueteurs() {
		return enqueteurs;
	}

	public void setEnqueteur(Enqueteur enqueteur) {
    	if (!enqueteurs.contains(enqueteur)) {
			this.enqueteurs.add(enqueteur);
		}
    	enqueteur.setPosition(this.position);
	}

	public void removeEnqueteur(Enqueteur enqueteur){
		enqueteurs.remove(enqueteur);
	}

	//renvoit la position de l'enqueteur passé en paramètre sur la carte
	//si l'enqueteur est sur une carte de la meme ligne ou colonne il n'est pas ABSENT
	public int getPosEnqueteur(Enqueteur enqueteur){
		if (enqueteur.getPosition().x == position.x){
			return enqueteur.getPositionSurCarte();
		} else if (enqueteur.getPosition().y == position.y){
			return enqueteur.getPositionSurCarte();
		} else {
			return Enqueteur.ABSENT;
		}
	}
	
	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}
	
	public Suspect getSuspect() {
		return suspect;
	}

	public void setSuspect(Suspect suspect) {
		this.suspect = suspect;
		this.suspect.setOrientation(this.orientation);
	}

	public boolean getDejaTourne(){
    	return dejaTourne;
	}

	public void setDejaTourne(Boolean dejaTourne){
    	this.dejaTourne = dejaTourne;
	}

	public boolean getInnocente(){
    	return suspect.getInnocente();
	}
}
