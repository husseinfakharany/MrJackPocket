package Controle;

import Modele.Coup;
import Vue.InterfaceGraphique;

public interface CollecteurEvenements {
		//void clicSouris(int l, int c);
		void commandeDistrict(int l, int c);
		boolean commandeMenu(String c);
		boolean commandeJeu(String c);
		void fixerInterfaceUtilisateur(InterfaceGraphique i);
		void fixerIA(String com);
		void annuler();
		void refaire();
		void jouerCoupIA(Coup cp);
}

