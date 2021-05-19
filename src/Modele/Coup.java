package Modele;
import java.util.*;
import java.awt.*;
/*
**Coup**:
Type Action
Methode qui execute le coup
 */
public class Coup {
	
	private Actions action;
    private Point position1;
    private Point position2;
    private int orientation;
    private int deplacement =0;
   
    //entier : 1 ou 2
	
    //CAS Où ACTION EQUIVEAUT à ECHANGE OU ROTATION
	public Coup(Actions action,Point position1,Point position2,int orientation,int deplacement) {
		
		this.action = action;
		this.position1 = position1;
		this.position2 = position2;
		this.orientation = orientation;
		if(action.name().equals("DEPLACER_DETECTIVE") || action.name().equals("DEPLACER_JOKER") ) {
			this.deplacement=deplacement;
		}else {
			this.deplacement=0;
		}
	}
	
   
	public Actions getAction() {
		return action;
	}

	public void setAction(Actions action) {
		this.action = action;
	}

	public Point getPosition1() {
		return position1;
	}

	public void setPosition1(Point position1) {
		this.position1 = position1;
	}

	public Point getPosition2() {
		return position2;
	}

	public void setPositionQuartier2(Point position2) {
		this.position2 = position2;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public int getDeplacement() {
		return deplacement;
	}

	public void setDeplacement(int deplacement) {
		this.deplacement = deplacement;
	}


	
	
}
