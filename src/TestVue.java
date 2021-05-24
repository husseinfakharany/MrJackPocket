import Modele.Jeu;
import Vue.ControleurMediateur;
import Vue.InterfaceGraphique;


public class TestVue {
    public static void main(String[] args) {
        Jeu jeu = new Jeu();


        ControleurMediateur controle = new ControleurMediateur(jeu);
        InterfaceGraphique.demarrer(jeu,controle);
    }
}
