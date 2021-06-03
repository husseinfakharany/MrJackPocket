package Controle;

import Global.Configuration;
import Modele.*;
import Modele.Action;
import Vue.AdaptateurTemps;
import Vue.InterfaceGraphique;

import javax.swing.*;

public class ControleurMediateur implements CollecteurEvenements {
    InterfaceGraphique ig;
    Jeu jeu;
    IA ia;
    boolean iaActive;
    boolean iaIsJack;
    boolean iaJoue;
    int selectionne = -1;

    Action action;
    Coup cp;

    Timer tempsIA;

    public ControleurMediateur(Jeu j) {
        jeu = j;
        iaActive = false;
        iaJoue = false;
        action = new Action(jeu.plateau().joueurCourant);
        cp = new Coup(jeu.plateau(),action);
    }

    public boolean iaActive(){
        return iaActive;
    }

    public void fixerIA(String com){
        iaActive = true;
        switch (com){
            case "facile":
                ia = new IaAleatoire(jeu,iaIsJack);
                break;
            case "moyen":
                try{
                    ia = new IAMeilleureProchain(jeu.clone(),iaIsJack);
                }catch(Exception e){
                    Configuration.instance().logger().severe("Erreur de clonage du jeu");
                    System.exit(-1);
                }
                break;
            case "difficile":

                break;
            default:
                //throw new NoSuchElementException();
        }
        tempsIA = new Timer(3000,new AdaptateurTemps(ia,jeu,this));
        tempsIA.setRepeats(false);
    }

    public void activeIA(int state){

    }

    @Override
    public void jouerCoupIA(Coup cp){
        if(iaActive){
            this.cp = cp;
            action = cp.getAction();
            selectionne = cp.getAction().getNumAction();
            tempsIA.stop();
            iaJoue = false;
            jouerCoup();
        }
    }

    public void appliquer(int i){

        //Reinitialisation
        reinitialiser();

        //Désaffichage des feedback précédents
        if(!jeu.plateau().finJeu(false,true) && !jeu.plateau().tousJetonsJoues()){
            ig.dessinerInfo(InterfaceGraphique.texteIndicatif(null));
        }
        if(jeu.plateau().tousJetonsJoues()){
            ig.getDistrict().setAfficherVisible(true);
        }

        if(i==1){
            if(jeu.plateau().joueurCourant.equals(jeu.plateau().jack) && iaActive && iaIsJack && jeu.plateau().getNumAction() <4){
                tempsIA.restart();
                iaJoue = true;
                ig.dessinerInfo("Ia joue son coup");
            }
            if(jeu.plateau().joueurCourant.equals(jeu.plateau().enqueteur) && iaActive && !iaIsJack  && jeu.plateau().getNumAction() <4){
                tempsIA.restart();
                iaJoue = true;
                ig.dessinerInfo("Ia joue son coup");
            }
            if(!ig.getMain().getAfficherEnqueteur() && jeu.plateau().joueurCourant.equals(jeu.plateau().enqueteur ) )
                commandeMenu("main");
        }
    }



    @Override
    public void annuler() {
        cp = jeu.annule();
        if(cp != null){
            action = cp.getAction();
            selectionne = action.getNumAction();
            appliquer(-1);
        }

    }

    @Override
    public void refaire() {
        cp = jeu.refaire();
        if(cp != null){
            action = cp.getAction();
            selectionne = action.getNumAction();
            appliquer(1);
        }
    }

    @Override
    public void commandeDistrict(int l, int c){
        if(iaJoue || cp==null)return;

        if(l>=1 && l <= 3 && c>=1 && c<=3 && (cp.getAction().getAction() == Actions.ROTATION_DISTRICT
                || cp.getAction().getAction() != Actions.ECHANGER_DISTRICT || cp.getAction().getAction() != Actions.INNOCENTER_CARD) )
        cp.ajouterArguments(l,c);
        else if ( l==0 || l==4 || c==0 || c==4 && (cp.getAction().getAction() == Actions.DEPLACER_JOKER  || cp.getAction().getAction() == Actions.DEPLACER_SHERLOCK
                || cp.getAction().getAction() == Actions.DEPLACER_WATSON || cp.getAction().getAction() == Actions.DEPLACER_TOBBY) )
            cp.ajouterArguments(l,c);
        action.setJoueur(jeu.plateau().joueurCourant);
        ig.getJetons().dessinerValide(action.estValide());
        ig.getDistrict().dessinerFeedback(action);
        ig.dessinerInfo(InterfaceGraphique.texteIndicatif(action));
    }

