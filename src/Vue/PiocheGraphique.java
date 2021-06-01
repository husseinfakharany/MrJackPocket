package Vue;

import Global.Configuration;
import Modele.Jeu;

import javax.swing.*;
import java.awt.*;

public class PiocheGraphique extends JComponent implements ElementPlateauG{
    Graphics2D drawable;
    int tailleC;
    Jeu jeu;
    Image piocheA, piocheD;
    boolean piocheActive;
    private int offsetX = 25, offsetY = 35;

    PiocheGraphique(Jeu j){
        jeu=j;
        piocheD = Configuration.chargeImage("PiocheD");
        piocheA = Configuration.chargeImage("PiocheA");
        piocheActive = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        int largeur, hauteur;
        // Graphics 2D est le vrai type de l'objet passé en paramètre
        // Le cast permet d'avoir acces a un peu plus de primitives de dessin
        drawable = (Graphics2D) g;

        // On recupere quelques infos provenant de la partie JComponent
        largeur = getSize().width;
        hauteur = getSize().height;

        // On efface tout
        //drawable.clearRect(0, 0, largeur, hauteur);
        drawable.setBackground(new Color(255,255,255,0));
        g.setColor(new Color(255,255,255,0));
        g.fillRect(0, 0, getWidth(),getHeight());

        //Calcul de la taille d'une case
        int hCase=hauteur-offsetY;
        int lCase=largeur;
        tailleC=Math.min(hCase,lCase);

        int taillePioche = jeu.plateau().getTaillePioche();
        drawable.setFont(new Font("default", Font.BOLD, 16));
        drawable.setColor(Color.WHITE);
        drawable.drawString("Pioche ( " + taillePioche + " / 8 ) :",(int) ((largeur-0.8*tailleC)/2),25);
        drawable.setFont(new Font("default", Font.PLAIN, 12));
        if(piocheActive) drawable.drawImage(piocheA,(int) ((largeur-0.8*tailleC)/2),offsetY,(int) (0.8*tailleC),(int) (0.8*tailleC),null);
        else drawable.drawImage(piocheD,(int) ((largeur-0.8*tailleC)/2),offsetY,(int) (0.8*tailleC),(int) (0.8*tailleC),null);

    }

    @Override
    public int getTailleCase() {
        return tailleC;
    }

    @Override
    public int getType() {
        return AdaptateurSouris.pioche;
    }

    @Override
    public int getOffsetX() {
        return offsetX;
    }

    @Override
    public int getOffsetY() {
        return offsetY;
    }

    public void setPiocheActive(boolean piocheA){
        piocheActive = piocheA;
        repaint();
    }
}
