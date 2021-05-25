package Vue;

import Controle.CollecteurEvenements;

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
				controle.commandeDistrict(l,c);
				break;
			case jetons:
				System.out.println("Jetons : ( " +l +" , "+c+" )");
				if(l==0){
					if(c==0) controle.commandeJeu("jetonA");

					if(c==1)controle.commandeJeu("jetonB");
				}
				if(l==1){
					if(c==0)controle.commandeJeu("jetonC");
					if(c==1) controle.commandeJeu("jetonD");
				}
				break;
			case pioche:
				controle.commandeJeu("pioche");
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + elementG.getType());
		}
	}
}
