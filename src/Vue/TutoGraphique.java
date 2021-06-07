package Vue;

import Global.Configuration;

import javax.swing.*;
import java.awt.*;



public class TutoGraphique extends JComponent implements  ElementPlateauG{
    Graphics2D drawable;
    int tailleC;
    Image [] s;
    int i;
    private final int offsetX;
    private final int offsetY;
    Image toDraw;

    TutoGraphique(){
        i=0;
        s= new Image[8];
        for (int j=0;j<s.length;j++) {
            s[j]= Configuration.chargeImage("tuto"+j);
        }
        offsetX = 0;
        offsetY = 0;
        toDraw = s[1];
    }


    public void tutorielNext(){

        if (i < s.length - 1){
            i++;
            toDraw = s[i];
            repaint();
        }
        else {
                System.out.println("No more tuto");
        }

    }

    public void tutorielPrevious(){
        if (i > 0){
            i--;
            toDraw = s[i];
            repaint();
        }
        else {
            System.out.println("No more tuto");
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

        drawable.drawImage(toDraw, offsetX,offsetY,null);

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

