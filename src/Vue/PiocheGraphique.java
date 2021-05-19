package Vue;

import Modele.Jeu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class PiocheGraphique extends JComponent {
    Graphics2D drawable;
    int tailleC;
    Jeu jeu;
    Image piocheA, piocheD;
    boolean piocheActive;
    PiocheGraphique(Jeu j){
        jeu=j;
        piocheD = chargeImage("Pioche");
        piocheA = chargeImage("Pioche");
    }

    private Image chargeImage ( String nom) {
        Image img = null;
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream( "PNG/" + nom + ".png" );
        try {
        // Chargement d'une image
            img = ImageIO.read (in);
        } catch ( Exception e ) {
            System.err.println("Pb image : " + nom);
            System .exit(1);
        }
        return img;
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
        drawable.clearRect(0, 0, largeur, hauteur);

        //Calcul de la taille d'une case
        int hCase=hauteur/5; //3 rues + 2 inspecteurs qui l'entoure possiblement
        int lCase=largeur/5; //3 rues + 2 inspecteurs qui l'entoure possiblement
        tailleC=Math.min(hCase,lCase);
        if(piocheActive) drawable.drawImage(piocheA,0,0,tailleC,tailleC,null);
        else drawable.drawImage(piocheD,0,0,tailleC,tailleC,null);

    }
}
