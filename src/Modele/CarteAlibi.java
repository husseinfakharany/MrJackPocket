package Modele;

/*
**Carte Alibi**:
Nombre de sabliers (0, 1, 2)
Nom Personnage suspect
 */
public class CarteAlibi {
	
		private Suspect suspect;
		private int sablier;

		CarteAlibi(Suspect suspect){
			this.suspect = suspect;

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
