package Modele;

import static Modele.SuspectCouleur.*;

/*
**Carte Alibi**:
Nombre de sabliers (0, 1, 2)
Nom Personnage suspect
 */
public class CarteAlibi {
	
		private SuspectNom suspect;
		private int sablier;

		// Getters and Setters
		public SuspectNom getSuspect() {
			return suspect;
		}

		public SuspectCouleur getCouleur() {
			switch (suspect){
				case MADAME:
					return ROSE;
				case JOHN_PIZER:
					return BLANC;
				case JOHN_SMITH:
					return JAUNE;
				case JEREMY_BERT:
					return ORANGE;
				case JOSEPH_LANE:
					return GRIS;
				case MISS_STEALTHY:
					return VERT;
				case WILLIAM_GULL:
					return VIOLET;
				case SERGENT_GOODLEY:
					return NOIR;
				case INSPECTOR_LESTRADE:
					return BLEU;
				default:
					throw new IllegalStateException("Unexpected value: " + suspect);
			}
		}

		public void setSuspect(SuspectNom s) {
			suspect = s;
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
