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
        piocheD = chargeImage("PiocheD");
        piocheA = chargeImage("PiocheA");
        piocheActive = false;
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

        int taillePioche = 8;
        drawable.setFont(new Font("default", Font.BOLD, 16));
        drawable.drawString("Pioche ( " + taillePioche + " / 8 ) :",10,20);
        drawable.setFont(new Font("default", Font.PLAIN, 12));
        if(piocheActive) drawable.drawImage(piocheA,25,30,tailleC,tailleC,null);
        else drawable.drawImage(piocheD,25,30,tailleC,tailleC,null);


    }
}
