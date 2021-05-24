import Modele.Jeu;


public class TestModele {
    public static void main(String[] args) {
        Jeu jeu = new Jeu();
        int compteurCarte = 1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println("Carte " + compteurCarte);
                System.out.print("Orientation: " + jeu.plateau().grille[i][j].getOrientation());
                System.out.print(" - Position: " + jeu.plateau().grille[i][j].getPosition());
                System.out.print(" - Position personnage: " + jeu.plateau().grille[i][j].getSuspect().getPosition());
                System.out.print(" - Nom Personnage: " + jeu.plateau().grille[i][j].getSuspect().getNomPersonnage());
                System.out.print(" - Carte Cachée: " + jeu.plateau().grille[i][j].getSuspect().getInnocente());
                System.out.print(" - Personnage pioché: " + jeu.plateau().grille[i][j].getSuspect().getPioche());
                System.out.println();
                compteurCarte++;
            }

        }
    }
}
