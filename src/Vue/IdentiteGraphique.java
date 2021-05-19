package Vue;

import Modele.Jeu;
import Modele.Joueur;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class IdentiteGraphique extends JComponent {
    Graphics2D drawable;
    int largeur,hauteur,tailleC;
    Jeu jeu;
    Image jack,sherlock, pioche;

    IdentiteGraphique(Jeu j){
        jeu=j;
        switch (jeu.idJack){
            case BLEU:
                jack = chargeImage("Suspect-bleuB");
                break;
            case GRIS:
                jack = chargeImage("Suspect-grisB");
                break;
            case NOIR:
                jack = chargeImage("Suspect-noirB");
                break;
            case ROSE:
                jack = chargeImage("Suspect-roseB");
                break;
            case VERT:
                jack = chargeImage("Suspect-vertB");;
                break;
            case BLANC:
                jack = chargeImage("Suspect-blancB");
                break;
            case JAUNE:
                jack = chargeImage("Suspect-jauneB");
                break;
            case ORANGE:
                jack = chargeImage("Suspect-orangeB");
                break;
            case VIOLET:
                jack = chargeImage("Suspect-violetB");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + jeu.idJack);
        }
        sherlock = chargeImage("Sherlock");
        pioche = chargeImage("PiocheA");
    }

    private Image chargeImage(String nom) {
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

    public void dessinerIdentite(){
        Image personnage;
        Joueur personnageCourant = jeu.joueurCourant;
        int taillePiocheAdv ;
        boolean isJack = personnageCourant.isJack();
        //isJack = !isJack;
        if(isJack){
            personnage = jack;
            taillePiocheAdv = jeu.jack.getCardList().size();
        } else {
            personnage = sherlock;
            taillePiocheAdv = jeu.enqueteur.getCardList().size();
        }

        drawable.setFont(new Font("default", Font.BOLD, 16));
        drawable.drawString("Vous Incarnez :",10,20);
        drawable.setFont(new Font("default", Font.ITALIC, 14));
        if(isJack) drawable.drawString("Meurtrier",10,40);
        else drawable.drawString("Enquêteur",10,40);
        drawable.drawImage(personnage, 10, 75, tailleC, tailleC, null);

        drawable.setFont(new Font("default", Font.BOLD, 16));
        drawable.drawString("Adversaire",10,(int) (2.6*tailleC-20));
        drawable.drawImage(pioche,(int) (tailleC*0.05),(int) (2.6*tailleC), (int) (0.5*tailleC), (int) (0.5*tailleC), null);
        drawable.drawString(": "+taillePiocheAdv+" / 8",(int) (tailleC*0.5)+25,(int) (2.9*tailleC) );
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
        int hCase=hauteur/4; //texte + Photo + Blanc + Adversaire
        int lCase=largeur;
        tailleC=Math.min(hCase,lCase);

        dessinerIdentite();

    }
}
