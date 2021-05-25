package Vue;

import Global.Configuration;
import Modele.CarteRue;
import Modele.Enqueteur;
import Modele.Jeu;
import Modele.Plateau;
import Modele.Action;

import javax.swing.*;
import java.awt.*;

public class DistrictGraphique extends JComponent implements  ElementPlateauG{
    Graphics2D drawable;
    int tailleC;
    Jeu jeu;
    Image quartier1, quartier2, quartier3, quartier4, quartierX, quartier1S, quartier2S, quartier3S, quartier4S,
            quartierXS, cible, suspectBla, suspectBle, suspectJau, suspectNoi, suspectOra, suspectRos, suspectVer,
            suspectVio, suspectGri, sherlock, watson, chien;
    private int offsetX=0,offsetY=0;

    //TODO Previsualisation de l'action
    //Mettre a jour après commandeDistrict et commandeJeu
    //reinitialiser après jouerCoup
    private Action actionTemp;

    DistrictGraphique(Jeu j){
        jeu=j;
        quartier1 = Configuration.chargeImage("QuartierVide-1");
        quartier2 = Configuration.chargeImage("QuartierVide-2");
        quartier3 = Configuration.chargeImage("QuartierVide-3");
        quartier4 = Configuration.chargeImage("QuartierVide-4");
        quartierX = Configuration.chargeImage("QuartierVideX");
        quartier1S = Configuration.chargeImage("QuartierVide-1S");
        quartier2S = Configuration.chargeImage("QuartierVide-2S");
        quartier3S = Configuration.chargeImage("QuartierVide-3S");
        quartier4S = Configuration.chargeImage("QuartierVide-4S");
        quartierXS = Configuration.chargeImage("QuartierVideXS");
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

    public void dessinerCarte(int l, int c, CarteRue rue,boolean isSelectionne, int orientation){
        Image quartier,suspect,enqueteur;
        int tailleE = (int) (0.8*tailleC);
        int offsetE = (int) (0.1*tailleC);
        if(!rue.getEnqueteurs().isEmpty()) {
            Enqueteur e = rue.getEnqueteurs().get(rue.getEnqueteurs().size()-1);
            switch (e.getNomPersonnage()){
                case TOBBY:
                    enqueteur = chien;
                    break;
                case WATSON:
                    enqueteur = watson;
                    break;
                case SHERLOCK:
                    enqueteur = sherlock;
                    break;
                default:
                    throw new IllegalStateException("Enqueteur inconnu");
            }
            switch(e.getPositionSurCarte()){
                case Enqueteur.NORD:
                    drawable.drawImage(enqueteur, c*tailleC+offsetE, (l-1)*tailleC+offsetE, tailleE, tailleE, null);
                    break;
                case Enqueteur.EST:
                    drawable.drawImage(enqueteur, (c+1)*tailleC+offsetE, l*tailleC+offsetE, tailleE, tailleE, null);
                    break;
                case Enqueteur.SUD:
                    drawable.drawImage(enqueteur, c*tailleC+offsetE, (l+1)*tailleC+offsetE, tailleE, tailleE, null);
                    break;
                case Enqueteur.OUEST:
                    drawable.drawImage(enqueteur, (c-1)*tailleC+offsetE, l*tailleC+offsetE, tailleE, tailleE, null);
                    break;
            }
        }
        switch (orientation){
            case Plateau.NSE:
                if(isSelectionne) quartier = quartier4S;
                else quartier = quartier4;
                break;
            case Plateau.NSO:
                if(isSelectionne) quartier = quartier3S;
                else quartier = quartier3;
                break;
            case Plateau.NEO:
                if(isSelectionne) quartier = quartier2S;
                else quartier = quartier2;
                break;
            case Plateau.SEO:
                if(isSelectionne) quartier = quartier1S;
                else quartier = quartier1;
                break;
            case Plateau.NSEO:
                if(isSelectionne) quartier = quartierXS;
                else quartier = quartierX;
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
        drawable.drawImage(quartier, c*tailleC, l*tailleC,tailleC, tailleC, null);
        if(!rue.getSuspect().getInnocente())drawable.drawImage(suspect, (int) ((c+0.25)*tailleC), (int) ((l+0.25)*tailleC), (int) (tailleC*0.5), (int) (tailleC*0.5), null);
    }

    public void dessinerCarte(int l, int c, CarteRue rue){
        dessinerCarte(l, c, rue,false, rue.getOrientation());
    }

    public void dessinerCarte(int l, int c, CarteRue rue, boolean isSelectionne){
        dessinerCarte(l, c, rue,isSelectionne, rue.getOrientation());
    }

    public void dessinerGrille(){
        int i=0,j=0;
        for(int l=0; l < 3; l++){
            for(int c=0; c < 3; c++){
                //if(l==0) drawable.drawImage(cible, (c+1)*tailleC, 0, tailleC, tailleC, null);
                //if(l==2) drawable.drawImage(cible, (c+1)*tailleC, 4*tailleC, tailleC, tailleC, null);
                //if(c==0) drawable.drawImage(cible, 0, (l+1)*tailleC, tailleC, tailleC, null);
                //if(c==2) drawable.drawImage(cible, 4*tailleC, (l+1)*tailleC, tailleC, tailleC, null);
                dessinerCarte(l+1,c+1,jeu.plateau().grille[l][c]);
            }
        }
    }

    public void dessinerFeedback(){
        Point pos1 = actionTemp.getPosition1();
        Point pos2 = actionTemp.getPosition2();
        switch (actionTemp.getAction()){
            case ECHANGER_DISTRICT:
                if (pos1 != null)dessinerCarte(pos1.y+1,pos1.x+1,jeu.plateau().grille[pos1.y][pos1.x],true);
                if (pos2 != null)dessinerCarte(pos2.y+1,pos2.x+1,jeu.plateau().grille[pos2.y][pos2.x],true);
                break;
            case ROTATION_DISTRICT:
                if (pos1 != null)dessinerCarte(pos1.y+1,pos1.x+1,jeu.plateau().grille[pos1.y][pos1.x],true, actionTemp.getOrientationNew());
                break;
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

        if(actionTemp!=null)dessinerFeedback();
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

    @Override
    public void setSelection(int s) {

    }

    public void setActionTemp(Action actionTemp) {
        this.actionTemp = actionTemp;
        repaint();
    }
}
