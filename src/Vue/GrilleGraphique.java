package Vue;

import Global.Configuration;
import Modele.CarteRue;
import Modele.Jeu;
import Modele.Plateau;

import javax.swing.*;
import java.awt.*;

public class GrilleGraphique extends JComponent implements  ElementPlateauG{
    Graphics2D drawable;
    int tailleC;
    Jeu jeu;
    Image quartier1, quartier2, quartier3, quartier4, quartierX, cible, suspectBla, suspectBle, suspectJau, suspectNoi,
            suspectOra, suspectRos, suspectVer, suspectVio, suspectGri, sherlock, watson, chien;
    private int offsetX=0,offsetY=0;

    GrilleGraphique(Jeu j){
        jeu=j;
        quartier1 = Configuration.chargeImage("QuartierVide-1");
        quartier2 = Configuration.chargeImage("QuartierVide-2");
        quartier3 = Configuration.chargeImage("QuartierVide-3");
        quartier4 = Configuration.chargeImage("QuartierVide-4");
        quartierX = Configuration.chargeImage("QuartierVideX");
        cible = Configuration.chargeImage("Cible");
        suspectBla = Configuration.chargeImage("Suspect-blancB");
        suspectBle = Configuration.chargeImage("Suspect-bleuB");
        suspectJau = Configuration.chargeImage("Suspect-jauneB");
        suspectGri = Configuration.chargeImage("Suspect-grisB");
        suspectNoi = Configuration.chargeImage("Suspect-noirB");
        suspectOra = Configuration.chargeImage("Suspect-orangeB");
        suspectRos = Configuration.chargeImage("Suspect-roseB");
        suspectVer = Configuration.chargeImage("Suspect-vertB");
        suspectVio = Configuration.chargeImage("Suspect-violetB");
        sherlock = Configuration.chargeImage("Sherlock");
        watson = Configuration.chargeImage("Watson");
        chien = Configuration.chargeImage("Chien");
    }

    public void dessinerCarte(int l, int c, CarteRue rue){
        Image quartier,suspect;
        switch (rue.orientation){
            case Plateau.NSE:
                quartier = quartier4;
                break;
            case Plateau.NSO:
                quartier = quartier3;
                break;
            case Plateau.NEO:
                quartier = quartier2;
                break;
            case Plateau.SEO:
                quartier = quartier1;
                break;
            case Plateau.NSEO:
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
                suspect = suspectGri;
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
        drawable.drawImage(quartier, c*tailleC, l*tailleC,tailleC, tailleC, null);
        drawable.drawImage(suspect, (int) ((c+0.25)*tailleC), (int) ((l+0.25)*tailleC), (int) (tailleC*0.5), (int) (tailleC*0.5), null);
    }

    public void dessinerGrille(){
        int i=0,j=0;
        for(int l=0; l < 3; l++){
            for(int c=0; c < 3; c++){
                if(l==0) drawable.drawImage(cible, (c+1)*tailleC, 0, tailleC, tailleC, null);
                if(l==2) drawable.drawImage(cible, (c+1)*tailleC, 4*tailleC, tailleC, tailleC, null);
                if(c==0) drawable.drawImage(cible, 0, (l+1)*tailleC, tailleC, tailleC, null);
                if(c==2) drawable.drawImage(cible, 4*tailleC, (l+1)*tailleC, tailleC, tailleC, null);
                dessinerCarte(l+1,c+1,jeu.plateau().grille[l][c]);
            }
        }
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
        int hCase=hauteur/5; //3 rues + 2 inspecteurs qui l'entourent possiblement
        int lCase=largeur/5; //3 rues + 2 inspecteurs qui l'entourent possiblement
        tailleC=Math.min(hCase,lCase);

        dessinerGrille();

    }

    @Override
    public int getTailleCase() {
        return tailleC;
    }

    @Override
    public int getType() {
        return AdaptateurSouris.district;
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
