package Vue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurCombobox implements ActionListener {
	CollecteurEvenements controle;
	String commande;

	AdaptateurCombobox(CollecteurEvenements c) {
		controle = c;
	}

	//Pre-Condition : Vide
	//Post-Condition : Execute les actions associés aux boutons associés
	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox)e.getSource();
		String champ = (String)cb.getSelectedItem();
		controle.commandeMenu(champ);
	}

}
