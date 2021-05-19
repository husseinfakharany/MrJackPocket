import Modele.Jeu;


public class TestModele {
    public static void main(String[] args) {
        Jeu jeu = new Jeu();
        int compteurCarte = 1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println("Carte " + compteurCarte);
                System.out.print("Orientation: " + jeu.grille[i][j].getOrientation());
                System.out.print(" - Position: " + jeu.grille[i][j].getPosition());
                System.out.print(" - Position personnage: " + jeu.grille[i][j].getSuspect().getPosition());
                System.out.print(" - Position Enqueteur: " + jeu.grille[i][j].getPositionEnqueteur());
                System.out.print(" - Nom Personnage: " + jeu.grille[i][j].getSuspect().getNomPersonnage());
                System.out.print(" - Carte Cachée: " + jeu.grille[i][j].getSuspect().getCarteCache());
                System.out.print(" - Personnage pioché: " + jeu.grille[i][j].getSuspect().getPioche());
                System.out.println();
                compteurCarte++;
            }

        }
    }
}
