package Vue;

import Modele.Jeu;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class InterfaceGraphique implements Observer, Runnable {
    Jeu jeu;
    JFrame frame;
    Box boiteMenu = null,boiteTitre  = null,boiteBoutons = null, boiteInfo = null, boiteAvantPartie = null;
    boolean ia;
    CollecteurEvenements controle;
    JComboBox<String> commencer, boutonIA;

    //TODO Fonction charger boite + fonctions privés qui contruisent la boite si null (~ Confguration Sokoban) +
    // une fonction qui renvoie la boite d'un String en parametre avec un switch Ex: afficherEcran("Menu")
    // TODO Implémentation Tutoriel

    InterfaceGraphique(Jeu j, CollecteurEvenements c) {
        jeu = j;
        jeu.addObserver(this);
        controle = c;
        controle.fixerInterfaceUtilisateur(this);
    }



    public static void demarrer(Jeu j, CollecteurEvenements c) {
        SwingUtilities.invokeLater(new InterfaceGraphique(j, c));
    }

    public void run() {
        frame = new JFrame("Mr Jack Pocket");
        frame.add(getBoiteMenu());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080,720);
        //activerPleinEcran();
        frame.setVisible(true);
    }

    public void activeIA(){

    }

    public void lancerPartie(){
        frame.remove(getBoiteAvantPartie());
        frame.add(getBoiteInfo(), BorderLayout.PAGE_START);
        frame.revalidate();
        frame.repaint();
    }

    public void menuPartie(){
        frame.remove(getBoiteMenu());
        frame.add(getBoiteAvantPartie());
        frame.revalidate();
        frame.repaint();

    }

    public void retourMenu(){
        frame.remove(getBoiteInfo());
        frame.add(getBoiteMenu());
        frame.revalidate();
        frame.repaint();
    }

    public void activerPleinEcran() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        device.setFullScreenWindow(frame);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    //Description des boites
    private Box getBoiteMenu(){
        if (boiteMenu == null){
            boiteMenu = Box.createVerticalBox();

            boiteMenu = Box.createHorizontalBox();
            boiteMenu.add(Box.createHorizontalGlue());
            boiteMenu.add(getBoiteBoutons());
            boiteMenu.add(Box.createHorizontalGlue());
            boiteMenu.add(getBoiteTitre());
            boiteMenu.add(Box.createHorizontalGlue());
        }
        commencer.setEditable(true);
        commencer.setSelectedItem("Commencer une partie");
        commencer.setEditable(false);
        return boiteMenu;
    }

    private Box getBoiteTitre(){
        if (boiteTitre == null){
            boiteTitre = Box.createVerticalBox();

            //Alignement et ajout des pemiers éléments
            JLabel titre = new JLabel("Mr Jack Pocket");
            titre.setFont(new Font(Font.SANS_SERIF,  Font.PLAIN, 25));
            titre.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel astuces = new JLabel("Astuces : Cliquez sur le bouton commencer une partie, c'est magique !");
            astuces.setAlignmentX(Component.CENTER_ALIGNMENT);

            boiteTitre.add(Box.createVerticalGlue());
            boiteTitre.add(titre);
            boiteTitre.add(astuces);
            boiteTitre.add(Box.createVerticalGlue());
        }
        return boiteTitre;
    }

    private Box getBoiteBoutons(){
        if (boiteBoutons == null){
            JButton charger,tuto,quitter;

            //Lancer la partie
            String [] menuListe = {"Enquêteur", "Meurtrier", "Ordi Vs Ordi"};
            commencer = new JComboBox<>(menuListe);
            commencer.setPreferredSize(new Dimension(200, 25));
            commencer.setMaximumSize(new Dimension(450, 25));
            DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
            listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center-aligned items
            commencer.setRenderer(listRenderer);
            commencer.addActionListener(new AdaptateurCombobox(controle));
            commencer.setAlignmentX(Component.CENTER_ALIGNMENT);

            //Charger partie
            charger = new JButton("Charger une partie");
            charger.setMaximumSize(new Dimension(450, 25));
            charger.addActionListener(new AdaptateurCommande(controle,"charger"));
            charger.setAlignmentX(Component.CENTER_ALIGNMENT);

            //Charger partie
            quitter = new JButton("Quitter");
            quitter.setMaximumSize(new Dimension(450, 25));
            quitter.addActionListener(new AdaptateurCommande(controle,"quitter"));
            quitter.setAlignmentX(Component.CENTER_ALIGNMENT);

            //Règles & Tutoriel
            tuto = new JButton("Règles & Tutoriel");
            tuto.setMaximumSize(new Dimension(450, 25));
            tuto.addActionListener(new AdaptateurCommande(controle,"tuto"));
            tuto.setAlignmentX(Component.CENTER_ALIGNMENT);



            boiteBoutons =Box.createVerticalBox();

            boiteBoutons.add(Box.createVerticalGlue());
            boiteBoutons.add(commencer);
            boiteBoutons.add(Box.createVerticalGlue());
            boiteBoutons.add(charger);
            boiteBoutons.add(Box.createVerticalGlue());
            boiteBoutons.add(tuto);
            boiteBoutons.add(Box.createVerticalGlue());
            boiteBoutons.add(quitter);
            boiteBoutons.add(Box.createVerticalGlue());

        }
        return boiteBoutons;
    }

    private Box getBoiteInfo(){
        JLabel info,tour;
        if (boiteInfo == null){
            boiteInfo = Box.createHorizontalBox();

            tour = new JLabel();
            tour.setText("Tour n°X");

            info = new JLabel();
            info.setText("Explications");

            JButton menu = new JButton("Retour au Menu");
            menu.addActionListener(new AdaptateurCommande(controle,"menu"));

            // JComponent plateauGraphique
            //frame.add(plateauGraphique);
            //plateauGraphique.addMouseListener(new AdaptateurSouris(gaufreG, controle));

            boiteInfo.add(tour);
            boiteInfo.add(Box.createHorizontalGlue());
            boiteInfo.add(info);
            boiteInfo.add(Box.createHorizontalGlue());
            boiteInfo.add(menu);

        }
        return boiteInfo;
    }

    private Box getBoiteAvantPartie(){
        if (boiteAvantPartie == null){
            boiteAvantPartie = Box.createHorizontalBox();

            //Lancer la partie
            String [] menuListe = {"Facile", "Moyenne", "Difficile"};
            boutonIA = new JComboBox<>(menuListe);
            boutonIA.setPreferredSize(new Dimension(200, 25));
            boutonIA.setMaximumSize(new Dimension(450, 25));
            DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
            listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center-aligned items
            boutonIA.setRenderer(listRenderer);
            boutonIA.addActionListener(new AdaptateurCombobox(controle));
            boutonIA.setAlignmentX(Component.CENTER_ALIGNMENT);

            //Charger partie
            JButton local = new JButton("Joueur contre un ami");
            local.setMaximumSize(new Dimension(450, 25));
            local.addActionListener(new AdaptateurCommande(controle,"local"));
            local.setAlignmentX(Component.CENTER_ALIGNMENT);

            //Charger partie
            JButton reseau = new JButton("Joueur en réseau");
            reseau.setMaximumSize(new Dimension(450, 25));
            reseau.addActionListener(new AdaptateurCommande(controle,"reseau"));
            reseau.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel titre = new JLabel("Mr Jack Pocket");
            titre.setFont(new Font(Font.SANS_SERIF,  Font.PLAIN, 25));
            titre.setAlignmentX(Component.CENTER_ALIGNMENT);

            boiteAvantPartie = Box.createVerticalBox();

            boiteAvantPartie.add(Box.createVerticalGlue());
            boiteAvantPartie.add(titre);
            boiteAvantPartie.add(Box.createVerticalGlue());
            boiteAvantPartie.add(boutonIA);
            boiteAvantPartie.add(Box.createVerticalGlue());
            boiteAvantPartie.add(local);
            boiteAvantPartie.add(Box.createVerticalGlue());
            boiteAvantPartie.add(reseau);
            boiteAvantPartie.add(Box.createVerticalGlue());
        }
        boutonIA.setEditable(true);
        boutonIA.setSelectedItem("Joueur contre une IA");
        boutonIA.setEditable(false);
        return boiteAvantPartie;
    }
}
