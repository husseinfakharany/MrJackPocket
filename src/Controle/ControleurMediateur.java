package Controle;

import Global.Configuration;
import Modele.*;
import Vue.InterfaceGraphique;

//TODO fonctions fixerIA(String com) activeIA(int state) undo() redo()
public class ControleurMediateur implements CollecteurEvenements {
    InterfaceGraphique ig;
    Jeu jeu;
    boolean iaActive;
    boolean iaPeutJouer;
    int selectionne = -1;

    Action action;
    Coup cp;
    public ControleurMediateur(Jeu j) {
        jeu = j;
        iaActive = false;
        action = new Action(jeu.plateau().joueurCourant);
        cp=new Coup(jeu.plateau(),action);
    }

    public boolean iaActive(){
        return iaActive;
    }

    public void fixerIA(String com){

    }

    public void activeIA(int state){

    }

    public void clicSouris(int l, int c) {
        System.out.println("Clic en ( l : " +l+" , c : "+c+" )" );
    }

    public void jouerCoupIA(Coup cp){
        if(iaPeutJouer)
            jeu.jouerCoup(cp);
    }

    @Override
    public void annuler() {
        jeu.annule();
    }

    @Override
    public void refaire() {
        jeu.refaire();
    }

    @Override
    public void commandeDistrict(int l, int c){
        if(l>=1 && l <= 3 && c>=1 && c<=3)cp.ajouterArguments(l-1,c-1);
        else{
            cp.ajouterArguments(l,c);
        }
        action.setJoueur(jeu.plateau().joueurCourant);
        ig.getJetons().setJouable(action.estValide());
        ig.getDistrict().setActionTemp(action);
    }

    @Override
    public boolean commandeJeu(String c){
        action.setJoueur(jeu.plateau().joueurCourant);
        switch (c){
            case "jetonA":
                if(action.estValide() && action.getAction().equals(jeu.plateau().getActionJeton(0)) ){
                    if (!jeu.plateau().getJeton(0).getDejaJoue()){
                        jouerCoup();
                    } else {
                        Configuration.instance().logger().warning("L'action était déjà jouée");
                    }
                }
                action.setAction(jeu.plateau().getActionJeton(0));
                selectionne=0;
                break;
            case "jetonB":
                if(action.estValide() && action.getAction().equals(jeu.plateau().getActionJeton(1)) ) {
                    if (!jeu.plateau().getJeton(1).getDejaJoue()) {
                        jouerCoup();
                    } else {
                        Configuration.instance().logger().warning("L'action était déjà jouée");
                    }
                }
                action.setAction(jeu.plateau().getActionJeton(1));
                selectionne=1;
                break;
            case "jetonC":
                if(action.estValide() && action.getAction().equals(jeu.plateau().getActionJeton(2)) ){
                    if (!jeu.plateau().getJeton(2).getDejaJoue()) {
                        jouerCoup();
                    } else {
                        Configuration.instance().logger().warning("L'action était déjà jouée");
                    }
                }
                action.setAction(jeu.plateau().getActionJeton(2));
                selectionne=2;
                break;
            case "jetonD":
                if(action.estValide() && action.getAction().equals(jeu.plateau().getActionJeton(3)) && !action.getAction().equals(Actions.INNOCENTER_CARD)){
                    if (!jeu.plateau().getJeton(3).getDejaJoue()) {
                        jouerCoup();
                    } else {
                        Configuration.instance().logger().warning("L'action était déjà jouée");
                    }
                }
                action.setAction(jeu.plateau().getActionJeton(3));
                selectionne=3;
                break;
            case "pioche":
                if(action.estValide() && action.getAction().equals(jeu.plateau().getActionJeton(3)) && action.getAction().equals(Actions.INNOCENTER_CARD)) {
                    if (!jeu.plateau().getJeton(3).getDejaJoue()) {
                        jouerCoup();
                    } else {
                        Configuration.instance().logger().warning("L'action était déjà jouée");
                    }
                }
                selectionne=3;
                break;
        }
        ig.getJetons().setJouable(action.estValide());
        ig.getJetons().repaint();
        ig.getPioche().setPiocheActive(action.getAction().equals(Actions.INNOCENTER_CARD));
        return true;
    }

    public void jouerCoup(){
        System.out.println("Coup joué");
        jeu.jouerCoup(cp);
        //TODO to be modified for historique
        jeu.plateau().setNumAction(jeu.plateau().getNumAction()+1);
        jeu.plateau().actionJouee();
        jeu.plateau().getJeton(selectionne).setDejaJoue(true);
        ig.getJetons().setJouable(false);
        cp.reinitialiser();
        ig.getBoiteJeu().repaint();

    }

    @Override
    public boolean commandeMenu(String c) {
        switch (c) {
            case "quitter":
                System.exit(0);
                break;
            case "commencer":
            case "Enquêteur":
            case "Meurtrier":
                ig.changerMenu(ig.getBoiteMenu(),ig.getBoiteAvantPartie());
                break;
            case "Ordi Vs Ordi":
                System.out.println("Partie Ordi Vs Ordi");
                break;
            case "Facile":
            case "Moyenne":
            case "Difficile":
                ig.changerMenu(ig.getBoiteAvantPartie(), ig.getBoiteJeu());
                jeu.plateau().reinitialiser();
                ig.getJetons().setSelection(-1);
                fixerIA(c);
                break;
            case "local":
                ig.changerMenu(ig.getBoiteAvantPartie(), ig.getBoiteJeu());
                jeu.plateau().reinitialiser();
                ig.getJetons().setSelection(-1);
                break;
            case "reseau":
                System.out.println("Attente d'une partie .");
                System.out.println("Attente d'une partie ..");
                System.out.println("Attente d'une partie ...");
                ig.changerMenu(ig.getBoiteAvantPartie(), ig.getBoiteJeu());
                break;
            case "menuJ":
                ig.changerMenu(ig.getBoiteJeu(), ig.getBoiteMenu() );
                break;
            case "menuC":
                ig.changerMenu(ig.getBoiteCharger(), ig.getBoiteMenu() );
                break;
            case "menuCharger":
                ig.changerMenu(ig.getBoiteMenu(), ig.getBoiteCharger() );
                break;
            case "charger":
                System.out.println("Chargement de la partie...");
                break;
            case "annuler":
                annuler();
                break;
            case "refaire":
                refaire();
                break;
            case "main":
                ig.getMain().changerMain();
                if(ig.getMain().getAfficherEnqueteur()) ig.getVoirJack().setText("Voir mes cartes");
                else ig.getVoirJack().setText("Cacher mes cartes");
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void fixerInterfaceUtilisateur(InterfaceGraphique i) {
        ig=i;
    }
}
