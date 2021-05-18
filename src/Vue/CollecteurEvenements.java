package Vue;

public interface CollecteurEvenements {
		void clicSouris(int l, int c);
		boolean commande(String c);
		void fixerInterfaceUtilisateur(InterfaceGraphique i);
		void fixerIA(String com);
		boolean iaActive();
		void activeIA(int state);
		void undo();
		void redo();
	}

