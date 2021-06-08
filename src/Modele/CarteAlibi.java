package Modele;

import Global.Configuration;

/*
**Carte Alibi**:
Nombre de sabliers (0, 1, 2)
Nom du Personnage suspect
 */
public class CarteAlibi implements Cloneable {
	
		private Suspect suspect;
		private int sablier;

		public CarteAlibi(Suspect suspect){
			this.suspect = suspect;
			switch (suspect.getNomPersonnage()) {
				case JOHN_PIZER -> sablier = 1;
				case SERGENT_GOODLEY -> sablier = 0;
				case WILLIAM_GULL -> sablier = 1;
				case MISS_STEALTHY -> sablier = 1;
				case JEREMY_BERT -> sablier = 1;
				case INSPECTOR_LESTRADE -> sablier = 0;
				case JOSEPH_LANE -> sablier = 1;
				case MADAME -> sablier = 2;
				case JOHN_SMITH -> sablier = 1;
				default -> throw new IllegalStateException("Unexpected value: " + suspect.getNomPersonnage());
			}
		}

		@Override
		public CarteAlibi clone() throws CloneNotSupportedException {
			CarteAlibi copy;
			try {
				copy = (CarteAlibi) super.clone();

			} catch (CloneNotSupportedException e) {
				Configuration.instance().logger().severe("Bug interne : Carte Alibi non clonable");
				return null;
			}
			copy.suspect = suspect.clone();
			copy.sablier = this.sablier;
			return copy;
		}

		public SuspectCouleur getCouleur(){
			return suspect.getCouleur();
		}

		// Getters and Setters
		public Suspect getSuspect() {
			return suspect;
		}

		public void setSuspect(Suspect suspect) {
			this.suspect = suspect;
		}

		public int getSablier() {
			return sablier;
		}

		public void setSablier(int sablier) {
			this.sablier = sablier;
		}
}
