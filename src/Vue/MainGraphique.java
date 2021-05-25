package Vue;

import Global.Configuration;
import Modele.CarteAlibi;
import Modele.Jeu;
import Modele.SuspectCouleur;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

//TODO agrandir la taille des cartes et recupere la bonne main

public class MainGraphique extends JComponent {
    Graphics2D drawable;
    int largeur,hauteur,tailleC;
    Jeu jeu;
    Image suspectBla, suspectBle, suspectJau, suspectNoi, suspectOra, suspectRos, suspectVer, suspectVio, suspectGri;
    boolean afficherEnqueteur;

    MainGraphique(Jeu j){
        jeu=j;
        suspectBle = Configuration.chargeImage("Suspect-bleuB");
        suspectGri = Configuration.chargeImage("Suspect-grisB");
        suspectNoi = Configuration.chargeImage("Suspect-noirB");
        suspectRos = Configuration.chargeImage("Suspect-roseB");
        suspectVer = Configuration.chargeImage("Suspect-vertB");
        suspectBla = Configuration.chargeImage("Suspect-blancB");
        suspectJau = Configuration.chargeImage("Suspect-jauneB");
        suspectOra = Configuration.chargeImage("Suspect-orangeB");
        suspectVio = Configuration.chargeImage("Suspect-violetB");
        afficherEnqueteur = true;
        /*ArrayList<CarteAlibi> mainFictif = new ArrayList<CarteAlibi>();
        mainFictif.add( new CarteAlibi( new Suspect( SuspectNom.JEREMY_BERT,null ) ) );
        mainFictif.add( new CarteAlibi( new Suspect( SuspectNom.MADAME,null ) ) );
        mainFictif.add( new CarteAlibi( new Suspect( SuspectNom.JOHN_PIZER,null ) ) );
        mainFictif.add( new CarteAlibi( new Suspect( SuspectNom.INSPECTOR_LESTRADE,null ) ) );
        mainFictif.add( new CarteAlibi( new Suspect( SuspectNom.SERGENT_GOODLEY,null ) ) );
        jeu.plateau().enqueteur.setCardList(mainFictif);*/
    }

    public void dessinerCarte(SuspectCouleur couleur, int x, int y){
        switch (couleur){
            case BLEU:
                drawable.drawImage(suspectBle, x,y,tailleC,tailleC,null);
                break;
            case GRIS:
                drawable.drawImage(suspectGri, x,y,tailleC,tailleC,null);
                break;
            case NOIR:
                drawable.drawImage(suspectNoi, x,y,tailleC,tailleC,null);
                break;
            case ROSE:
                drawable.drawImage(suspectRos, x,y,tailleC,tailleC,null);
                break;
            case VERT:
                drawable.drawImage(suspectVer, x,y,tailleC,tailleC,null);
                break;
            case BLANC:
                drawable.drawImage(suspectBla, x,y,tailleC,tailleC,null);
                break;
            case JAUNE:
                drawable.drawImage(suspectJau, x,y,tailleC,tailleC,null);
                break;
            case ORANGE:
                drawable.drawImage(suspectOra, x,y,tailleC,tailleC,null);
                break;
            case VIOLET:
                drawable.drawImage(suspectVio, x,y,tailleC,tailleC,null);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + jeu.plateau().idJack);
        }
    }
    public void dessinerMain(){
        Iterator<CarteAlibi> main;

        drawable.setFont(new Font("default", Font.BOLD, 25));
        if(afficherEnqueteur){
            main = jeu.plateau().enqueteur.getCardList().iterator();
            drawable.drawString("Main de l'enquêteur :",10,25);
        }
        else {
            main = jeu.plateau().jack.getCardList().iterator();
            drawable.drawString("Main de Jack :",10,25);
        }
        int i=0;
        CarteAlibi actuel;
        while(main.hasNext()){
            actuel = main.next();
            dessinerCarte(actuel.getSuspect().getCouleur(),i* (int) (tailleC*0.85),45);
            i++;
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
        int hCase=hauteur-45;
        int lCase=(int)(largeur*1.15)/8;
        tailleC=Math.min(hCase,lCase);

        dessinerMain();
    }

    public void changerMain(){
        afficherEnqueteur = !afficherEnqueteur;
    }

    public boolean getAfficherEnqueteur(){
        return afficherEnqueteur;
    }
}
