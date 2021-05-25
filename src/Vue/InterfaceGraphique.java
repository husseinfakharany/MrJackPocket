package Vue;

import Controle.CollecteurEvenements;
import Modele.Jeu;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class InterfaceGraphique implements Observer, Runnable {
    static int MIN_WIDTH = 1080;
    static int MIN_HEIGHT = 720;
    Jeu jeu;
    JFrame frame;
    private Box boiteMenu = null,boiteTitre  = null,boiteBoutons = null, boiteJeu = null, boiteAvantPartie = null,
            boiteCharger = null;
    boolean ia,isJack;
    private JButton voirJack;
    JButton undo;
    JButton redo;
    JButton menu;
    JLabel info,tour;
    CollecteurEvenements controle;
    JComboBox<String> commencer, boutonIA;
    private DistrictGraphique district;
    private PiocheGraphique pioche;
    private JetonsGraphique jetons;
    IdentiteGraphique identite;
    private MainGraphique main;
    Box boiteBas, boiteBasG, boiteInfo, boiteUnReDo, boiteCentreD, courant;

    // TODO Implémentation Tutoriel

    InterfaceGraphique(Jeu j, CollecteurEvenements c) {
        jeu = j;
        jeu.addObserver(this);
        controle = c;
        controle.fixerInterfaceUtilisateur(this);
        setDistrict(new DistrictGraphique(jeu));
        pioche = new PiocheGraphique(jeu);
        setJetons(new JetonsGraphique(jeu));
        identite = new IdentiteGraphique(jeu);
        main = new MainGraphique(jeu);
    }

    public static void demarrer(Jeu j, CollecteurEvenements c) {
        SwingUtilities.invokeLater(new InterfaceGraphique(j, c));
    }

    public void run() {
        frame = new JFrame("Mr Jack Pocket");
        frame.add(getBoiteMenu());
        courant = boiteMenu;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(MIN_WIDTH,MIN_HEIGHT);
        frame.addComponentListener(new EcouteurFenetre(this));
        frame.setVisible(true);
    }

    public void activeIA(){

    }

    public void changerMenu(Box ancienne,Box nouvelle){
        courant = nouvelle;
        frame.remove(ancienne);
        frame.add(nouvelle);
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

    public JButton nouveauBouton(String text){
        JButton bouton = new JButton(text);
        bouton.setMaximumSize(new Dimension(400, 40));
        bouton.setFont(new Font("default",Font.PLAIN,25));
        return bouton;
    }

    public JButton nouveauBouton(String text, int width, int height){
        JButton bouton = new JButton(text);
        bouton.setMaximumSize(new Dimension(width, height));
        bouton.setFont(new Font("default",Font.PLAIN,25));
        return bouton;
    }
    //Description des boites
    public Box getBoiteMenu(){
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

    public Box getBoiteTitre(){
        if (boiteTitre == null){
            boiteTitre = Box.createVerticalBox();

            //Alignement et ajout des pemiers éléments
            JLabel titre = new JLabel("Mr Jack Pocket");
            titre.setFont(new Font(Font.SANS_SERIF,  Font.PLAIN, 50));
            titre.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel astuces = new JLabel("Astuces : Cliquez sur le bouton commencer une partie !");
            astuces.setFont(new Font(Font.SANS_SERIF,  Font.PLAIN, 25));
            astuces.setAlignmentX(Component.CENTER_ALIGNMENT);

            boiteTitre.add(Box.createVerticalGlue());
            boiteTitre.add(titre);
            boiteTitre.add(astuces);
            boiteTitre.add(Box.createVerticalGlue());
        }
        return boiteTitre;
    }

    public Box getBoiteBoutons(){
        if (boiteBoutons == null){
            JButton charger,tuto,quitter;

            //Lancer la partie
            String [] menuListe = {"Enquêteur", "Meurtrier", "Ordi Vs Ordi"};
            commencer = new JComboBox<>(menuListe);
            commencer.setMaximumSize(new Dimension(400, 40));
            commencer.setPreferredSize(new Dimension(400, 40));
            commencer.setFont(new Font("default",Font.PLAIN,25));
            DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
            listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center-aligned items
            commencer.setRenderer(listRenderer);
            commencer.addActionListener(new AdaptateurCombobox(controle));
            commencer.setAlignmentX(Component.CENTER_ALIGNMENT);

            //Charger partie
            charger = nouveauBouton("Charger une partie");
            charger.addActionListener(new AdaptateurCommande(controle,"menuCharger"));
            charger.setAlignmentX(Component.CENTER_ALIGNMENT);

            //Charger partie
            quitter = nouveauBouton("Quitter");
            quitter.addActionListener(new AdaptateurCommande(controle,"quitter"));
            quitter.setAlignmentX(Component.CENTER_ALIGNMENT);

            //Règles & Tutoriel
            tuto = nouveauBouton("Règles & Tutoriel");
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

    public Box getBoiteJeu(){
        isJack = jeu.plateau().joueurCourant.isJack();
        int width = frame.getWidth(), height = frame.getHeight();
        //isJack=!isJack;

        if(boiteJeu==null)boiteInfo = Box.createHorizontalBox();
        else boiteInfo.removeAll();


        boiteInfo.setPreferredSize(new Dimension(1*width,(int) (0.104*height) ));

        tour = new JLabel();
        tour.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        tour.setText("  Tour n°"+jeu.plateau().getNumTour());
        tour.setFont(new Font("default", Font.PLAIN, (int) (0.034*height) ));

        info = new JLabel("Explications",SwingConstants.CENTER);
        info.setFont(new Font("default", Font.PLAIN, (int) (0.028*height) ));

        menu = nouveauBouton("Retour au Menu", (int) (0.231*width) , (int) (0.056*height) );
        menu.addActionListener(new AdaptateurCommande(controle,"menuJ"));

        boiteInfo.add(tour);
        boiteInfo.add(Box.createHorizontalGlue());
        boiteInfo.add(info);
        boiteInfo.add(Box.createHorizontalGlue());
        boiteInfo.add(menu);


        if(boiteJeu==null) {
            boiteUnReDo = Box.createHorizontalBox();
            undo = new JButton("⏪");
            undo.addActionListener(new AdaptateurCommande(controle,"annuler"));
            redo = new JButton("⏩");
            redo.addActionListener(new AdaptateurCommande(controle,"refaire"));
            setVoirJack(new JButton("Voir mes cartes"));
            getVoirJack().addActionListener(new AdaptateurCommande(controle,"main"));
        }
        else boiteUnReDo.removeAll();

        undo.setFont(new Font("default", Font.PLAIN, (int) (0.048*height) ));
        undo.setPreferredSize(new Dimension( (int) (0.093*width), (int) (0.056*height)));
        undo.setMaximumSize(new Dimension((int) (0.093*width), (int) (0.056*height)));

        redo.setFont(new Font("default", Font.PLAIN, 35));
        redo.setPreferredSize(new Dimension((int) (0.093*width), (int) (0.056*height)));
        redo.setMaximumSize(new Dimension((int) (0.093*width), (int) (0.056*height)));


        getVoirJack().setFont(new Font("default", Font.PLAIN, 16));
        getVoirJack().setPreferredSize(new Dimension((int) (0.185*width), (int) (0.056*height)));
        getVoirJack().setMaximumSize(new Dimension((int) (0.185*width), (int) (0.056*height)));

        getVoirJack().setAlignmentX(JComponent.CENTER_ALIGNMENT);


        boiteUnReDo.add(Box.createHorizontalGlue());
        boiteUnReDo.add(undo);
        boiteUnReDo.add(redo);
        boiteUnReDo.add(Box.createHorizontalGlue());


        if(boiteCentreD == null) boiteCentreD = Box.createVerticalBox();
        else boiteCentreD.removeAll();

        getJetons().setPreferredSize(new Dimension((int) (0.223*width),(int) (0.334*height)));
        boiteCentreD.add(Box.createVerticalGlue());
        boiteCentreD.add(getJetons());
        boiteCentreD.add(Box.createVerticalGlue());

        Box boiteCentre = Box.createHorizontalBox();

        identite.setPreferredSize(new Dimension((int) (0.223*width),boiteCentre.getHeight()));
        getDistrict().setPreferredSize(new Dimension( (int) (0.4*width),(int) (0.4*width) ));

        boiteCentre.add(Box.createHorizontalGlue());
        boiteCentre.add(identite);
        boiteCentre.add(Box.createHorizontalGlue());
        boiteCentre.add(getDistrict());
        boiteCentre.add(Box.createHorizontalGlue());
        boiteCentre.add(boiteCentreD);
        boiteCentre.add(Box.createHorizontalGlue());

        boiteBasG = Box.createVerticalBox();

        boiteBasG.setMaximumSize(new Dimension((int) (0.37*width),(int) (0.278*height) ));

        boiteBasG.add(getVoirJack());
        boiteBasG.add(Box.createVerticalGlue());
        boiteBasG.add(boiteUnReDo);
        boiteBasG.add(Box.createVerticalGlue());


        if(boiteBas == null) boiteBas = Box.createHorizontalBox();
        else boiteBas.removeAll();

        boiteBas.setMaximumSize(new Dimension(width,(int) (0.243*height) ));
        boiteBas.setPreferredSize(new Dimension(width,(int) (0.243*height)));
        boiteBasG.setPreferredSize(new Dimension((int) (0.175*width),boiteBas.getHeight()));
        getMain().setPreferredSize(new Dimension((int) (0.8*width),(int) (0.243*height) ));
        getPioche().setPreferredSize(new Dimension((int) (0.175*width),(int) (0.243*height)));
        getPioche().setMaximumSize(new Dimension((int) (0.175*width),(int) (0.243*height)));

        boiteBas.add(boiteBasG);
        boiteBas.add(Box.createHorizontalGlue());
        boiteBas.add(getMain());
        boiteBas.add(Box.createHorizontalGlue());
        boiteBas.add(getPioche());

        if(boiteJeu == null) {
            getDistrict().addMouseListener(new AdaptateurSouris(getDistrict(), controle));
            getJetons().addMouseListener(new AdaptateurSouris(getJetons(), controle));
            getPioche().addMouseListener(new AdaptateurSouris(getPioche(), controle));
        }


        if(boiteJeu == null) boiteJeu = Box.createVerticalBox();
        else boiteJeu.removeAll();

        boiteJeu.add(boiteInfo);
        boiteJeu.add(boiteCentre);
        boiteJeu.add(boiteBas);

        boiteJeu.setOpaque(true);
        boiteJeu.setBackground(Color.WHITE);

        getVoirJack().setEnabled(isJack);
        getVoirJack().setVisible(isJack);
        identite.repaint();
        getDistrict().repaint();
        getPioche().repaint();
        getMain().repaint();
        getJetons().repaint();
        tour.setText("  Tour n°"+jeu.plateau().getNumTour());

        return boiteJeu;
    }

    public Box getBoiteAvantPartie(){
        if (boiteAvantPartie == null){
            boiteAvantPartie = Box.createHorizontalBox();

            //Lancer la partie
            String [] menuListe = {"Facile", "Moyenne", "Difficile"};
            boutonIA = new JComboBox<>(menuListe);
            boutonIA.setPreferredSize(new Dimension(400, 40));
            boutonIA.setMaximumSize(new Dimension(400, 40));
            boutonIA.setFont(new Font("default",Font.PLAIN,25));
            DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
            listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center-aligned items
            boutonIA.setRenderer(listRenderer);
            boutonIA.addActionListener(new AdaptateurCombobox(controle));
            boutonIA.setAlignmentX(Component.CENTER_ALIGNMENT);

            //Charger partie
            JButton local = nouveauBouton("Joueur contre un ami");
            local.addActionListener(new AdaptateurCommande(controle,"local"));
            local.setAlignmentX(Component.CENTER_ALIGNMENT);

            //Charger partie
            JButton reseau = nouveauBouton("Joueur en réseau");
            reseau.addActionListener(new AdaptateurCommande(controle,"reseau"));
            reseau.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel titre = new JLabel("Mr Jack Pocket");
            titre.setFont(new Font(Font.SANS_SERIF,  Font.PLAIN, 50));
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

    public Box getBoiteCharger(){
        if (boiteCharger == null){
            boiteCharger = Box.createHorizontalBox();

            //Recuperer la liste des parties (en attendant)
            String [] listPartie = {"Partie 1 - 28/02/21", "Partie 2 - 04/03/21", "Partie 3 - 05/05/21",
                    "Partie 4 - 18/05/21","Partie 1 - 28/02/21", "Partie 2 - 04/03/21","Partie 1 - 28/02/21",
                    "Partie 2 - 04/03/21","Partie 1 - 28/02/21", "Partie 2 - 04/03/21","Partie 1 - 28/02/21",
                    "Partie 2 - 04/03/21","Partie 1 - 28/02/21", "Partie 2 - 04/03/21","Partie 1 - 28/02/21",
                    "Partie 2 - 04/03/21","Partie 1 - 28/02/21", "Partie 2 - 04/03/21","Partie 1 - 28/02/21",
                    "Partie 2 - 04/03/21","Partie 1 - 28/02/21", "Partie 2 - 04/03/21","Partie 1 - 28/02/21" };

            JList list = new JList(listPartie);
            list.setAlignmentX(Component.CENTER_ALIGNMENT);
            list.setLayoutOrientation(JList.VERTICAL);
            list.setFont(new Font("default",Font.PLAIN,20));
            JScrollPane listScroller = new JScrollPane(list);
            listScroller.setPreferredSize(new Dimension(300, 350));
            listScroller.setMaximumSize(new Dimension(300, 350));


            Box boiteTitre = Box.createVerticalBox();

            JLabel titre = new JLabel("Mr Jack Pocket");
            titre.setFont(new Font(Font.SANS_SERIF,  Font.PLAIN, 50));
            titre.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel astuces = new JLabel("Astuces : Cliquez sur une partie et finissez là !");
            astuces.setFont(new Font(Font.SANS_SERIF,  Font.PLAIN, 25));
            astuces.setAlignmentX(Component.CENTER_ALIGNMENT);

            Box boiteList = Box.createHorizontalBox();
            boiteList.add(Box.createHorizontalGlue());
            boiteList.add(listScroller);
            boiteList.add(Box.createHorizontalGlue());

            Box boiteBoutons = Box.createHorizontalBox();

            JButton charger = nouveauBouton("Charger partie",250,40);
            charger.addActionListener(new AdaptateurCommande(controle,"charger"));

            JButton menu = nouveauBouton("Retour au Menu",250,40);
            menu.addActionListener(new AdaptateurCommande(controle,"menuC"));

            boiteBoutons.add(Box.createHorizontalGlue());
            boiteBoutons.add(charger);
            boiteBoutons.add(Box.createHorizontalGlue());
            boiteBoutons.add(menu);
            boiteBoutons.add(Box.createHorizontalGlue());

            boiteTitre.add(Box.createVerticalGlue());
            boiteTitre.add(titre);
            boiteTitre.add(astuces);
            boiteTitre.add(Box.createVerticalGlue());
            boiteTitre.add(boiteBoutons);
            boiteTitre.add(Box.createVerticalGlue());

            boiteCharger.add(boiteList, BorderLayout.PAGE_START);
            boiteCharger.add(boiteTitre);
        }
        return boiteCharger;
    }

    public Box getCourant(){
        return courant;
    }

    public MainGraphique getMain() {
        return main;
    }

    public JButton getVoirJack() {
        return voirJack;
    }

    public void setVoirJack(JButton voirJack) {
        this.voirJack = voirJack;
    }

    public JetonsGraphique getJetons() {
        return jetons;
    }

    public void setJetons(JetonsGraphique jetons) {
        this.jetons = jetons;
    }

    public DistrictGraphique getDistrict() {
        return district;
    }

    public void setDistrict(DistrictGraphique district) {
        this.district = district;
    }

    public PiocheGraphique getPioche() {
        return pioche;
    }
}
