package Vue;

import Global.Configuration;
import Modele.*;
import Modele.Action;

import javax.swing.*;
import java.awt.*;

public class DistrictGraphique extends JComponent implements ElementPlateauG{
    Graphics2D drawable;
    int tailleC;
    Jeu jeu;
    Image quartier1, quartier2, quartier3, quartier4, quartierX, quartier1S, quartier2S, quartier3S, quartier4S,
            quartierXS, cible, suspectBla, suspectBle, suspectJau, suspectNoi, suspectOra, suspectRos, suspectVer,
            suspectVio, suspectGri, suspectBlaJ, suspectBleJ, suspectJauJ, suspectNoiJ, suspectOraJ, suspectRosJ, suspectVerJ,
            suspectVioJ, suspectGriJ, sherlock, watson, chien, sherlockNB, watsonNB, chienNB;
    private int offsetX=0,offsetY=0;
    private boolean afficherVisible;

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
        suspectBla = Configuration.chargeImage("Suspect-blanc");
        suspectBle = Configuration.chargeImage("Suspect-bleu");
        suspectJau = Configuration.chargeImage("Suspect-jaune");
        suspectGri = Configuration.chargeImage("Suspect-marron");
        suspectNoi = Configuration.chargeImage("Suspect-noir");
        suspectOra = Configuration.chargeImage("Suspect-orange");
        suspectRos = Configuration.chargeImage("Suspect-rose");
        suspectVer = Configuration.chargeImage("Suspect-vert");
        suspectVio = Configuration.chargeImage("Suspect-violet");
        suspectBlaJ = Configuration.chargeImage("Suspect-blancJ");
        suspectBleJ = Configuration.chargeImage("Suspect-bleuJ");
        suspectJauJ = Configuration.chargeImage("Suspect-jauneJ");
        suspectGriJ = Configuration.chargeImage("Suspect-marronJ");
        suspectNoiJ = Configuration.chargeImage("Suspect-noirJ");
        suspectOraJ = Configuration.chargeImage("Suspect-orangeJ");
        suspectRosJ = Configuration.chargeImage("Suspect-roseJ");
        suspectVerJ = Configuration.chargeImage("Suspect-vertJ");
        suspectVioJ = Configuration.chargeImage("Suspect-violetJ");
        sherlock = Configuration.chargeImage("Sherlock");
        watson = Configuration.chargeImage("Watson");
        chien = Configuration.chargeImage("Chien");
        sherlockNB = Configuration.chargeImage("SherlockNB");
        watsonNB = Configuration.chargeImage("WatsonNB");
        chienNB = Configuration.chargeImage("ChienNB");
        setAfficherVisible(false);
    }

    public void dessinerCarte(int l, int c, CarteRue rue,boolean isSelectionne, int orientation){
        Image quartier,suspect,enqueteur;
        int tailleE = (int) (0.7*tailleC);
        int offsetE = (int) (0.3*tailleC);
        int nbNord=0,nbSud=0,nbEst=0,nbOuest=0;
        for( Enqueteur e : rue.getEnqueteurs() ) {
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
                    drawable.drawImage(enqueteur, c*tailleC+offsetE/2, (l-1)*tailleC+(offsetE/3)*nbNord, tailleE, tailleE, null);
                    nbNord++;
                    break;
                case Enqueteur.EST:
                    drawable.drawImage(enqueteur, (c+1)*tailleC+(offsetE/3)*nbEst, l*tailleC+offsetE/2, tailleE, tailleE, null);
                    nbEst++;
                    break;
                case Enqueteur.SUD:
                    drawable.drawImage(enqueteur, c*tailleC+offsetE/2, (l+1)*tailleC+offsetE+(offsetE/3)*(nbSud-3), tailleE, tailleE, null);
                    nbSud++;
                    break;
                case Enqueteur.OUEST:
                    drawable.drawImage(enqueteur, (c-1)*tailleC-(offsetE/3)*(nbOuest-3), l*tailleC+offsetE/2, tailleE, tailleE, null);
                    nbOuest++;
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
                if (afficherVisible && ( (jeu.plateau().visibles().contains(rue.suspect) && !jeu.plateau().jackVisible)
                        || ( !jeu.plateau().visibles().contains(rue.suspect) && jeu.plateau().jackVisible) ) ) suspect = suspectBleJ;
                else suspect = suspectBle;
                break;
            case GRIS:
                if (afficherVisible && ( (jeu.plateau().visibles().contains(rue.suspect) && !jeu.plateau().jackVisible)
                        || ( !jeu.plateau().visibles().contains(rue.suspect) && jeu.plateau().jackVisible) ) ) suspect = suspectGriJ;
                else suspect = suspectGri;
                break;
            case NOIR:
                if (afficherVisible && ( (jeu.plateau().visibles().contains(rue.suspect) && !jeu.plateau().jackVisible)
                        || ( !jeu.plateau().visibles().contains(rue.suspect) && jeu.plateau().jackVisible) ) ) suspect = suspectNoiJ;
                else suspect = suspectNoi;
                break;
            case ROSE:
                if (afficherVisible && ( (jeu.plateau().visibles().contains(rue.suspect) && !jeu.plateau().jackVisible)
                        || ( !jeu.plateau().visibles().contains(rue.suspect) && jeu.plateau().jackVisible) ) ) suspect = suspectRosJ;
                else suspect = suspectRos;
                break;
            case VERT:
                if (afficherVisible && ( (jeu.plateau().visibles().contains(rue.suspect) && !jeu.plateau().jackVisible)
                        || ( !jeu.plateau().visibles().contains(rue.suspect) && jeu.plateau().jackVisible) ) ) suspect = suspectVerJ;
                else suspect = suspectVer;
                break;
            case BLANC:
                if (afficherVisible && ( (jeu.plateau().visibles().contains(rue.suspect) && !jeu.plateau().jackVisible)
                        || ( !jeu.plateau().visibles().contains(rue.suspect) && jeu.plateau().jackVisible) ) ) suspect = suspectBlaJ;
                else suspect = suspectBla;
                break;
            case JAUNE:
                if (afficherVisible && ( (jeu.plateau().visibles().contains(rue.suspect) && !jeu.plateau().jackVisible)
                        || ( !jeu.plateau().visibles().contains(rue.suspect) && jeu.plateau().jackVisible) ) ) suspect = suspectJauJ;
                else suspect = suspectJau;
                break;
            case ORANGE:
                if (afficherVisible && ( (jeu.plateau().visibles().contains(rue.suspect) && !jeu.plateau().jackVisible)
                        || ( !jeu.plateau().visibles().contains(rue.suspect) && jeu.plateau().jackVisible) ) ) suspect = suspectOraJ;
                else suspect = suspectOra;
                break;
            case VIOLET:
                if (afficherVisible && ( (jeu.plateau().visibles().contains(rue.suspect) && !jeu.plateau().jackVisible)
                        || ( !jeu.plateau().visibles().contains(rue.suspect) && jeu.plateau().jackVisible) ) ) suspect = suspectVioJ;
                else suspect = suspectVio;
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
                dessinerCarte(l+1,c+1,jeu.plateau().grille[l][c]);
            }
        }
    }

    public void dessinerFeedback(){
        Point pos1 = actionTemp.getPosition1();
        Point pos2 = actionTemp.getPosition2();
        Point s;
        int tailleE = (int) (0.8*tailleC);
        int offsetE = (int) (0.1*tailleC);
        if(actionTemp.getAction() == null || (actionTemp.getOrientationNew() == Plateau.NSEO && actionTemp.getPosition1().getX() == -1) ) return;
        switch (actionTemp.getAction()){
            case ECHANGER_DISTRICT:
                if (pos1 != null)dessinerCarte(pos1.y+1,pos1.x+1,jeu.plateau().grille[pos1.y][pos1.x],true);
                if (pos2 != null)dessinerCarte(pos2.y+1,pos2.x+1,jeu.plateau().grille[pos2.y][pos2.x],true);
                break;
            case ROTATION_DISTRICT:
                if (pos1 != null) {
                    if ( actionTemp.getOrientationNew() != jeu.plateau().grille[pos1.y][pos1.x].getOrientation() || actionTemp.getOrientationNew() == Plateau.NSEO ) dessinerCarte(pos1.y+1,pos1.x+1,jeu.plateau().grille[pos1.y][pos1.x],true, actionTemp.getOrientationNew());
                    else repaint();
                }
                break;
            case DEPLACER_SHERLOCK:
                s = Plateau.calculPosition(jeu.plateau().enqueteurs.get(Plateau.SHERLOCK).getPosition(),jeu.plateau().enqueteurs.get(Plateau.SHERLOCK).getPositionSurCarte());
                s = Plateau.suivant(s);
                if(actionTemp.getDeplacement()==1) drawable.drawImage(sherlockNB, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                drawable.drawImage(cible, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                s = Plateau.suivant(s);
                if(actionTemp.getDeplacement()==2) drawable.drawImage(sherlockNB, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                drawable.drawImage(cible, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                break;
            case DEPLACER_WATSON:
                s = Plateau.calculPosition(jeu.plateau().enqueteurs.get(Plateau.WATSON).getPosition(),jeu.plateau().enqueteurs.get(Plateau.WATSON).getPositionSurCarte());
                s = Plateau.suivant(s);
                if(actionTemp.getDeplacement()==1) drawable.drawImage(watsonNB, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                drawable.drawImage(cible, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                s = Plateau.suivant(s);
                if(actionTemp.getDeplacement()==2) drawable.drawImage(watsonNB, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                drawable.drawImage(cible, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                break;
            case DEPLACER_TOBBY:
                s = Plateau.calculPosition(jeu.plateau().enqueteurs.get(Plateau.TOBBY).getPosition(),jeu.plateau().enqueteurs.get(Plateau.TOBBY).getPositionSurCarte());
                s = Plateau.suivant(s);
                if(actionTemp.getDeplacement()==1) drawable.drawImage(chienNB, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                drawable.drawImage(cible, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                s = Plateau.suivant(s);
                if(actionTemp.getDeplacement()==2) drawable.drawImage(chienNB, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                drawable.drawImage(cible, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                break;
            case DEPLACER_JOKER:
                for (Enqueteur enqueteur : jeu.plateau().enqueteurs) {
                    s = Plateau.calculPosition(enqueteur.getPosition(),enqueteur.getPositionSurCarte());
                    Point dep = (Point) s.clone();
                    s = Plateau.suivant(s);
                    if(actionTemp.getNumEnqueteur() == Plateau.SHERLOCK && enqueteur.getNumEnqueteur() == Plateau.SHERLOCK && actionTemp.getDeplacement()==1) {
                        drawable.drawImage(sherlockNB, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                    }
                    if(actionTemp.getNumEnqueteur() == Plateau.WATSON && enqueteur.getNumEnqueteur() == Plateau.WATSON && actionTemp.getDeplacement()==1) {
                        drawable.drawImage(watsonNB, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                    }
                    if(actionTemp.getNumEnqueteur() == Plateau.TOBBY && enqueteur.getNumEnqueteur() == Plateau.TOBBY && actionTemp.getDeplacement()==1){
                        drawable.drawImage(chienNB, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                    }
                    drawable.drawImage(cible, (s.x)*tailleC+offsetE, (s.y)*tailleC+offsetE, tailleE, tailleE, null);
                }
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
        //drawable.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        drawable.setBackground(new Color(255,255,255,0));
        g.setColor(new Color(255,255,255,0));
        g.fillRect(0, 0, getWidth(),getHeight());

        //Calcul de la taille d'une case
        int hCase=hauteur/5; //3 rues + 2 inspecteurs qui l'entourent possiblement
        int lCase=largeur/5; //3 rues + 2 inspecteurs qui l'entourent possiblement
        tailleC=Math.min(hCase,lCase);

        dessinerGrille();

        if(actionTemp!=null) dessinerFeedback();
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

    public void dessinerFeedback(Action actionTemp) {
        this.actionTemp = actionTemp;
        repaint();
    }

    public boolean isAfficherVisible() {
        return afficherVisible;
    }

    public void setAfficherVisible(boolean afficherVisible) {
        this.afficherVisible = afficherVisible;
        repaint();
    }
}
