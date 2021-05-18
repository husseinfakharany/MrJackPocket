package Vue;


import Modele.Coup;
import Modele.Jeu;

public class ControleurMediateur implements CollecteurEvenements {
    InterfaceGraphique ig;
    Jeu jeu;
    boolean iaActive;
    boolean iaPeutJouer;

    public ControleurMediateur(Jeu j) {
        jeu = j;
        iaActive = false;
    }

    public boolean iaActive(){
        return iaActive;
    }

    public void fixerIA(String com){

    }

    public void activeIA(int state){

    }

    @Override
    public void clicSouris(int l, int c) {
        System.out.println("Clic en ( l : " +l+" , c : "+c+" )" );
    }

    public void jouerCoupIA(Coup cp){
        if(iaPeutJouer)
            jeu.jouerCoup(cp);
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }

    @Override
    public boolean commande(String c) {
        switch (c) {
            case "quitter":
                System.exit(0);
                break;
            case "commencer":
            case "Enquêteur":
            case "Meurtrier":
            case "Ordi Vs Ordi":
                ig.menuPartie();
                break;

            case "Facile":
            case "Moyenne":
            case "Difficile":
            case "local":
            case "reseau":
                ig.lancerPartie();
                //fixerIA(c);
                break;
            case "undo":
                undo();
                break;
            case "redo":
                redo();
                break;
            case "menu":
                ig.retourMenu();
                break;
                //TODO
            case "charger":
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
