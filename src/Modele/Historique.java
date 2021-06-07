package Modele;

import java.util.ArrayList;

public class Historique<E extends Commande> {
	public ArrayList<E> passe;
    ArrayList<E> futur;

	Historique() {
		passe = new ArrayList<>();
		futur = new ArrayList<>();
	}

	public boolean nouveau(E c) {
		if(c.execute()){
			passe.add(0,c);
			while (!futur.isEmpty())
				futur.remove(0);
			return true;
		}
		return false;
	}


	public boolean peutAnnuler() {
		return !passe.isEmpty();
	}

	E annuler() {
		if (peutAnnuler()) {
			E c = passe.get(0);
			if (c.desexecute()){
				futur.add(0,c);
				passe.remove(0);
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
