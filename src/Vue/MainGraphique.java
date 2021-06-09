package Vue;

import Global.Configuration;
import Modele.CarteAlibi;
import Modele.Jeu;
import Modele.SuspectCouleur;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class MainGraphique extends JComponent {
    Graphics2D drawable;
    int largeur,hauteur,tailleC;
    Jeu jeu;
    Image suspectBla, suspectBle, suspectJau, suspectNoi, suspectOra, suspectRos, suspectVer, suspectVio, suspectGri;
    private boolean afficherEnqueteur;
    //ArrayList<CarteAlibi> mainFictif;

    MainGraphique(Jeu j){
        jeu=j;
        suspectBle = Configuration.chargeImage("Suspect-bleuB");
        suspectGri = Configuration.chargeImage("Suspect-marronB");
        suspectNoi = Configuration.chargeImage("Suspect-noirB");
        suspectRos = Configuration.chargeImage("Suspect-roseB");
        suspectVer = Configuration.chargeImage("Suspect-vertB");
        suspectBla = Configuration.chargeImage("Suspect-blancB");
        suspectJau = Configuration.chargeImage("Suspect-jauneB");
        suspectOra = Configuration.chargeImage("Suspect-orangeB");
        suspectVio = Configuration.chargeImage("Suspect-violetB");
        setAfficherEnqueteur(true);
        /*mainFictif = new ArrayList<CarteAlibi>();
        mainFictif.add( new CarteAlibi( new Suspect( SuspectNom.JEREMY_BERT,null ) ) );
        mainFictif.add( new CarteAlibi( new Suspect( SuspectNom.MADAME,null ) ) );
        mainFictif.add( new CarteAlibi( new Suspect( SuspectNom.JOHN_PIZER,null ) ) );
        mainFictif.add( new CarteAlibi( new Suspect( SuspectNom.INSPECTOR_LESTRADE,null ) ) );
        mainFictif.add( new CarteAlibi( new Suspect( SuspectNom.SERGENT_GOODLEY,null ) ) );
        mainFictif.add( new CarteAlibi( new Suspect( SuspectNom.WILLIAM_GULL,null ) ) );
        mainFictif.add( new CarteAlibi( new Suspect( SuspectNom.MISS_STEALTHY,null ) ) );*/
    }

    //Pre-Condition : Jeu initialisé
    //Post-Condition : dessine une carte suspect
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

    //Pre-Condition : Jeu initialisé
    //Post-Condition : dessine toutes les cartes d'une main
    public void dessinerMain(){
        Iterator<CarteAlibi> main;
        //jeu.plateau().enqueteur.setCardList(mainFictif);
        drawable.setFont(new Font("default", Font.PLAIN, 18));
        drawable.setColor(Color.BLACK);
        if(!isAfficherEnqueteur() || jeu.plateau().finJeu(false,false) ){
            main = jeu.plateau().jack.getCardList().iterator();
            drawable.drawString("Main de Jack :",10,25);
        }
        else {
            main = jeu.plateau().enqueteur.getCardList().iterator();
            drawable.drawString("Main de l'enquêteur :",10,25);
        }
        int i=0;
        CarteAlibi actuel;
        while(main.hasNext()){
            actuel = main.next();
            dessinerCarte(actuel.getSuspect().getCouleur(),(i%3)* (int) (tailleC*0.85),(i/3)*tailleC+45);
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
        //drawable.clearRect(0, 0, largeur, hauteur);
        drawable.setBackground(new Color(255,255,255,0));
        g.setColor(new Color(255,255,255,0));
        g.fillRect(0, 0, getWidth(),getHeight());

        //Calcul de la taille d'une case
        int hCase=(hauteur-45)/3;
        int lCase=(int)(largeur*1.17)/3;
        tailleC=Math.min(hCase,lCase);

        dessinerMain();
    }

    public void changerMain(){
        setAfficherEnqueteur(!isAfficherEnqueteur());
        repaint();
    }

    public boolean getAfficherEnqueteur(){
        return isAfficherEnqueteur();
    }

    public boolean isAfficherEnqueteur() {
        return afficherEnqueteur;
    }

    public void setAfficherEnqueteur(boolean afficherEnqueteur) {
        this.afficherEnqueteur = afficherEnqueteur;
        repaint();
    }
}
