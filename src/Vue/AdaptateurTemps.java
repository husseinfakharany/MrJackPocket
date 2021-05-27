package Vue;


import Controle.CollecteurEvenements;
import Controle.IA;
import Modele.Jeu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurTemps implements ActionListener {
	IA ia;
	Jeu jeu;
	CollecteurEvenements control;

	public AdaptateurTemps(IA ia, Jeu j, CollecteurEvenements c) {
		this.ia = ia;
		jeu = j;
		control=c;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		control.jouerCoupIA(ia.coupIA(jeu));
	}

}
