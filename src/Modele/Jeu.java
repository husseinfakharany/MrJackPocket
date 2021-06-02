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

    public Jeu(String nomFichier){
        Scanner sc = new Scanner(new File(nomFichier));
        ArrayList<SuspectNom> suspectIndicesSauv = new ArrayList<>();
        ArrayList<Integer> orientationSauv = new ArrayList<>();
        ArrayList<Boolean> jetonsActionsSauv = new ArrayList<>();
        ArrayList<Action> ActionsSauv = new ArrayList<>();
        ArrayList<SuspectNom> SuspectNomPiocheSauv = new ArrayList<>();
        SuspectNom nomPersonnage;
        Action ac;
        Coup cp;
        String chaineChar;
        int i, l, c;

        // Lecture informations de la grille
        for (i=0; i<9; i++){
            suspectIndicesSauv.add(stringVersSuspectNom(sc.next()));
            orientationSauv.add(sc.nextInt());
        }
        chaineChar = sc.next();
        nomPersonnage = stringVersSuspectNom(chaineChar);

        //Chargement position initiale de la partie
        plateau = Plateau(this, suspectIndicesSauv, orientationSauv, nomPersonnage);
        Configuration.instance().logger().info("Seed used: " + seed);


        //Lecture jetons et actions
        int actionsVues = 8;
        while(sc.hasNext()){
            if(actionsVues == 8){
                //Lecture de nouveaux jetons
                jetonsActionsSauv.clear();
                for(i=0; i<=3; i++){
                    chaineChar = sc.next();
                    if (chaineChar.equals("true"){
                        jetonsActionsSauv.add(true);
                    } else{
                        jetonsActionsSauv.add(false);
                    }
                }
                p.forceJetons(jetonsActionsSauv);
                actionsVues = 0;
            } else{
                ac = new Action(null);
                chaineChar = sc.next();
                switch(chaineChar){
                    case "DEPLACER_JOKER":
                        ac.setAction(DEPLACER_JOKER);
                        ac.setNumEnqueteur(sc.nextInt());
                        ac.setDeplacement(sc.nextInt());
                        break;
                    case "DEPLACER_TOBBY":
                        ac.setAction(DEPLACER_TOBBY);
                        ac.setNumEnqueteur(2);
                        ac.setDeplacement(sc.nextInt());
                        break;
                    case "DEPLACER_WATSON":
                        ac.setAction(DEPLACER_WATSON);
                        ac.setNumEnqueteur(1);
                        ac.setDeplacement(sc.nextInt());
                        break;
                    case "DEPLACER_SHERLOCK":
                        ac.setAction(DEPLACER_SHERLOCK);
                        ac.setNumEnqueteur(0);
                        ac.setDeplacement(sc.nextInt());
                        break;
                    case "INNOCENTER_CARD":
                        ac.setAction(INNOCENTER_CARD);
                        chaineChar = sc.next();
                        for(CarteAlibi a: p.getCartesAlibis()){
                            if(a.getSuspect().getNomPersonnage().toString() == chaineChar){
                                ac.setCartePioche(a);
                            }
                        }
                        break;
                    case "ECHANGER_DISTRICT":
                        ac.setAction(ECHANGER_DISTRICT);
                        l = sc.nextInt();
                        c = sc.nextInt();
                        ac.setPosition1(new Point(c,l));
                        l = sc.nextInt();
                        c = sc.nextInt();
                        ac.setPosition2(new Point(c,l));
                        break;
                    case "ROTATION_DISTRICT":
                        ac.setAction(ROTATION_DISTRICT);
                        l = sc.nextInt();
                        c = sc.nextInt();
                        ac.setPosition1(new Point(c,l));
                        ac.setOrientationNew(sc.nextInt());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + nom);
                }
                actionsVues++;
                cp = new Coup(plateau, ac);
                jouerCoup(cp);
            }
        }
        sc.close();
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
        Integer entier;

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
                    flux.write(entier.toString());
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
                                flux.write(entier.toString());
                                flux.write(" ");
                            case DEPLACER_TOBBY:
                            case DEPLACER_WATSON:
                            case DEPLACER_SHERLOCK:
                                entier = ac.getDeplacement();
                                flux.write(entier.toString());
                                break;
                            case INNOCENTER_CARD:
                                flux.write(ac.getCartePioche().getSuspect().getNomPersonnage().toString());
                                break;
                            case ROTATION_DISTRICT:
                                entier = ac.getPosition1().y;
                                flux.write(entier.toString());
                                flux.write(" ");
                                entier = ac.getPosition1().x;
                                flux.write(entier.toString());
                                flux.write(" ");
                                entier = ac.getOrientationNew();
                                flux.write(entier.toString());
                                break;
                            case ECHANGER_DISTRICT:
                                entier = ac.getPosition1().y;
                                flux.write(entier.toString());
                                flux.write(" ");
                                entier = ac.getPosition1().x;
                                flux.write(entier.toString());
                                flux.write(" ");
                                entier = ac.getPosition2().y;
                                flux.write(entier.toString());
                                flux.write(" ");
                                entier = ac.getPosition2().x;
                                flux.write(entier.toString());
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
            return;
        }
    }

    SuspectNom stringVersSuspectNom(String nom){
        switch(nom){
            case "MADAME":
                return MADAME;
            case "JOHN_PIZER":
                return JOHN_PIZER;
            case "JOHN_SMITH":
                return JOHN_SMITH;
            case "JEREMY_BERT":
                return JEREMY_BERT;
            case "JOSEPH_LANE":
                return JOSEPH_LANE;
            case "MISS_STEALTHY":
                return MISS_STEALTHY;
            case "WILLIAM_GULL":
                return WILLIAM_GULL;
            case "SERGENT_GOODLEY":
                return SERGENT_GOODLEY;
            case "INSPECTOR_LESTRADE":
                return INSPECTOR_LESTRADE;
            default:
                throw new IllegalStateException("Unexpected value: " + nom);
        }
    }

    //Getters and setters
    public static long getSeed() {
        return seed;
    }
}
