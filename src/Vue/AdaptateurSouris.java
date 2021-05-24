package Vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdaptateurSouris extends MouseAdapter {
	ElementPlateauG elementG;
	CollecteurEvenements controle;

	public final static int district = 1;
	public final static int jetons = 2;
	public final static int pioche = 3;

	AdaptateurSouris(ElementPlateauG e, CollecteurEvenements c) {
		controle = c;
		elementG =e;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int c = e.getX() / (elementG.getTailleCase()+ elementG.getOffsetX());
		int l = e.getY() / (elementG.getTailleCase()+ elementG.getOffsetY());
		switch(elementG.getType()){
			case district:
				System.out.println("District : ( " +l +" , "+c+" )");
				break;
			case jetons:
				System.out.println("Jetons : ( " +l +" , "+c+" )");
				break;
			case pioche:
				System.out.println("Pioche : ( " +l +" , "+c+" )");
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + elementG.getType());
		}
	}
}
