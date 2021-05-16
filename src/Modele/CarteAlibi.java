package Modele;
/*
**Carte Alibi**:
Nombre de sabliers (0, 1, 2)
Nom Personnage suspect
 */
public class CarteAlibi {
	
		private AlibiName suspect;
		private int sablier;

		// Getters and Setters
		public AlibiName getSuspect() {
			return suspect;
		}

		public void setSuspect(AlibiName suspect) {
			this.suspect = suspect;
		}

		public int getSablier() {
			return sablier;
		}

		public void setSablier(int sablier) {
			this.sablier = sablier;
		}

		// tester toString
		public String toString() {
			return suspect.name();
		}

	
}
