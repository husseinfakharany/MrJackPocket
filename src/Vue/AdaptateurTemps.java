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

	@Override
	public void actionPerformed(ActionEvent e) {
		control.jouerCoupIA(ia.coupIA());
	}

}
