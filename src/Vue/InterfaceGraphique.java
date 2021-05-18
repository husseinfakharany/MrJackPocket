package Vue;

import Modele.Jeu;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class InterfaceGraphique implements Observer, Runnable {
    Jeu jeu;
    JFrame frame;
    Box boiteMenu,boiteTitre,boiteBoutons, boiteInfo, boiteAvantPartie;
    boolean ia;
    CollecteurEvenements controle;
    JComboBox<String> commencer;

    //TODO Fonction charger boite + fonctions privés qui contruisent la boite si null (~ Confguration Sokoban) +
    // une fonction qui renvoie la boite d'un String en parametre avec un switch Ex: afficherEcran("Menu")
    // TODO Implémentation Tutoriel

    InterfaceGraphique(Jeu j, CollecteurEvenements c) {
        jeu = j;
        jeu.addObserver(this);
        controle = c;
        controle.fixerInterfaceUtilisateur(this);
    }

    //code : https://stackoverflow.com/questions/16665688/display-a-non-selectable-default-value-for-jcombobox
    public JComboBox<String> dropdown(String [] elements){
        String NOT_SELECTABLE_OPTION = "Commencer une partie";
        JComboBox<String> comboBox = new JComboBox<>();

        comboBox.setModel(new DefaultComboBoxModel<String>() {
            private static final long serialVersionUID = 1L;
            boolean selectionAllowed = true;

            @Override
            public void setSelectedItem(Object anObject) {
                if (!NOT_SELECTABLE_OPTION.equals(anObject)) {
                    super.setSelectedItem(anObject);
                } else if (selectionAllowed) {
                    // Allow this just once
                    selectionAllowed = false;
                    super.setSelectedItem(anObject);
                }
            }
        });

        comboBox.addItem(NOT_SELECTABLE_OPTION);
        for(String e : elements){
            comboBox.addItem(e);
        }
        return comboBox;
    }

    public static void demarrer(Jeu j, CollecteurEvenements c) {
        SwingUtilities.invokeLater(new InterfaceGraphique(j, c));
    }

    public void run() {
        JButton charger,tuto,quitter;

        frame = new JFrame("Mr Jack Pocket");

        boiteMenu = Box.createVerticalBox();

        //Lancer la partie
        String [] menuListe = {"Enquêteur", "Meurtrier", "Ordi Vs Ordi"};
        commencer = new JComboBox<>(menuListe);
        commencer.setPreferredSize(new Dimension(200, 25));
        commencer.setMaximumSize(new Dimension(450, 25));
        DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center-aligned items
        commencer.setRenderer(listRenderer);
        commencer.setEditable(true);
        commencer.setSelectedItem("Commencer une partie");
        commencer.setEditable(false);
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

        boiteMenu = Box.createHorizontalBox();
        boiteMenu.add(Box.createHorizontalGlue());
        boiteMenu.add(boiteBoutons);
        boiteMenu.add(Box.createHorizontalGlue());
        boiteMenu.add(boiteTitre);
        boiteMenu.add(Box.createHorizontalGlue());

        frame.add(boiteMenu);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080,720);
        //activerPleinEcran();
        frame.setVisible(true);
    }

    public void activeIA(){

    }

    public void updateCommencer(){
        //commencer.setEnabled(controle.iaActive());
    }

    public void lancerPartie(){
        JLabel info,tour;

        frame.remove(boiteAvantPartie);

        // JComponent plateauGraphique
        //frame.add(plateauGraphique);


        boiteInfo = Box.createHorizontalBox();

        tour = new JLabel();
        tour.setText("Tour n°X");

        info = new JLabel();
        info.setText("Explications");

        JButton menu = new JButton("Retour au Menu");
        menu.addActionListener(new AdaptateurCommande(controle,"menu"));

        boiteInfo.add(tour);
        boiteInfo.add(Box.createHorizontalGlue());
        boiteInfo.add(info);
        boiteInfo.add(Box.createHorizontalGlue());
        boiteInfo.add(menu);

        frame.add(boiteInfo, BorderLayout.PAGE_START);

        frame.revalidate();
        frame.repaint();
        //plateauGraphique.addMouseListener(new AdaptateurSouris(gaufreG, controle));
    }

    public void menuPartie(){
        JLabel info,tour;

        frame.remove(boiteMenu);

        // JComponent plateauGraphique
        //frame.add(plateauGraphique);


        boiteAvantPartie = Box.createHorizontalBox();

        //Lancer la partie
        String [] menuListe = {"Facile", "Moyenne", "Difficile"};
        JComboBox boutonIA = new JComboBox<>(menuListe);
        boutonIA.setPreferredSize(new Dimension(200, 25));
        boutonIA.setMaximumSize(new Dimension(450, 25));
        DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center-aligned items
        boutonIA.setRenderer(listRenderer);
        boutonIA.setEditable(true);
        boutonIA.setSelectedItem("Joueur contre une IA");
        boutonIA.setEditable(false);
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

        frame.add(boiteAvantPartie);

        frame.revalidate();
        frame.repaint();
        //plateauGraphique.addMouseListener(new AdaptateurSouris(gaufreG, controle));
    }

    public void retourMenu(){
        frame.remove(boiteInfo);
        frame.add(boiteMenu);
        commencer.setEditable(true);
        commencer.setSelectedItem("Commencer une partie");
        commencer.setEditable(false);
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
}
