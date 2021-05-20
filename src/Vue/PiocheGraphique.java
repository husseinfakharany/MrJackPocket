package Vue;

import Modele.Jeu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class PiocheGraphique extends JComponent implements ElementPlateauG{
    Graphics2D drawable;
    int tailleC;
    Jeu jeu;
    Image piocheA, piocheD;
    boolean piocheActive;
    private int offsetX = 25, offsetY = 30;

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
        int hCase=hauteur;
        int lCase=largeur;
        tailleC=Math.min(hCase,lCase);

        int taillePioche = 8;
        drawable.setFont(new Font("default", Font.BOLD, 16));
        drawable.drawString("Pioche ( " + taillePioche + " / 8 ) :",(int) (largeur*0.5)-50,20);
        drawable.setFont(new Font("default", Font.PLAIN, 12));
        if(piocheActive) drawable.drawImage(piocheA,offsetX,offsetY,(int) (0.8*tailleC),(int) (0.8*tailleC),null);
        else drawable.drawImage(piocheD,offsetX,offsetY,tailleC-offsetY,tailleC-offsetY,null);


    }

    @Override
    public int getTailleCase() {
        return tailleC;
    }

    @Override
    public int getType() {
        return 4; //4 pioche
    }

    @Override
    public int getOffsetX() {
        return offsetX;
    }

    @Override
    public int getOffsetY() {
        return offsetY;
    }
}
