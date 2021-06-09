import Controle.ControleurMediateur;
import Modele.Jeu;
import Vue.InterfaceGraphique;


public class MrJackPocket {
    public static void main(String[] args) {
        Jeu jeu = new Jeu();

        ControleurMediateur controle = new ControleurMediateur(jeu);
        InterfaceGraphique.demarrer(jeu,controle);
    }
}
