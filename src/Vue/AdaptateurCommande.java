package Vue;

import Controle.CollecteurEvenements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurCommande implements ActionListener {
	CollecteurEvenements controle;
	String commande;

	AdaptateurCommande(CollecteurEvenements c, String com) {
		controle = c;
		commande = com;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controle.commandeMenu(commande);
	}

}
