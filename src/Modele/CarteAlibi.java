package Modele;

/*
**Carte Alibi**:
Nombre de sabliers (0, 1, 2)
Nom du Personnage suspect
 */
public class CarteAlibi {
	
		private Suspect suspect;
		private int sablier;

		public CarteAlibi(Suspect suspect){
			this.suspect = suspect;
			switch(suspect.getNomPersonnage()){
				case JOHN_PIZER:
					sablier=1;
					break;
				case SERGENT_GOODLEY:
					sablier=0;
					break;
				case WILLIAM_GULL:
					sablier=1;
					break;
				case MISS_STEALTHY:
					sablier=1;
					break;
				case JEREMY_BERT:
					sablier=1;
					break;
				case INSPECTOR_LESTRADE:
					sablier=0;
					break;
				case JOSEPH_LANE:
					sablier=1;
					break;
				case MADAME:
					sablier=2;
					break;
				case JOHN_SMITH:
					sablier=1;
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + suspect.getNomPersonnage());

			}
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
