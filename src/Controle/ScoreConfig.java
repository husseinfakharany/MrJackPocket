package Controle;

import Modele.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/*
//TODO [Sherlock] Essayer de continuer de voir les personnage sur lesquels on vient de perdre la vision
 */

public class ScoreConfig {

    //[Sherlock]Qui disperse les enqueteurs avecsens = 1
    //[Jack]Qui regroupe les enqueteurs avec sens = -1
    static int scoreDispersionEnqueteur(Jeu j, int sens){
        int distanceCalcule, distanceMin=-1,resultat=-1;
        for(Enqueteur e1 : j.plateau().enqueteurs){
            distanceCalcule = 0;
            for(Enqueteur e2 : j.plateau().enqueteurs){
                if(e1.getNumEnqueteur() != e2.getNumEnqueteur()){
                    distanceCalcule = (int) ((e1.getPosition().getX() - e2.getPosition().getX()) + (e1.getPosition().getY() - e2.getPosition().getY() ));
                }
            }
            distanceMin = Math.min(distanceMin,distanceCalcule);
            resultat=Math.min(distanceMin,resultat);
        }
        return sens*resultat;
    }

    //[Jack]Se montre si ça permet d'innoncenter 3 personne de plus
    static int scoreJackVisiblePourSauver(Jeu j){
        int jackVisibles = 0;
        for(Suspect s : j.plateau().visibles()){
            if(s.getCouleur().equals(j.plateau().idJack)) jackVisibles = -3;
        }
        return jackVisibles + 9 - j.plateau().getSuspectsInnocete().size() - j.plateau().visibles().size();
    }

    //[Jack]Qui essaye de protéger un maximum de suspect
    static int scoreSuspectCaches(Jeu j){
        return 9 - j.plateau().getSuspectsInnocete().size() - j.plateau().visibles().size();
    }

    //[Jack]Qui s'éloigne le plus de l'inspecteur le plus proche en nombre de cartes
    static int scoreEloignement(Jeu j){
        Point positionJ=null;
        for(Suspect s : j.plateau().getSuspects()){
            if(s.getCouleur().equals(j.plateau().idJack)) positionJ = s.getPosition();
        }
        if(positionJ==null) throw new RuntimeException("Jack innocenté");
        int distance = -1;
        for(Enqueteur e : j.plateau().enqueteurs){
            distance = Math.min(distance,(positionJ.x-e.getPosition().x)+(positionJ.y-e.getPosition().y));
        }
        return distance;
    }

    //[Jack]Qui essaye de protéger un maximum de suspect dont Jack
    //Négatif si Jack est visible prend le cas où le moins de suspect sont innocentés
    static int scoreSuspectVisiblesJackCache(Jeu j){
        int jackVisibles = 0;
        for(Suspect s : j.plateau().visibles()){
            if(s.getCouleur().equals(j.plateau().idJack)) jackVisibles = -10;
        }
        return jackVisibles + 9 - j.plateau().getSuspectsInnocete().size() - j.plateau().visibles().size();
    }

    //[Jack]Protéger les suspects de notre main
    static int scoreSuspectMainCache(Jeu j){
        int suspectMainsVisibles = 0;
        for(Suspect s : j.plateau().visibles()){
            if(s.getCouleur().equals(j.plateau().idJack)) suspectMainsVisibles ++;
            for(CarteAlibi carte : j.plateau().jack.getCardList()){
                if(carte.getSuspect().getNomPersonnage().equals(s.getNomPersonnage())) suspectMainsVisibles ++;
            }
        }
        return suspectMainsVisibles;
    }

    //[Jack]Gagner le plus de sabliers possible
    static int scoreSablierJack(Jeu j, Actions action){
        Random r = new Random();
        int res = 0;
        if(action == Actions.INNOCENTER_CARD){
            ArrayList<CarteAlibi> listCarte = new ArrayList<>();
            while(j.plateau().getTaillePioche() != 0){
                listCarte.add(j.plateau().piocher());
            }
            //Rajouter celle qu'on vient de piocher
            //listCarte.add(0,j.plateau().jack.getCardList().get(j.plateau().jack.getCardList().size()-1));
            int alea;
            //Tirer 3 cartes au hasard pour ne pas tenir compte que de la carte au dessu de la pioche
            if(listCarte.size()==0)return scoreSuspectElimine(j);
            for(int i = 0; i<3; i++){
                alea = r.nextInt(listCarte.size());
                if(listCarte.get(alea).getSablier()>0) res = 10;
            }
            //Enlever la carte qu'on recupère dans notre main
            listCarte.remove(0);
            //Restaurer la pioche dans l'état initial du debut de la fonction
            while(!listCarte.isEmpty()){
                CarteAlibi carte = listCarte.get(0);
                j.plateau().addToPioche(carte);
                listCarte.remove(0);
            }
            return res;
        } else {
            return scoreSuspectElimine(j);
        }
    }

    //[Sherlock] Piocher dès que possible en fonction de la **proba d'innoncenter en fonction du nombre à innocenter** un suspect
    static int scorePiocheSuspectCaches(Jeu j, Actions action){
        if(action == Actions.INNOCENTER_CARD){
            return j.plateau().getTaillePioche() - j.plateau().getSuspectsInnocete().size();
        } else {
            return scoreSuspectElimine(j);
        }
    }

    //[Sherlock]Qui essaye d'avoir autant de suspect visible que pas visible
    static int scoreSuspectElimine(Jeu j){
        return Math.min( 9 - j.plateau().getSuspectsInnocete().size() - j.plateau().visibles().size(), j.plateau().visibles().size());
    }

    //[Sherlock]Qui essaye de voir un maximum de suspect possible
    static int scoreSuspectVisibles(Jeu j){
        return j.plateau().visibles().size();
    }
}