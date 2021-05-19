package Vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdaptateurSouris extends MouseAdapter {
	ElementPlateauG carre;
	CollecteurEvenements controle;

	AdaptateurSouris(ElementPlateauG e, CollecteurEvenements c) {
		controle = c;
		carre=e;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int c = e.getX() / carre.getTailleCase();
		int l = e.getY() / carre.getTailleCase();
		controle.clicSouris(l, c);
	}
}
