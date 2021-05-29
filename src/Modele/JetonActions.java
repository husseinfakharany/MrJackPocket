package Modele;

import Global.Configuration;

/*
**Jetons Actions**:
Action face 1
Action face 2
Boolean recto (True) = Action 1 ou verso (False) = Action 2
Boolean dejaJoue
 */
public class JetonActions {

	Actions action1; //action face 1 
	Actions action2;  // action face 2
	boolean estRecto = true;
	boolean dejaJoue;

	public JetonActions(Actions action1, Actions action2) {
		this.action1 = action1;
		this.action2 = action2;
	}

	@Override
	public JetonActions clone() throws CloneNotSupportedException {
		JetonActions copy;
		try {
			copy = (JetonActions) super.clone();

		} catch (CloneNotSupportedException e) {
			Configuration.instance().logger().severe("Bug interne: CarteRue non clonable");
			return null;
		}
		copy.action1 = action1;
		copy.action2 = action2;
		return copy;
	}

	public Actions getAction1() {
		return action1;
	}

	public void setAction1(Actions action1) {
		this.action1 = action1;
	}

	public Actions getAction2() {
		return action2;
	}

	public void setAction2(Actions action2) {
		this.action2 = action2;
	}

	public boolean estRecto() {
		return estRecto;
	}

	public void setEstRecto(boolean estRecto) {
		this.estRecto = estRecto;
	}

	public boolean getDejaJoue(){
		return dejaJoue;
	}

	public void setDejaJoue(boolean b) {
		this.dejaJoue = b;
	}
}
