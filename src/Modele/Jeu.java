package Modele;

import Global.Configuration;

import java.util.Observable;

public class Jeu extends Observable implements Cloneable{

    public static final long seed = System.currentTimeMillis();
    //public static final long seed = 1622191690051L; //Testing seed

    Plateau plateau;

    public Jeu(){
        plateau = new Plateau(this);
        Configuration.instance().logger().info("Seed used: " + seed);
    }

    public Plateau plateau(){return plateau;}

    public boolean jouerCoup(Coup cp) {
        boolean res;
        if (cp == null) {
            throw new IllegalStateException("Coup Impossible");
        } else {
            res = plateau.jouerCoup(cp);
            notifyObservers();
        }
        return res;
    }

    @Override
    public Jeu clone() throws CloneNotSupportedException {
        Jeu copy;
        try {
            copy = (Jeu) super.clone();

        } catch (CloneNotSupportedException e) {
            Configuration.instance().logger().severe("Bug interne : Jeu non clonable");
            return null;
        }
        copy.plateau = plateau.clone();
        return copy;
    }

    void notifierObserveurs(){
        setChanged();
        notifyObservers();
    }

    public Coup annule() {
        Coup cp = plateau.annuler();
        notifyObservers();
        return cp;
    }

    public Coup refaire() {
        Coup cp = plateau.refaire();
        notifyObservers();
        return cp;
    }
    
    void sauvegarder(String nomFichier){
        Coup cp;
        Action ac;
        CarteRue rue;
        int l, c, i;
        Integer entier;

        cp = annule();
        // Retour à l'état initial de la partie
        while(cp != null){
            cp = annule();
        }
        try{
            PrintWriter flux = new PrintWriter(nomFichier);
            // Recuperation des informations de la grille
            for(int l=0; l<3; l++){
                for(int c=0; c<3; c++){
                    rue = plateau.grille[l][c];
                    flux.write(rue.getSuspect().getNomPersonnage());
                    flux.write(" ");
                    entier = rue.getOrientation();
                    flux.write(entier.toString());
                    flux.write(" ");
                }
            }
            flux.write("\n");
            for(Suspect s: suspects){
                if(s.getIsJack()){ // Il n'y a qu'un seul Jack
                    flux.write(s.getNomPersonnage());
                }
            }
            flux.write("\n");

            //Recuperation jetons et actions
            int actionsVues = 8;
            bool finAtteinte = false;

            while(!finAtteinte){
                if(actionsVues == 8){
                    //Recuperation des nouveaux jetons
                    for(i=0; i<=3; i++){
                        if(plateau.getJeton(i).estRecto()){
                            flux.write("true ");
                        } else{
                            flux.write("false ");
                        }
                    }
                    flux.write("\n");
                    actionsVues = 0;
                } else{
                    //Recuperation d'une action
                    cp = refaire();
                    if(cp == null){
                        finAtteinte = true;
                    } else{
                        ac = cp.getAction();
                        flux.write(ac.getAction());
                        flux.write(" ");
                        switch(ac.getAction()){
                            case DEPLACER_JOKER:
                                entier = ac.getNumEnqueteur();
                                flux.write(entier.toString()());
                                flux.write(" ");
                            case DEPLACER_TOBBY:
                            case DEPLACER_WATSON:
                            case DEPLACER_SHERLOCK:
                                entier = ac.getDeplacement();
                                flux.write(entier.toString()());
                                break;
                            case INNOCENTER_CARD:
                                break;
                            case ROTATION_DISTRICT:
                                entier = ac.getPosition1().y;
                                flux.write(entier.toString()());
                                flux.write(" ");
                                entier = ac.getPosition1().x;
                                flux.write(entier.toString()());
                                flux.write(" ");
                                entier = ac.getOrientationNew();
                                flux.write(entier.toString()());
                                break;
                            case ECHANGER_DISTRICT:
                                entier = ac.getPosition1().y;
                                flux.write(entier.toString()());
                                flux.write(" ");
                                entier = ac.getPosition1().x;
                                flux.write(entier.toString()());
                                flux.write(" ");
                                entier = ac.getPosition2().y;
                                flux.write(entier.toString()());
                                flux.write(" ");
                                entier = ac.getPosition2().x;
                                flux.write(entier.toString()());
                                break;
                        }
                    }
                    flux.write("\n");
                    actionsVues++;
                }
            }
            flux.close();
        }
        catch(Exception e){
            System.out.println("Erreur ouverture//écriture fichier");
            return;
        }
    }

    //Getters and setters
    public static long getSeed() {
        return seed;
    }
}
