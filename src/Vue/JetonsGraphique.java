package Vue;

import Global.Configuration;
import Modele.Jeu;

import javax.swing.*;
import java.awt.*;

public class JetonsGraphique extends JComponent implements ElementPlateauG {
    Graphics2D drawable;
    int largeur,hauteur,tailleC,offsetX,offsetY;
    Jeu jeu;
    Image jeton1A , jeton1B , jeton2A , jeton2B, jeton3A , jeton3B, jeton4A , jeton4B,
            jeton1ANB , jeton1BNB , jeton2ANB , jeton2BNB, jeton3ANB , jeton3BNB, jeton4ANB , jeton4BNB ,
            jeton1ATra , jeton1BTra , jeton2ATra , jeton2BTra, jeton3ATra , jeton3BTra, jeton4ATra , jeton4BTra, jetonV, tourSuivant;
    int selection;
    private boolean estJouable;

    JetonsGraphique(Jeu j){
        jeu=j;
        jeton1A = Configuration.chargeImage("Jeton-1-A");
        jeton1B = Configuration.chargeImage("Jeton-1-B");
        jeton2A = Configuration.chargeImage("Jeton-2-A");
        jeton2B = Configuration.chargeImage("Jeton-2-B");
        jeton3A = Configuration.chargeImage("Jeton-3-A");
        jeton3B = Configuration.chargeImage("Jeton-3-B");
        jeton4A = Configuration.chargeImage("Jeton-4-A");
        jeton4B = Configuration.chargeImage("Jeton-4-B");
        jeton1ANB = Configuration.chargeImage("Jeton-1-A-NB");
        jeton1BNB = Configuration.chargeImage("Jeton-1-B-NB");
        jeton2ANB = Configuration.chargeImage("Jeton-2-A-NB");
        jeton2BNB = Configuration.chargeImage("Jeton-2-B-NB");
        jeton3ANB = Configuration.chargeImage("Jeton-3-A-NB");
        jeton3BNB = Configuration.chargeImage("Jeton-3-B-NB");
        jeton4ANB = Configuration.chargeImage("Jeton-4-A-NB");
        jeton4BNB = Configuration.chargeImage("Jeton-4-B-NB");
        jeton1ATra = Configuration.chargeImage("Jeton-1-A-tra");
        jeton1BTra = Configuration.chargeImage("Jeton-1-B-tra");
        jeton2ATra = Configuration.chargeImage("Jeton-2-A-tra");
        jeton2BTra = Configuration.chargeImage("Jeton-2-B-tra");
        jeton3ATra = Configuration.chargeImage("Jeton-3-A-tra");
        jeton3BTra = Configuration.chargeImage("Jeton-3-B-tra");
        jeton4ATra = Configuration.chargeImage("Jeton-4-A-tra");
        jeton4BTra = Configuration.chargeImage("Jeton-4-B-tra");
        tourSuivant = Configuration.chargeImage("tourSuivant");
        jetonV = Configuration.chargeImage("JetonV");
        offsetX = 20;
        offsetY = 30;
        selection = -1;
        dessinerValide(false);
    }

    public void dessinerJetons(){
        Image jeton1, jeton2, jeton3, jeton4, jeton1Tra, jeton2Tra, jeton3Tra, jeton4Tra;

        drawable.setFont(new Font("default", Font.BOLD, 25));
        drawable.drawString("Jetons :",0,25);
        drawable.setFont(new Font("default", Font.PLAIN, 12));

        if (jeu.plateau().tousJetonsJoues()){
            int taille = Math.min(largeur,hauteur);
            drawable.drawImage(tourSuivant, offsetX, offsetX+20, taille-offsetX-20 , taille-offsetX-20, null);
            return;
        }

        if(jeu.plateau().getJeton(0).estRecto())  {
            jeton1 = selection==1 || selection == -1? jeton1A : jeton1ANB;
            jeton1Tra = jeton1ATra;
        }
        else{
            jeton1 = selection==1 || selection == -1? jeton1B : jeton1BNB;
            jeton1Tra = jeton1BTra;
        }
        if(jeu.plateau().getJeton(1).estRecto()){
            jeton2 = selection==2 || selection == -1? jeton2A : jeton2ANB;
            jeton2Tra = jeton2ATra;
        }
        else {
            jeton2 = selection==2 || selection == -1? jeton2B : jeton2BNB;
            jeton2Tra = jeton2BTra;
        }
        if(jeu.plateau().getJeton(2).estRecto()){
            jeton3 = selection==3 || selection == -1? jeton3A : jeton3ANB;
            jeton3Tra = jeton3ATra;
        }
        else{
            jeton3 = selection==3 || selection == -1? jeton3B : jeton3BNB;
            jeton3Tra = jeton3BTra;
        }
        if(jeu.plateau().getJeton(3).estRecto()) {
            jeton4 = selection==4 || selection == -1? jeton4A : jeton4ANB;
            jeton4Tra = jeton4ATra;
        }
        else{
            jeton4 = selection==4 || selection == -1? jeton4B : jeton4BNB;
            jeton4Tra = jeton4BTra;
        }


        if(jeu.plateau().getJeton(0).getDejaJoue()) drawable.drawImage(jeton1Tra, 0, offsetY, tailleC, tailleC, null);
        else drawable.drawImage(jeton1, 0, offsetY, tailleC, tailleC, null);
        if(jeu.plateau().getJeton(1).getDejaJoue())drawable.drawImage(jeton2Tra, tailleC+offsetX, offsetY, tailleC, tailleC, null);
        else drawable.drawImage(jeton2, tailleC+offsetX, offsetY, tailleC, tailleC, null);
        if(jeu.plateau().getJeton(2).getDejaJoue()) drawable.drawImage(jeton3Tra, 0, tailleC+2*offsetY, tailleC, tailleC, null);
        else drawable.drawImage(jeton3, 0, tailleC+2*offsetY, tailleC, tailleC, null);
        if(jeu.plateau().getJeton(3).getDejaJoue()) drawable.drawImage(jeton4Tra, tailleC+offsetX, tailleC+2*offsetY, tailleC, tailleC, null);
        else drawable.drawImage(jeton4, tailleC+offsetX, tailleC+2*offsetY, tailleC, tailleC, null);


        if(estJouable) {
            int posX = (selection-1)%2;
            int posY = (selection-1)/2;
            drawable.drawImage(jetonV, posX*(tailleC+offsetX) , posY*tailleC+(posY+1)*offsetY, tailleC, tailleC, null);
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
        int hCase=(hauteur-2*offsetY)/2;
        int lCase=(largeur-2*offsetX)/2;
        tailleC=Math.min(hCase,lCase);

        dessinerJetons();

    }

    public void dessinerSelection(int selection){
        this.selection = selection;
        repaint();
    }

    @Override
    public int getTailleCase() {
        return tailleC;
    }

    @Override
    public int getType() {
        return AdaptateurSouris.jetons;
    }

    @Override
    public int getOffsetX() {
        return offsetX;
    }

    @Override
    public int getOffsetY() {
        return offsetY;
    }

    public void dessinerValide(boolean estJouable) {
        this.estJouable = estJouable;
        repaint();
    }
}
