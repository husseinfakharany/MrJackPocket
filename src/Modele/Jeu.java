package Modele;

import Global.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;

public class Jeu extends Observable implements Cloneable{

    public static final long seed = System.currentTimeMillis();
    //public static final long seed = 1622191690051L; Testing seed

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

    public void sauvegarder(String nomFichier){
        Coup cp;
        Action ac;
        CarteRue rue;
        int i;
        int entier;

        cp = annule();
        // Retour à l'état initial de la partie
        while(cp != null){
            cp = annule();
        }
        try{
            String home = System.getProperty("user.home");
            Path path = Paths.get(home + File.separator + "JackPocket"+ File.separator);
            Files.createDirectories(path);
            File f = new File(home + File.separator + "JackPocket" + File.separator + nomFichier + ".save");
            if (f.createNewFile()) {
                System.out.println("File created: " + f.getName());
            } else {
                throw new IOException("File already exist");
            }
            PrintWriter flux = new PrintWriter(f);

            // Recuperation des informations de la grille
            for(int l=0; l<3; l++){
                for(int c=0; c<3; c++){
                    rue = plateau.grille[l][c];
                    flux.write(rue.getSuspect().getNomPersonnage().toString());
                    flux.write(" ");
                    entier = rue.getOrientation();
                    flux.write(Integer.toString(entier));
                    flux.write(" ");
                }
            }
            flux.write("\n");
            for(Suspect s: plateau().getSuspects()){
                if(s.getIsJack()){ // Il n'y a qu'un seul Jack
                    flux.write(s.getNomPersonnage().toString());
                }
            }
            flux.write("\n");

            //Recuperation jetons et actions
            int actionsVues = 8;
            boolean finAtteinte = false;

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
                        flux.write(ac.getAction().toString());
                        flux.write(" ");
                        switch(ac.getAction()){
                            case DEPLACER_JOKER:
                                entier = ac.getNumEnqueteur();
                                flux.write(Integer.toString(entier));
                                flux.write(" ");
                            case DEPLACER_TOBBY:
                            case DEPLACER_WATSON:
                            case DEPLACER_SHERLOCK:
                                entier = ac.getDeplacement();
                                flux.write(Integer.toString(entier));
                                break;
                            case INNOCENTER_CARD:
                                flux.write(ac.getCartePioche().getSuspect().getNomPersonnage().toString());
                                break;
                            case ROTATION_DISTRICT:
                                entier = ac.getPosition1().y;
                                flux.write(Integer.toString(entier));
                                flux.write(" ");
                                entier = ac.getPosition1().x;
                                flux.write(Integer.toString(entier));
                                flux.write(" ");
                                entier = ac.getOrientationNew();
                                flux.write(Integer.toString(entier));
                                break;
                            case ECHANGER_DISTRICT:
                                entier = ac.getPosition1().y;
                                flux.write(Integer.toString(entier));
                                flux.write(" ");
                                entier = ac.getPosition1().x;
                                flux.write(Integer.toString(entier));
                                flux.write(" ");
                                entier = ac.getPosition2().y;
                                flux.write(Integer.toString(entier));
                                flux.write(" ");
                                entier = ac.getPosition2().x;
                                flux.write(Integer.toString(entier));
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
            e.printStackTrace();
        }
    }

    //Getters and setters
    public static long getSeed() {
        return seed;
    }

}
