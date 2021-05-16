import Modele.CarteRue;
import Modele.Jeu;
import Modele.SuspectNom;


public class TestModele {
    public static void main(String[] args) {
        Jeu jeu = new Jeu();
        int compteurCarte = 1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println("Carte " + compteurCarte);
                System.out.print("Orientation: " + jeu.grille[i][j].orientation);
                System.out.print(" - Position: " + jeu.grille[i][j].position);
                System.out.print(" - Position personnage: " + jeu.grille[i][j].suspect.position);
                System.out.print(" - Position Enqueteur: " + jeu.grille[i][j].positionEnqueteur);
                System.out.print(" - Nom Personnage: " + jeu.grille[i][j].suspect.nomPersonnage);
                System.out.print(" - Carte Cachée: " + jeu.grille[i][j].suspect.carteCache);
                System.out.print(" - Personnage pioché: " + jeu.grille[i][j].suspect.pioche);
                System.out.println();
                compteurCarte++;
            }

        }
    }
}
