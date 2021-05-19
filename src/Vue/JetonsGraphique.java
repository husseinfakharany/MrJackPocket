package Vue;

import Modele.Jeu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class JetonsGraphique extends JComponent implements ElementPlateauG {
    Graphics2D drawable;
    int largeur,hauteur,tailleC,offsetX,offsetY;
    Jeu jeu;
    Image jeton1A , jeton1B , jeton2A , jeton2B, jeton3A , jeton3B, jeton4A , jeton4B;

    JetonsGraphique(Jeu j){
        jeu=j;
        jeton1A = chargeImage("Jeton-1-A");
        jeton1B = chargeImage("Jeton-1-B");
        jeton2A = chargeImage("Jeton-2-A");
        jeton2B = chargeImage("Jeton-2-B");
        jeton3A = chargeImage("Jeton-3-A");
        jeton3B = chargeImage("Jeton-3-B");
        jeton4A = chargeImage("Jeton-4-A");
        jeton4B = chargeImage("Jeton-4-B");
        offsetX = 20;
        offsetY = 20;
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

    //TODO charger la bonne face
    public void dessinerJetons(){
        Image jeton1, jeton2, jeton3, jeton4;
        jeton1 = jeton1A;
        jeton2 = jeton2A;
        jeton3 = jeton3A;
        jeton4 = jeton4B;
        drawable.setFont(new Font("default", Font.BOLD, 16));
        drawable.drawString("Jetons :",85,16);
        drawable.setFont(new Font("default", Font.PLAIN, 12));
        drawable.drawImage(jeton1, offsetX, offsetY, 2*tailleC, 2*tailleC, null);
        drawable.drawImage(jeton2, 2*(tailleC+offsetX), offsetY, 2*tailleC, 2*tailleC, null);
        drawable.drawImage(jeton3, offsetX, 2*(tailleC+offsetY), 2*tailleC, 2*tailleC, null);
        drawable.drawImage(jeton4, 2*(tailleC+offsetX), 2*(tailleC+offsetY), 2*tailleC, 2*tailleC, null);
    }

    @Override
    public void paintComponent(Graphics g) {
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

        dessinerJetons();


    }

    @Override
    public int getTailleCase() {
        return tailleC;
    }

    @Override
    public int getType() {
        return 2; //2 : Jetons
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
