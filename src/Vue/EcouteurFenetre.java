package Vue;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class EcouteurFenetre implements ComponentListener {
    InterfaceGraphique interG;

    EcouteurFenetre(InterfaceGraphique ig){
        interG = ig;
    }
    @Override
    public void componentResized(ComponentEvent e) {
        if(interG.getCourant().equals(interG.getBoiteJeu())) interG.changerMenu(interG.getBoiteJeu(), interG.getBoiteJeu());
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
