package Modele;

import java.util.ArrayList;
import java.util.List;


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

public class Joueur {
	
	private List<CarteAlibi> cardList = new ArrayList<>();
	private int sablier;
	private boolean isJack = false;
	private boolean win = false;
	private boolean turn = false;
	private String name;

	// Constructeur 
    //	name: nom du joueur soit jack soit dectective
	//  isJack
	//sablier
	public Joueur(boolean isJack, String name, int sablier,boolean win,boolean turn) {
		setJack(isJack);
		setName(name);
		setSablier(sablier);
		setWinner(win);
		setTurn(turn);
	}

	public void ajouterCarte(CarteAlibi card) {
		System.out.println(" ajout carte : " + card);
		cardList.add(card);
	}

	public void supprimerCarte(CarteAlibi card) {
		System.out.println(" suppression carte : " + card);
		cardList.remove(card);
	}

	// Getters and Setters
	public List<CarteAlibi> getCardList() {
		return cardList;
	}

	public void setCardList(List<CarteAlibi> cardList) {
		this.cardList = cardList;
	}

	public int getSablier() {
		return sablier;
	}

	public void setSablier(int sablier) {
		this.sablier = sablier;
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
