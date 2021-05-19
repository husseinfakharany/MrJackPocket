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
		int c = e.getX() / (carre.getTailleCase()+carre.getOffsetX());
		int l = e.getY() / (carre.getTailleCase()+ carre.getOffsetY());
		controle.clicSouris(l, c);
	}
}
