package Vue;

import Modele.CarteRue;
import Modele.Jeu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class JetonsGraphique extends JComponent {
    Graphics2D drawable;
    int largeur,hauteur,tailleC;
    Jeu jeu;
    Image quartier1, quartier2, quartier3, quartier4, quartierX, cible, suspectBla, suspectBle, suspectJau, suspectMar, suspectNoi,
            suspectOra, suspectRos, suspectVer, suspectVio, suspectGri, sherlock, watson, chien;

    JetonsGraphique(Jeu j){
        jeu=j;
        quartier1 = chargeImage("QuartierVide-1");
        quartier2 = chargeImage("QuartierVide-2");
        quartier3 = chargeImage("QuartierVide-3");
        quartier4 = chargeImage("QuartierVide-4");
        quartierX = chargeImage("QuartierVideX");
        cible = chargeImage("Cible");
        suspectBla = chargeImage("Suspect-blancB");
        suspectBle = chargeImage("Suspect-bleuB");
        suspectJau = chargeImage("Suspect-jauneB");
        suspectMar = chargeImage("Suspect-marronB");
        suspectNoi = chargeImage("Suspect-noirB");
        suspectOra = chargeImage("Suspect-orangeB");
        suspectRos = chargeImage("Suspect-roseB");
        suspectVer = chargeImage("Suspect-vertB");
        suspectVio = chargeImage("Suspect-violetB");
        //suspectGri = chargeImage("Suspect-grisB");
        sherlock = chargeImage("Sherlock");
        watson = chargeImage("Watson");
        chien = chargeImage("Chien");;
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

    public void dessinerCarte(int l, int c, CarteRue rue){
        Image quartier,suspect;
        switch (rue.orientation){
            case CarteRue.NSE:
                quartier = quartier4;
                break;
            case CarteRue.NSO:
                quartier = quartier3;
                break;
            case CarteRue.NEO:
                quartier = quartier2;
                break;
            case CarteRue.SEO:
                quartier = quartier1;
                break;
            case CarteRue.NSEO:
                quartier = quartierX;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + rue.orientation);
        }
        switch (rue.suspect.getCouleur()){
            case BLEU:
                suspect = suspectBle;
                break;
            case GRIS:
                suspect = cible;
                break;
            case NOIR:
                suspect = suspectNoi;
                break;
            case ROSE:
                suspect = suspectRos;
                break;
            case VERT:
                suspect = suspectVer;
                break;
            case BLANC:
                suspect = suspectBla;
                break;
            case JAUNE:
                suspect = suspectJau;
                break;
            case ORANGE:
                suspect = suspectOra;
                break;
            case VIOLET:
                suspect = suspectVio;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + rue.orientation);
        }
        //suspect.getScaledInstance(tailleC,tailleC,Image.SCALE_DEFAULT);
        drawable.drawImage(quartier, l*tailleC, c*tailleC, tailleC, tailleC, null);
        drawable.drawImage(suspect, l*tailleC, c*tailleC, tailleC, tailleC, null);
    }

    public void dessinerGrille(){
        int i=0,j=0;
        for(int l=0; l < 3; l++){
            for(int c=0; c < 3; c++){
                if(l==0) drawable.drawImage(cible, (c+1)*tailleC, 0, tailleC, tailleC, null);
                if(l==3) drawable.drawImage(cible, (c+1)*tailleC, 4*tailleC, tailleC, tailleC, null);
                if(c==0) drawable.drawImage(cible, 0, (l+1)*tailleC, tailleC, tailleC, null);
                if(c==3) drawable.drawImage(cible, 4*tailleC, (l+1)*tailleC, tailleC, tailleC, null);
                dessinerCarte(l+1,c+1,jeu.grille[l][c]);
            }
        }
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

        dessinerGrille();

    }
}
