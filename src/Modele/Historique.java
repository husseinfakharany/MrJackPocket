package Modele;

import java.util.ArrayList;


//TODO compltete Historique and use intermediate class that extends Historique and called by Jeu

public class Historique<E extends Commande> {
	ArrayList<E> passe, futur;


	Historique() {
		passe = new ArrayList<>();
		futur = new ArrayList<>();
	}

	public void nouveau(E c) {
		if(c.execute()){
			passe.add(0,c);
			while (!futur.isEmpty())
				futur.remove(0);
		}
	}


	public boolean peutAnnuler() {
		return !passe.isEmpty();
	}

	E annuler() {
		if (peutAnnuler()) {
			E c = passe.remove(0);
			if (c.desexecute()){
				futur.add(0,c);
				return c;
			} else {
				passe.add(0,c);
				return null;
			}
		} else {
			return null;
		}
	}

	public boolean peutRefaire() {
		return !futur.isEmpty();
	}

	E refaire() {
		if (peutRefaire()) {
			E c = futur.remove(0);
			c.execute();
			passe.add(0,c);
			return c;
		} else {
			return null;
		}
	}
}
