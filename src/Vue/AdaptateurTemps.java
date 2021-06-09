package Vue;


import Controle.CollecteurEvenements;
import Controle.IA;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurTemps implements ActionListener {
	IA ia;
	CollecteurEvenements control;

	public AdaptateurTemps(IA ia, CollecteurEvenements c) {
		this.ia = ia;
		control=c;
	}

	//Pre-Condition : Vide
	//Post-Condition : Joue le coupde l'IA une fois le temps écoulés
	@Override
	public void actionPerformed(ActionEvent e) {
		control.jouerCoupIA(ia.coupIA());
	}

}