    @Override
    public boolean commandeJeu(String c){

        if (cp==null){
            action = new Action(jeu.plateau().joueurCourant);
            cp = new Coup(jeu.plateau(),action);
        }

        if(jeu.plateau().tousJetonsJoues() ){
            boolean partiFini = jeu.plateau().prochainTour();
            if(!partiFini){
                ig.dessinerInfo(InterfaceGraphique.texteIndicatif(action));
                demarrerIA();
                ig.getDistrict().setAfficherVisible(false);
            }else{
                jeu.plateau().setAfficherVerdict(true);
            }
            return true;
        }

        if(iaJoue)return false;
        switch (c){
            case "jetonA":
                if(action.estValide() && selectionne == 0 && action.getAction().equals(jeu.plateau().getActionJeton(0)) ){
                    if (!jeu.plateau().getJeton(0).getDejaJoue()){
                        jouerCoup();
                        return true;
                    } else {
                        Configuration.instance().logger().warning("L'action était déjà jouée");
                    }
                }
                if (!jeu.plateau().getJeton(0).getDejaJoue() && selectionne != 0){
                    ig.getJetons().dessinerSelection(1);
                    action.setAction(jeu.plateau().getActionJeton(0));
                    selectionne = 0;
                } else if (!jeu.plateau().getJeton(0).getDejaJoue() && selectionne == 0){
                    ig.getJetons().dessinerSelection(-1);
                    action.reinitialiser();
                    selectionne = -1;
                }
                break;
            case "jetonB":
                if(action.estValide() && selectionne == 1 && action.getAction().equals(jeu.plateau().getActionJeton(1)) ) {
                    if (!jeu.plateau().getJeton(1).getDejaJoue()) {
                        jouerCoup();
                        return true;
                    } else {
                        Configuration.instance().logger().warning("L'action était déjà jouée");
                    }
                }
                if (!jeu.plateau().getJeton(1).getDejaJoue() && selectionne != 1){
                    ig.getJetons().dessinerSelection(2);
                    action.setAction(jeu.plateau().getActionJeton(1));
                    selectionne = 1;
                } else if (!jeu.plateau().getJeton(1).getDejaJoue() && selectionne == 1 ){
                    ig.getJetons().dessinerSelection(-1);
                    action.reinitialiser();
                    selectionne = -1;
                }
                if(action.getAction() != null && action.getAction().equals(Actions.DEPLACER_JOKER))action.setNumEnqueteur(0);

                break;
            case "jetonC":
                if(action.estValide() && selectionne == 2 && action.getAction().equals(jeu.plateau().getActionJeton(2)) ){
                    if (!jeu.plateau().getJeton(2).getDejaJoue()) {
                        jouerCoup();
                        return true;
                    } else {
                        Configuration.instance().logger().warning("L'action était déjà jouée");
                    }
                }
                if (!jeu.plateau().getJeton(2).getDejaJoue() && selectionne != 2) {
                    ig.getJetons().dessinerSelection(3);
                    action.setAction(jeu.plateau().getActionJeton(2));
                    selectionne = 2;
                } else if (!jeu.plateau().getJeton(2).getDejaJoue() && selectionne == 2){
                    ig.getJetons().dessinerSelection(-1);
                    action.reinitialiser();
                    selectionne = -1;
                }
                break;
            case "jetonD":
                if(action.estValide() && selectionne == 3 && action.getAction().equals(jeu.plateau().getActionJeton(3)) && !action.getAction().equals(Actions.INNOCENTER_CARD)){
                    if (!jeu.plateau().getJeton(3).getDejaJoue()) {
                        jouerCoup();
                        return true;
                    } else {
                        Configuration.instance().logger().warning("L'action était déjà jouée");
                    }
                }
                if (!jeu.plateau().getJeton(3).getDejaJoue() && selectionne != 3){
                    ig.getJetons().dessinerSelection(4);
                    action.setAction(jeu.plateau().getActionJeton(3));
                    selectionne = 3;
                }else if (!jeu.plateau().getJeton(3).getDejaJoue() && selectionne == 3 ){
                    ig.getJetons().dessinerSelection(-1);
                    action.reinitialiser();
                    selectionne = -1;
                }
                break;
            case "pioche":
                if(action.getAction() != null && action.getAction().equals(jeu.plateau().getActionJeton(3)) && action.getAction().equals(Actions.INNOCENTER_CARD)) {
                    if (!jeu.plateau().getJeton(3).getDejaJoue()) {
                        jouerCoup();
                        return true;
                    } else {
                        Configuration.instance().logger().warning("L'action était déjà jouée");
                    }
                }
                break;
        }
        action.setNumAction(selectionne);
        action.setJoueur(jeu.plateau().joueurCourant);
        ig.getJetons().dessinerValide(action.estValide() && !action.getAction().equals(Actions.INNOCENTER_CARD));
        if(action.getAction() != null) ig.getPioche().setPiocheActive(action.getAction().equals(Actions.INNOCENTER_CARD));
        ig.getDistrict().dessinerFeedback(action);
        ig.dessinerInfo(InterfaceGraphique.texteIndicatif(action));
        return true;
    }


