package Controle;

import Modele.Coup;
import Modele.Jeu;
import Modele.Joueur;

import java.util.Random;


public class IAMeilleureProchain extends IA{
    Random r;
    Jeu j;
    boolean isJack;
    public IAMeilleureProchain(Jeu j, boolean isJack) {
        r = new Random();
        this.j=j;
        this.isJack = isJack;
    }
    @Override
    public Coup coupIA(Jeu j) {

        Joueur joueurCourant = j.plateau().joueurCourant;
        return null;
    }
}
