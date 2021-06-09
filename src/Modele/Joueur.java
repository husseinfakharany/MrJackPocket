package Modele;

import Global.Configuration;

import java.util.ArrayList;


/*
**Joueur**:
Boolean isJack
Boolean sonTour
Boolean aGagne
String playerName jack ou detective
if Jack: Nom Personnage suspect
Total sabliers
Liste cartes alibi piochés
 */

public class Joueur implements Cloneable{
	
	private ArrayList<CarteAlibi> cardList;
	private int sablier;
	private int sablierVisibles;
	private int sablierCaches;
	private boolean isJack = false;
	private boolean win = false;
	private boolean turn = false;
	private String name;

	// Constructeur 
    //	name: nom du joueur soit jack soit dectective
	//  isJack
	//sablier
	public Joueur(boolean isJack, String name, int sabliersCaches, int sabliersVisibles, boolean win,boolean turn) {
		cardList = new ArrayList<>();
		setJack(isJack);
		setName(name);
		setWinner(win);
		setTurn(turn);
		setSablier(sabliersCaches,sabliersVisibles);
	}

	//Pre-Condition : Vide
	//Post-Condition : Ajoute une carte dans la main du joueur
	public void ajouterCarte(CarteAlibi card) {
		//System.out.println(" ajout carte : " + card);
		cardList.add(card);
	}

	//Pre-Condition : cardList non vide
	//Post-Condition : Supprime une carte dans la main du joueur
	public void supprimerCarte(CarteAlibi card) {
		//System.out.println(" suppression carte : " + card);
		cardList.remove(card);
	}

	@Override
	public Joueur clone() throws CloneNotSupportedException {
		Joueur copy;
		try {
			copy = (Joueur) super.clone();

		} catch (CloneNotSupportedException e) {
			Configuration.instance().logger().severe("Bug interne : Joueur non clonable");
			return null;
		}
		copy.cardList = (ArrayList<CarteAlibi>) cardList.clone();
		copy.sablier = sablier;
		copy.sablierVisibles = sablierVisibles;
		copy.sablierCaches = sablierCaches;
		copy.isJack = false;
		copy.win = false;
		copy.turn = false;
		copy.name = name;
		return copy;
	}

	// Getters and Setters
	public ArrayList<CarteAlibi> getCardList() {
		return cardList;
	}

	public void setCardList(ArrayList<CarteAlibi> cardList) {
		this.cardList = cardList;
	}

	public int getSablier() {
		return sablier;
	}

	public int getSablierVisibles(){
		return sablierVisibles;
	}

	public int getSablierCaches(){
		return sablierCaches;
	}

	public void setSablier(int sablierCaches, int sablierVisibles) {
		this.sablier = sablierCaches+sablierVisibles;
		this.sablierCaches = sablierCaches;
		this.sablierVisibles = sablierVisibles;
	}

	public void setSablierCaches(int sabliersCaches) {
		this.sablierCaches = sabliersCaches;
		this.sablier = this.sablierVisibles+this.sablierCaches;
	}

	public void setSablierVisibles(int sabliersVisibles) {
		this.sablierVisibles = sabliersVisibles;
		this.sablier = this.sablierCaches+this.sablierVisibles;
	}


	public boolean isJack() {
		return isJack;
	}

	public void setJack(boolean isJack) {
		this.isJack = isJack;
	}
	public boolean getWinner() {
		return win;
	}

	public void setWinner(boolean win) {
		this.win = win;
	}
    
	public boolean getTurn() {
		return turn;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Console toString
	public String toString() {
		return getName();
	}

	
}
