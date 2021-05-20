package Modele;

import java.util.ArrayList;


//TODO compltete Historique and use intermediate class that extends Historique and called by Jeu

public class Historique<E extends Commande> {
	//ArrayList<E> passe, futur;

	/*
	Historique() {
		passe = new ArrayList<>();
		futur = new ArrayList<>();
	}*/

	public void nouveau(E c) {
		//passe.add(c);
		c.execute();
		/*
		while (!futur.isEmpty())
			futur.get(0);
			futur.extraitTete();

		 */
	}

	/*
	public boolean peutAnnuler() {
		return !passe.estVide();
	}

	E annuler() {
		if (peutAnnuler()) {
			E c = passe.extraitTete();
			c.desexecute();
			futur.insereTete(c);
			return c;
		} else {
			return null;
		}
	}

	public boolean peutRefaire() {
		return !futur.estVide();
	}

	E refaire() {
		if (peutRefaire()) {
			E c = futur.extraitTete();
			c.execute();
			passe.insereTete(c);
			return c;
		} else {
			return null;
		}
	}
	*/
}
