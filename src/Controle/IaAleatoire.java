package Controle;

import Modele.*;


public class IaAleatoire extends IA{
    @Override
    Coup coupIA(Jeu j) {

        Joueur joueurCourant = j.plateau().joueurCourant;

        if(joueurCourant.isJack()){
            //traitement Jack
        }else{
            //Traitement Enqueteur
        }

        return null;
    }
}
