package Modele;

import java.awt.*;
import java.util.ArrayList;

public class Action{


    Actions action;
    Joueur joueur;
    Point position1;
    Point position2;
    int orientationNew;
    int orientationOld;
    int numEnqueteur;
    private int numAction;
    private int deplacement;
    CarteAlibi cartePioche;
    int orientationSuspect;
    boolean ia;

    public Action(Joueur joueur){
        this.action = null;
        this.position1 = null;
        this.position2 = null;
        this.orientationOld = -1;
        this.orientationNew = -1;
        this.deplacement = -1;
        this.numEnqueteur = -1;
        this.joueur = joueur;
        this.cartePioche = null;
        this.orientationSuspect = -1;
        this.ia = false;
    }

    //Pre-Condition : actionJeton non vide
    //Post-Condition : Renvoie une liste non vide est exhaustif des différents coups jouables
    public static Iterable<Action> listeAction(Actions actionJeton, Joueur joueur) {
        ArrayList<Point> positions = new ArrayList<>();
        ArrayList<Action> res = new ArrayList<>();
        Action action;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                positions.add(new Point(i,j));
            }
        }
        switch (actionJeton){
            case DEPLACER_JOKER:
                for(int i = 0; i<3;i++){
                    action = new Action(joueur);
                    action.setAction(Actions.DEPLACER_JOKER);
                    action.setDeplacement(1);
                    action.setNumEnqueteur(i);
                    action.setJoueur(joueur);
                    res.add(action);
                }
                break;
            case DEPLACER_SHERLOCK:
                for(int i = 1; i<3;i++){
                    action = new Action(joueur);
                    action.setAction(Actions.DEPLACER_SHERLOCK);
                    action.setDeplacement(i);
                    action.setNumEnqueteur(Plateau.SHERLOCK);
                    action.setJoueur(joueur);
                    res.add(action);
                }
                break;
            case DEPLACER_WATSON:
                for(int i = 1; i<3;i++){
                    action = new Action(joueur);
                    action.setAction(Actions.DEPLACER_WATSON);
                    action.setDeplacement(i);
                    action.setNumEnqueteur(Plateau.WATSON);
                    action.setJoueur(joueur);
                    res.add(action);
                }
                break;
            case DEPLACER_TOBBY:
                for(int i = 1; i<3;i++){
                    action = new Action(joueur);
                    action.setAction(Actions.DEPLACER_TOBBY);
                    action.setDeplacement(i);
                    action.setNumEnqueteur(Plateau.TOBBY);
                    action.setJoueur(joueur);
                    res.add(action);
                }
                break;
            case ECHANGER_DISTRICT:
                for(Point position1 : positions){
                    for(Point position2 : positions){
                        if(!position1.equals(position2)){
                            action = new Action(joueur);
                            action.setAction(Actions.ECHANGER_DISTRICT);
                            action.setPosition1(position1);
                            action.setPosition2(position2);
                            action.setJoueur(joueur);
                            res.add(action);
                        }
                    }
                }
                break;
            case INNOCENTER_CARD:
                action = new Action(joueur);
                action.setAction(Actions.INNOCENTER_CARD);
                action.setJoueur(joueur);
                res.add(action);
                break;
            case ROTATION_DISTRICT:
                for(Point position : positions){
                    for(int i = 0; i<4;i++){
                        action = new Action(joueur);
                        action.setAction(Actions.ROTATION_DISTRICT);
                        action.setPosition1(position);
                        action.setOrientationNew((~(1<<i)) & Plateau.NSEO);
                        action.setJoueur(joueur);
                        res.add(action);
                    }
                }
                break;
        }
        return res;
    }

    //Pre-Condition : Vide
    //Post-Condition : Indique si un coup est dans le domaine des coups valides. Ne vérifie pas la jouabilité mais
    //le domaine des différents arguments du coup
    public boolean estValide(){
        if(action == null) return false;
        switch (action){
            case DEPLACER_JOKER:
                if (joueur.isJack()) {
                    return (getDeplacement() == -1 || getDeplacement() == 0 || getDeplacement() == 1 || getDeplacement() == 2) && numEnqueteur >= 0 && numEnqueteur <= 2;
                } else {
                    return (getDeplacement() == 1 || getDeplacement() == 2) && numEnqueteur >= 0 && numEnqueteur <= 2;
                }
            case DEPLACER_TOBBY:
            case DEPLACER_WATSON:
            case DEPLACER_SHERLOCK:
                return getDeplacement() ==1 || getDeplacement() ==2;
            case INNOCENTER_CARD:
                return cartePioche != null && orientationSuspect != -1;
            case ECHANGER_DISTRICT:
                return getPosition1() != null && getPosition2() !=null;
            case ROTATION_DISTRICT:
                return getPosition1() != null &&
                        ( (  getOrientationNew() == Plateau.NSE || getOrientationNew() == Plateau.NSO
                    || getOrientationNew() == Plateau.SEO || getOrientationNew() == Plateau.NEO)
                        && (getOrientationNew() != getOrientationOld()) || ( getOrientationNew() == Plateau.NSEO && getPosition1().getX() != -1 ) );
        }
        return false;
    }

    //Pre-Condition : Vide
    //Post-Condition : Remet les valeurs par défaut d'un coup
    public void reinitialiser(){
        this.action = null;
        this.position1 = null;
        this.position2 = null;
        this.orientationOld = -1;
        this.orientationNew = -1;
        this.deplacement = -1;
        this.numEnqueteur = -1;
        this.joueur = null;
        this.cartePioche = null;
    }

    //Getters and setters
    public Actions getAction(){
        return action;
    }

    public void setAction(Actions action){
        this.action = action;
        this.position1 = null;
        this.position2 = null;
        this.orientationOld = -1;
        this.orientationNew = -1;
        this.deplacement = -1;
        this.numEnqueteur = -1;
        this.joueur = null;
        this.cartePioche = null;
        this.ia = false;
    }

    public Joueur getJoueur(){
        return joueur;
    }

    public void setJoueur(Joueur joueur){
        this.joueur = joueur;
    }

    public Point getPosition1(){
        return position1;
    }

    public void setPosition1(Point position){
        this.position1 = position;
    }

    public Point getPosition2(){
        return position2;
    }

    public void setPosition2(Point position){
        this.position2 = position;
    }

    public int getOrientationNew(){
        return orientationNew;
    }

    public void setOrientationNew(int orientationNew){
        this.orientationNew = orientationNew;
    }

    public int getOrientationOld(){
        return orientationOld;
    }

    public void setOrientationOld(int orientationOld){
        this.orientationOld = orientationOld;
    }

    public int getNumEnqueteur(){
        return numEnqueteur;
    }

    public void setNumEnqueteur(int numEnqueteur){
        this.numEnqueteur = numEnqueteur;
    }

    public int getDeplacement(){
        return deplacement;
    }

    public void setDeplacement(int deplacement){
        this.deplacement = deplacement;
    }

    public CarteAlibi getCartePioche() {
        return cartePioche;
    }

    public void setCartePioche(CarteAlibi cartePioche){
        this.cartePioche = cartePioche;
    }

    public void setOrientationSuspect(int orientationSuspect){
        this.orientationSuspect = orientationSuspect;
    }

    public int getOrientationSuspect(){
        return orientationSuspect;
    }

    public int getNumAction() {
        return numAction;
    }

    public void setNumAction(int numAction) {
        this.numAction = numAction;
    }

    public boolean getIa() {
        return ia;
    }

    public void setIa(boolean ia){
        this.ia = ia;
    }
}