    public void jouerCoup(){
        if (jeu.jouerCoup(cp)){
            Configuration.instance().logger().info("Coup joué");
            appliquer(1);
        } else {
            Configuration.instance().logger().warning("Coup invalide");
        }
    }

    void reinitialiser(){
        ig.getJetons().dessinerSelection(-1);
        ig.getJetons().dessinerValide(false);
        ig.getDistrict().dessinerFeedback(null);
        selectionne = -1;
        cp = null;
        ig.getPioche().setPiocheActive(false);
        if(!jeu.plateau().tousJetonsJoues()) ig.dessinerInfo(InterfaceGraphique.texteIndicatif(action));
        if(!ig.getMain().getAfficherEnqueteur()) commandeMenu("main");
        ig.getMain().setAfficherEnqueteur(true);
    }

    void reinitialiserIA(){
        ia = null;
        iaActive = false;
        if(tempsIA != null)tempsIA.stop();
    }

    @Override
    public boolean commandeMenu(String c) {
        switch (c) {
            case "quitter":
                System.exit(0);
                break;
            case "IA contre IA":
                Configuration.instance().logger().info("Partie IA contre IA");
                //ig.changerMenu(ig.getBoiteMenu(),ig.getBoiteAvantPartie());
                break;
            case"Contre une IA":
                Configuration.instance().logger().info("Partie contre une IA");
                ig.changerMenu(ig.getBoiteMenu(),ig.getBoiteAvantPartieIA());
                break;
            case "jack":
                ig.refreshBoiteAvantPartieIA();
                iaIsJack = false;
                break;
            case "sherlock":
                ig.refreshBoiteAvantPartieIA();
                iaIsJack = true;
                break;
            case "facile":
                fixerIA(c);
                iaActive = true;
                ig.refreshBoiteAvantPartieIA();
                break;
            case "moyen":
                Configuration.instance().logger().info("Moyenne");
                //fixerIA(c);
                //iaActive = true;
                break;
            case "difficile":
                Configuration.instance().logger().info("Difficile");
                //fixerIA(c);
                //iaActive = true;
                break;
            case "commencerIA":
                jeu.plateau().reinitialiser();
                this.reinitialiser();
                ig.changerMenu(ig.getBoiteAvantPartieIA(), ig.getBoiteJeu());
                demarrerIA();
                break;
            case "Contre un ami":
                ig.changerMenu(ig.getBoiteMenu(), ig.getBoiteJeu());
                jeu.plateau().reinitialiser();
                this.reinitialiser();
                this.reinitialiserIA();
                break;
            case "quitterJ":
                ig.quitterJeu();
                break;
            case "menuJ":
                ig.changerMenu(ig.getBoiteJeu(), ig.getBoiteMenu() );
                break;
            case "retourMenu":
                ig.changerMenu(ig.getBoiteAvantPartieIA(), ig.getBoiteMenu() );
                break;
            case "menuC":
                ig.changerMenu(ig.getBoiteCharger(), ig.getBoiteMenu() );
                break;
            case "menuCharger":
                ig.changerMenu(ig.getBoiteMenu(), ig.getBoiteCharger() );
                break;
            case "charger":
                Configuration.instance().logger().info("Chargement de la partie...");
                break;
            case "annuler":
                annuler();
                break;
            case "refaire":
                refaire();
                break;
            case "main":
                ig.getMain().changerMain();
                ig.getIdentite().switchAfficherCaches();
                if(jeu.plateau().joueurCourant.equals(jeu.plateau().jack))ig.getIdentite().swapImageJack();
                if(ig.getMain().getAfficherEnqueteur()) ig.getVoirJack().setText("Voir mes cartes et ⏳");
                else ig.getVoirJack().setText("Cacher mes cartes et ⏳");
                break;
            case "tuto":
                ig.changerMenu(ig.getBoiteMenu(),ig.getBoiteTuto());
                break;
            case "nextTuto":
                ig.getTuto().tutorielNext();
                break;
            case "previousTuto":
                ig.getTuto().tutorielPrevious();
                break;
            case "retourMenuTuto":
                ig.changerMenu(ig.getBoiteTuto(), ig.getBoiteMenu() );
                break;
            default:
                return false;
        }
        return true;
    }

    private void demarrerIA() {
        if(!iaIsJack && iaActive && jeu.plateau().joueurCourant.equals(jeu.plateau().enqueteur)){
            tempsIA.restart();
            iaJoue =true;
            ig.dessinerInfo("Ia joue son coup");
        }
        if(iaIsJack && iaActive && jeu.plateau().joueurCourant.equals(jeu.plateau().jack)){
            tempsIA.restart();
            iaJoue =true;
            ig.dessinerInfo("Ia joue son coup");
        }
    }

    @Override
    public void fixerInterfaceUtilisateur(InterfaceGraphique i) {
        ig=i;
    }
}
