package Controle;

import Global.Configuration;
import Modele.*;
import Modele.Action;
import Vue.AdaptateurTemps;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ControleurMediateur implements CollecteurEvenements {
    InterfaceGraphique ig;
    Jeu jeu;
    Jeu jeuClone;
    IA ia;
    IAMoyen iaJ,iaS;
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


    //Pre-condition : L'argument doit etre "facile", "moyen" ou "difficile"
    //Post-condition : La variable ia est initialisee a la difficulté souhaité
    public void fixerIA(String com){
        iaActive = true;
        switch (com){
            case "facile":
                ia = new IAAleatoire(jeu,iaIsJack);
                tempsIA = new Timer(3000,new AdaptateurTemps(ia,this));
                tempsIA.setRepeats(false);
                break;
            case "moyen":
                try{
                    jeuClone = jeu.clone();
                    ia = new IAMoyen(jeuClone,iaIsJack);
                    tempsIA = new Timer(1000,new AdaptateurTemps(ia,this));
                    tempsIA.setRepeats(false);
                }catch(Exception e){
                    Configuration.instance().logger().severe("Erreur de clonage du jeu");
                    System.exit(-1);
                }
                break;
            case "difficile":
                try{
                    jeuClone = jeu.clone();
                    ia = new IADifficile(jeuClone,iaIsJack);
                    tempsIA = new Timer(1000,new AdaptateurTemps(ia,this));
                    tempsIA.setRepeats(false);
                }catch(Exception e){
                    Configuration.instance().logger().severe("Erreur de clonage du jeu");
                    System.exit(-1);
                }
                break;
            default:
                throw new NoSuchElementException();
        }

    }

    //Pre-condition : L'argument doit etre "facile", "moyen" ou "difficile"
    //Post-condition : La variable ia est initialisee a la difficulté souhaité et le timer de temporisation
    @Override
    public void jouerCoupIA(Coup cp){
        if(iaActive){
            this.cp = cp;
            action = cp.getAction();
            selectionne = cp.getAction().getNumAction();
            tempsIA.stop();
            iaJoue = false;
            cp.setPlateau(jeu.plateau());
            jouerCoup();
        }
    }

    //Pre-condition : jeuClone non nul
    //Post-condition : Applique le coup sur la copie du jeu
    public void appliquerIA(){
        jeuClone.plateau().finJeu(false,true);
    }

    //Pre-condition : Jeu et ig non nul. Ia et TempsIA initialisés par fixerIA
    //Post-condition : Reinitialisation des feedback et feedforward de l'IHM, affiche les personnages éliminés en fin de tour
    //lance le timer de l'IA pour demander un coup à l'IA
    public void appliquer(int i){

        //Reinitialisation
        reinitialiser();

        //Désaffichage des feedback précédents
        if(!jeu.plateau().finJeu(false,true) && !jeu.plateau().tousJetonsJoues()){
            ig.dessinerInfo(InterfaceGraphique.texteIndicatif(null));
        }
        if(jeu.plateau().tousJetonsJoues()){
            ig.getDistrict().setAfficherVisible(true);
            ig.getUndo().setEnabled(false);
            ig.getRedo().setEnabled(false);
        }

        if(i==1){
            if(jeu.plateau().joueurCourant.equals(jeu.plateau().jack) && iaActive && iaIsJack && jeu.plateau().getNumAction() <4 && jeu.plateau().getNumAction()!=0){
                tempsIA.restart();
                iaJoue = true;
                ig.dessinerInfo("Ia joue son coup");
            }
            if(jeu.plateau().joueurCourant.equals(jeu.plateau().enqueteur) && iaActive && !iaIsJack  && jeu.plateau().getNumAction() <4 && jeu.plateau().getNumAction()!=0){
                tempsIA.restart();
                iaJoue = true;
                ig.dessinerInfo("Ia joue son coup");
            }
            if(!ig.getMain().getAfficherEnqueteur() && jeu.plateau().joueurCourant.equals(jeu.plateau().enqueteur ) )
                commandeMenu("main");
        }
    }

    //Pre-condition : Vide
    //Post-condition : Verifie l'existence d'un coup dans l'historique et l'annule si il existe
    @Override
    public void annuler() {
        cp = jeu.annule();
        reinitialiser();
        jeu.notifierObserveurs();
        if(cp != null){
            action = cp.getAction();
            selectionne = action.getNumAction();
            appliquer(-1);
            ig.getDistrict().setAfficherVisible(false);
        }
    }

    //Pre-condition : Vide
    //Post-condition : Verifie l'existence d'un coup dans l'historique et le rejoue si il existe
    @Override
    public void refaire() {
        cp = jeu.refaire();
        reinitialiser();
        jeu.notifierObserveurs();
        if(cp != null){
            action = cp.getAction();
            selectionne = action.getNumAction();
            appliquer(1);
        }
    }

    //Pre-condition : l>=0 et l <= 4 ET c >= 0 et c <= 4
    //Post-condition : Ajouter les arguments des coups à jouer en fonction des actions et des clics sur le district.
    //Appeler par AdaptateurSouris
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
        ig.getDistrict().dessinerFeedback(cp.getAction());
        ig.dessinerInfo(InterfaceGraphique.texteIndicatif(action));
    }

    //Pre-condition : Argument "jetonA", "jetonB", "jetonC" et "jetonD"
    //Post-condition : Initialise le coup en fonction du jeton sur lequel on a cliqué
    //Appeler par AdaptateurSouris
    @Override
    public boolean commandeJeu(String c){
        if (cp==null){
            action = new Action(jeu.plateau().joueurCourant);
            cp = new Coup(jeu.plateau(),action);
        }

        if(jeu.plateau().tousJetonsJoues() ){
            boolean partiFini = jeu.plateau().prochainTour();
            if(iaActive) jeuClone.plateau().prochainTourClone(jeu);
            if(jeu.plateau().joueurCourant.equals(jeu.plateau().jack) && iaActive && iaIsJack && jeu.plateau().getNumAction()==0){
                tempsIA.restart();
                iaJoue = true;
                ig.dessinerInfo("Ia joue son coup");
            }
            if(jeu.plateau().joueurCourant.equals(jeu.plateau().enqueteur) && iaActive && !iaIsJack && jeu.plateau().getNumAction()==0){
                tempsIA.restart();
                iaJoue = true;
                ig.dessinerInfo("Ia joue son coup");
            }
            if(!partiFini){
                ig.dessinerInfo(InterfaceGraphique.texteIndicatif(action));
                demarrerIA();
                ig.getDistrict().setAfficherVisible(false);
                ig.getUndo().setEnabled(true);
                ig.getRedo().setEnabled(true);
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

    //Pre-Condition : cp non nul
    //Post-Condition : Joue le coup sur le jeu lié au controlleur, met à jour les jetons de la copie de jeu de l'IA si active
    public boolean jouerCoup(){
        if (jeu.jouerCoup(cp)){
            Configuration.instance().logger().info("Coup joué");
            appliquer(1);
            if (iaActive){
                jeuClone.plateau().setJetonsActions(jeu.plateau().jetonsActions);
                Action actionIA = jeu.plateau().passe.get(0).getAction();
                Coup cpIA = new Coup(jeuClone.plateau(),actionIA);
                if (actionIA.getAction()==Actions.INNOCENTER_CARD){
                    actionIA.setIa(true);
                }
                if (jeuClone.jouerCoup(cpIA)){
                    Configuration.instance().logger().info("Coup joué dans IA");
                    appliquerIA();
                }
            }
            return true;
        } else {
            Configuration.instance().logger().warning("Coup invalide");
            return false;
        }
    }

    //Pre-Condition : cp,jeu et ig non nul
    //Post-Condition : Reinitialise les affichages feedback
    void reinitialiser(){
        ig.getJetons().dessinerSelection(-1);
        ig.getJetons().dessinerValide(false);
        ig.getDistrict().dessinerFeedback(null);
        ig.getDistrict().setAfficherVisible(false);
        selectionne = -1;
        cp = null;
        ig.getPioche().setPiocheActive(false);
        if(!jeu.plateau().tousJetonsJoues()) ig.dessinerInfo(InterfaceGraphique.texteIndicatif(action));
        ig.getMain().setAfficherEnqueteur(true);
        ig.getIdentite().resetIdJack();
    }

    //Pre-Condition : vide
    //Post-Condition : désactive l'IA (Doit etre reinitialise pour être utilisé)
    void reinitialiserIA(){
        ia = null;
        iaActive = false;
        if(tempsIA != null)tempsIA.stop();
    }

    //Pre-Condition : c non nul
    //Post-Condition : Permet d'executer des actions souhaité en fonction du bouton qui l'appelle
    @Override
    public boolean commandeMenu(String c) {
        switch (c) {
            case "quitter":
                System.exit(0);
                break;
            case "IA contre IA":
                Configuration.instance().logger().info("Partie IA contre IA");
                ig.changerMenu(ig.getBoiteMenu(),ig.getBoiteIAvsIA());
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
                fixerIA(c);
                iaActive = true;
                ig.refreshBoiteAvantPartieIA();
                break;
            case "difficile":
                Configuration.instance().logger().info("Difficile");
                fixerIA(c);
                iaActive = true;
                ig.refreshBoiteAvantPartieIA();
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
                if(!ig.getSaveList().isSelectionEmpty()){
                    String home = System.getProperty("user.home");
                    File f = new File(home + File.separator + "JackPocket" + File.separator + ig.getSaveList().getSelectedValue());
                    jeu.charger(f);
                }
                reinitialiser();
                reinitialiserIA();
                ig.changerMenu(ig.getBoiteCharger(), ig.getBoiteJeu());
                break;
            case "annuler":
                annuler();
                break;
            case "refaire":
                refaire();
                break;
            case "main":
                if(jeu.plateau().joueurCourant.equals(jeu.plateau().jack)){
                    ig.getIdentite().swapImageJack();
                    ig.getMain().changerMain();
                }
                if(ig.getMain().getAfficherEnqueteur()) ig.getVoirJack().setText("Voir mes cartes et ⏳");
                else ig.getVoirJack().setText("Cacher mes cartes et ⏳");
                break;
            case "tuto":
                ig.changerMenu(ig.getBoiteMenu(),ig.getBoiteTuto());
                break;
            case "nextTuto":
                ig.getTuto().tutorielNext();
                ig.tutoPlus.setEnabled(ig.getTuto().i < 8);
                ig.tutoMoins.setEnabled(ig.getTuto().i > 0);
                break;
            case "previousTuto":
                ig.getTuto().tutorielPrevious();
                ig.tutoPlus.setEnabled(ig.getTuto().i < 8);
                ig.tutoMoins.setEnabled(ig.getTuto().i > 0);
                break;
            case "retourMenuTuto":
                ig.changerMenu(ig.getBoiteTuto(), ig.getBoiteMenu() );
                break;
            case "LancerIAvsIA":
                try {
                    iaJ = new IAMoyen(jeu.clone(), true);
                    iaS = new IAMoyen(jeu.clone(), false);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                iaActive = false;
                iaJ.setCoeff(ig);
                iaS.setCoeff(ig);
                jeu.plateau().reinitialiser();
                this.reinitialiser();
                jouerIAvsIA(ig.getNbParties());
                ig.frame.dispatchEvent(new WindowEvent(ig.frame, WindowEvent.WINDOW_CLOSING));
                break;
            default:
                return false;
        }
        return true;
    }

    //Pre-Condition : IA initialisé
    //Post-Condition : Permet d'appeler l'IA en début de partie si initialisé
    private void demarrerIA() {
        if((!iaIsJack && iaActive && jeu.plateau().joueurCourant.equals(jeu.plateau().enqueteur)) || (iaIsJack && iaActive && jeu.plateau().joueurCourant.equals(jeu.plateau().jack))){
            jeuClone.plateau().setJetonsActions(jeu.plateau().jetonsActions);
            tempsIA.restart();
            iaJoue =true;
            ig.dessinerInfo("Ia joue son coup");
        }
    }

    //Pre-Condition : vide
    //Post-Condition : Permet à deux IA de s'affronter (iaJ et iaS doivent être initialisé)
    //Appeler après l'initialisations des IAs dans commandeMenu
    private void jouerIAvsIA(int nbPartie){
        int victoireJack =0, victoireSherlock =0, nbEgalité=0;

        for(int i=0; i < nbPartie; i++){
            cp = null;
            action.setNumEnqueteur(-1);
            jeu.plateau().reinitialiser();
            iaJ.j.plateau().reinitialiser();
            iaS.j.plateau().reinitialiser();
            while(!jeu.plateau().finJeu(false,false)){
                if(jeu.plateau().joueurCourant.equals(jeu.plateau().jack)){
                    coupIAJack();
                } else if(jeu.plateau().joueurCourant.equals(jeu.plateau().enqueteur)){
                    coupIASherlock();
                }
            }
            if(jeu.plateau().jack.getWinner() && !jeu.plateau().enqueteur.getWinner()) {
                victoireJack++;
                Configuration.instance().logger().info("Fin de la partie n°"+i+" : Vainqueur : JACK");
            } else if(jeu.plateau().enqueteur.getWinner() && !jeu.plateau().jack.getWinner()){
                victoireSherlock++;
                Configuration.instance().logger().info("Fin de la partie n°"+i+" : Vainqueur : SHERLOCK");
            } else {
                nbEgalité++;
                Configuration.instance().logger().info("Fin de la partie n°"+i+" : Vainqueur : EGALITE");
            }
        }
        Configuration.instance().logger().info("Jack a gagné " + victoireJack + " parties sur " + (victoireJack+victoireSherlock+nbEgalité) );
        Configuration.instance().logger().info("Sherlock a gagné " + victoireSherlock + " parties sur " + (victoireJack+victoireSherlock+nbEgalité) );
        Configuration.instance().logger().info("Nombre d'égalité " + nbEgalité );
    }

    //Pre-Condition : iaS et iaJ initialisé
    //Post-Condition : Joue un coup de l'IA sherlock sur le jeu du controlleur et les copies des deux ia
    private void coupIASherlock(){
        cp = iaS.coupIA();
        iaS.j.jouerCoup(cp);
        cp.setPlateau(iaJ.j.plateau());
        iaJ.j.jouerCoup(cp);
        cp.setPlateau(jeu.plateau());
        jouerCoup();
        if(jeu.plateau().tousJetonsJoues()){
            jeu.plateau().prochainTour();
            iaJ.prochainTour();
            iaS.prochainTour();
        }
        iaJ.j.plateau().setJetonsActions((ArrayList<JetonActions>) jeu.plateau().jetonsActions.clone());
        iaS.j.plateau().setJetonsActions((ArrayList<JetonActions>) jeu.plateau().jetonsActions.clone());
    }

    //Pre-Condition : iaS et iaJ initialisé
    //Post-Condition : Joue un coup de l'IA jack sur le jeu du controlleur et les copies des deux ia
    private void coupIAJack(){
        cp = iaJ.coupIA();
        iaJ.j.jouerCoup(cp);
        cp.setPlateau(iaS.j.plateau());
        iaS.j.jouerCoup(cp);
        cp.setPlateau(jeu.plateau());
        jouerCoup();
        if(jeu.plateau().tousJetonsJoues()){
            jeu.plateau().prochainTour();
            iaJ.prochainTour();
            iaS.prochainTour();
        }
        iaJ.j.plateau().setJetonsActions((ArrayList<JetonActions>) jeu.plateau().jetonsActions.clone());
        iaS.j.plateau().setJetonsActions((ArrayList<JetonActions>) jeu.plateau().jetonsActions.clone());
    }


    @Override
    public void fixerInterfaceUtilisateur(InterfaceGraphique i) {
        ig=i;
    }

    public boolean iaActive(){
        return iaActive;
    }
}
