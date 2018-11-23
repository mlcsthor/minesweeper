import com.apple.eawt.Application;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

/**
 * Created by thorx on 18/05/2017.
 */
public class View extends JFrame {
    private Model model;

    private JButton newGame, home, aides, playMusic;
    protected JMenu menu, mNouveau, aide;
    protected JLabel jlNbDrapeaux, jltime, iconNbDrapeau, iconTime, jlScore, iconScore, jlMeilleursScores1, jlMeilleursScores2, jlMeilleursScores3, iconMeilleursScores;
    protected ControlMenu CMenu;
    protected JLabel[][] caseP;
    private JPanel jpPlateau, jpInfos;
    private PanelPrinc jpp;
    protected JMenuItem mAide = new JMenuItem("Règles");
    protected JMenuItem mAPropos = new JMenuItem("À propos");
    protected JMenuItem mQuit = new JMenuItem("Quitter");
    protected JMenuItem mNDebutant = new JMenuItem("Débutant");
    protected JMenuItem mNNormal = new JMenuItem("Normal");
    protected JMenuItem mNExpert = new JMenuItem("Expert");
    private int iconSize;
    private BackgroundMusic backgroundMusic;
    private Image icone;
    private ImageIcon iconeDUT;
    private ImageIcon iconeApp;
    private Image img;
    private Application app;


    /**
     * Créer une vue principale
     * @param model le model
     * @param backgroundMusic la musique de fond
     */
    public View(Model model, BackgroundMusic backgroundMusic) {
        this.model = model;
        this.iconSize = this.model.getSizeOfIcon();
        this.backgroundMusic = backgroundMusic;
        this.setTitle("Démineur");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setSize(1100, 700);
        this.setLocationRelativeTo(null);
        this.init();
        this.initMenu();
        this.initPlateau();
        this.initInfos();
        this.createView();

        this.icone = Toolkit.getDefaultToolkit().getImage("res/img/bombico.png");
        this.iconeDUT = new ImageIcon("res/img/dut.png");
        ImageIcon imageIcon = new ImageIcon("res/img/bombico.png"); //unscaled image
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH); // resize it here
        this.iconeApp = new ImageIcon(newimg);  // transform it back
        imageIcon = new ImageIcon("res/img/dut.png"); //unscaled image
        image = imageIcon.getImage();
        newimg = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH); // resize it here
        this.iconeDUT = new ImageIcon(newimg);
        this.img = new ImageIcon("res/img/bombico.png").getImage();
        this.app = Application.getApplication();
        try {
            app.setDockIconImage(img);
        } catch(NullPointerException e) {
            //e.printStackTrace();
        }
        this.setIconImage(icone);
    }

    /**
     * Définir le listener pour la souris
     * @param listener le listener
     */
    public void setMouseListener(MouseAdapter listener) {
        jpPlateau.addMouseMotionListener(listener);
        jpPlateau.addMouseListener(listener);
    }

    /**
     * Créer la vue
     */
    public void createView() {
        //Si aucun niveau défini
        if (this.model.getLevel() == 0) {
            Object[] possibilities = {"Débutant", "Normal", "Expert", "Personnalisé"};
            String s = (String) JOptionPane.showInputDialog(this, "Choisir un niveau", "Choix du niveau", JOptionPane.PLAIN_MESSAGE, null, possibilities, "Débutant");

            if (s.equals("Débutant")) {
                this.model.setLevel(1);
                this.createView();
                //this.model.setNbBombes(10)
            } else if (s.equals("Normal")) {
                this.model.setLevel(2);
                this.createView();
            } else if (s.equals("Expert")) {
                this.model.setLevel(3);
                this.createView();
            } else {
                System.out.println("Personnalisé");
            }
        } else {
            jpp = new PanelPrinc(1225, 700);
            jpp.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            jpPlateau.setOpaque(false);
            jpInfos.setOpaque(false);

            gbc.insets = new Insets(0, 50, 0, 10);
            gbc.weightx = 0.70;
            gbc.weighty = 1;

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.LINE_START;
            jpp.add(jpPlateau, gbc);

            gbc.insets = new Insets(0, 10, 0, 50);
            gbc.weightx = 0.30;
            gbc.gridx = 1;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            ;
            gbc.gridy = 0;
            gbc.gridheight = 1;
            jpp.add(jpInfos, gbc);

            setContentPane(jpp);
            validate();
        }
    }

    /**
     * Initialiser les infos de la vue
     */
    public void initInfos() {
        jpInfos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = gbc.gridy = 0; // la grille commence en (0, 0)
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.LINE_START; // ou BASELINE_LEADING mais pas WEST.
        gbc.insets = new Insets(20, 20, 0, 0);
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        jpInfos.add(home, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        jpInfos.add(playMusic,gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        jpInfos.add(aides, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        jpInfos.add(iconTime, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        jpInfos.add(jltime, gbc);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 2;
        jpInfos.add(iconNbDrapeau, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
        jpInfos.add(jlNbDrapeaux, gbc);
        gbc.anchor = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 3;
        jpInfos.add(iconScore, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        jpInfos.add(jlScore, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        jpInfos.add(iconMeilleursScores, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        jpInfos.add(jlMeilleursScores1, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        jpInfos.add(jlMeilleursScores2, gbc);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        jpInfos.add(jlMeilleursScores3, gbc);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        jpInfos.add(newGame, gbc);
    }

    /**
     * Initialiser le menu
     */
    public void initMenu() {
        mNouveau.add(mNDebutant);
        mNouveau.add(mNNormal);
        mNouveau.add(mNExpert);
        menu.add(mNouveau);
        menu.addSeparator();
        aide.add(mAide);
        aide.add(mAPropos);
        menu.add(mQuit);

        JMenuBar mbar = new JMenuBar();
        mbar.add(menu);
        mbar.add(aide);
        setJMenuBar(mbar);

        CMenu = new ControlMenu(this, model, backgroundMusic);
        mQuit.addActionListener(CMenu);
        mNDebutant.addActionListener(CMenu);
        mNNormal.addActionListener(CMenu);
        mNExpert.addActionListener(CMenu);
        mAide.addActionListener(CMenu);
        mAPropos.addActionListener(CMenu);
        ///
    }

    /**
     * Initialiser les éléments graphiques : menu et score
     */
    public void init() {
        File is = new File("./res/font/monodb_.ttf");
        Font font = null;
        try {
            //System.out.println("test");
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        Font sizedFont = font.deriveFont(25f);
        jlNbDrapeaux = new JLabel(Integer.toString(this.model.getNbBombes()));
        menu = new JMenu("Menu");
        aide = new JMenu("Aide");
        mNouveau = new JMenu("Nouvelle Partie");
        jltime = new JLabel("00min 00s");

        jlNbDrapeaux.setFont(sizedFont);
        jlNbDrapeaux.setPreferredSize(new Dimension(80, 50));

        jltime.setFont(sizedFont);
        jltime.setPreferredSize(new Dimension(160, 50));

        jlScore = new JLabel("0");
        jlScore.setFont(sizedFont);

        if(this.model.getHighscore().size()==0){
            jlMeilleursScores1 = new JLabel("0");
            jlMeilleursScores1.setFont(sizedFont);
            jlMeilleursScores2 = new JLabel("0");
            jlMeilleursScores2.setFont(sizedFont);
            jlMeilleursScores3 = new JLabel("0");
            jlMeilleursScores3.setFont(sizedFont);
        }
        else if (this.model.getHighscore().size()==1) {
            jlMeilleursScores1 = new JLabel(this.model.getHighscore().get(0).getPseudo() + " : " + this.model.getHighscore().get(0).getScore());
            jlMeilleursScores1.setFont(sizedFont);
            jlMeilleursScores2 = new JLabel("0");
            jlMeilleursScores2.setFont(sizedFont);
            jlMeilleursScores3 = new JLabel("0");
            jlMeilleursScores3.setFont(sizedFont);
        }
        else if (this.model.getHighscore().size()==2) {
            jlMeilleursScores1 = new JLabel(this.model.getHighscore().get(0).getPseudo() + " : " + this.model.getHighscore().get(0).getScore());
            jlMeilleursScores1.setFont(sizedFont);
            jlMeilleursScores2 = new JLabel(this.model.getHighscore().get(1).getPseudo() + " : " + this.model.getHighscore().get(1).getScore());
            jlMeilleursScores2.setFont(sizedFont);
            jlMeilleursScores3 = new JLabel("0");
            jlMeilleursScores3.setFont(sizedFont);
        }
        else if (this.model.getHighscore().size()>=3) {
            jlMeilleursScores1 = new JLabel(this.model.getHighscore().get(0).getPseudo() + " : " + this.model.getHighscore().get(0).getScore());
            jlMeilleursScores1.setFont(sizedFont);
            jlMeilleursScores2 = new JLabel(this.model.getHighscore().get(1).getPseudo() + " : " + this.model.getHighscore().get(1).getScore());
            jlMeilleursScores2.setFont(sizedFont);
            jlMeilleursScores3 = new JLabel(this.model.getHighscore().get(2).getPseudo() + " : " + this.model.getHighscore().get(2).getScore());
            jlMeilleursScores3.setFont(sizedFont);
        }


        home = new JButton();
        home.setPreferredSize(new Dimension(120, 50));
        home.setIcon(setIconButton("Menu",false,120,50));

        aides = new JButton();
        aides.setPreferredSize(new Dimension(120, 50));
        aides.setIcon(setIconButton("Aides",false,120,50));

        newGame = new JButton();
        newGame.setPreferredSize(new Dimension(166, 50));
        newGame.setIcon(setIconButton("newGame",true,166,50));

        playMusic = new JButton();
        playMusic.setPreferredSize(new Dimension(50, 50));
        if(this.backgroundMusic.isPlay()){
            playMusic.setIcon(setIconButton("play",false,50,50));
        }else{
            playMusic.setIcon(setIconButton("stop",false, 50,50));
        }
        iconNbDrapeau = new JLabel(setIconButton("nbDrapeaux",false, 235,35));
        iconTime = new JLabel(setIconButton("temps",false, 150,35));
        iconMeilleursScores = new JLabel(setIconButton("MeilleursScores",false, 300,35));
        iconScore = new JLabel(setIconButton("score",false, 150,35));
    }

    /**
     * Initialiser le plateau
     */
    public void initPlateau(){
        int hgap = 0, vgap;
        if(this.model.getLevel() == 1){
            vgap = 5;
        }else if(this.model.getLevel() == 2){
            vgap = 2;
        }else{
            vgap = 1;
        }
        jpPlateau = new JPanel(new GridLayout(this.model.getSize(),this.model.getSize(),hgap,vgap));

        caseP=new JLabel[this.model.getSize()][this.model.getSize()];
        for(int i=0;i<this.model.getSize();i++) {
            for(int j=0;j<this.model.getSize();j++){
                caseP[i][j] = new JLabel();
                caseP[i][j].setOpaque(false);
                setNormalIcon(i,j);
                jpPlateau.add(caseP[i][j]);
            }
        }
    }

    /**
     * Récupérer la label affichant le temps
     * @return le label
     */
    public JLabel getJltime() {
        return jltime;
    }

    /**
     * Récupèrer la musique de fond
     * @return la musique de fond
     */
    public BackgroundMusic getBackgroundMusic(){return this.backgroundMusic;}

    /**
     * Afficher la vue
     */
    public void display() {
        this.setVisible(true);
    }

    /**
     * Définir l'icône de bombe
     * @param i la ligne dans laquelle la case est
     * @param j la colonne dans laquelle la case est
     * @param couleur la couleur de bombe
     */
    public void setBombe(int i, int j, String couleur){
        BufferedImage imageCase = null;
        try {
            imageCase = ImageIO.read(new File("./res/img/bombe" + model.getLevel() + couleur + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon iconCase = new ImageIcon(imageCase);
        iconCase = new ImageIcon(iconCase.getImage().getScaledInstance(iconSize, iconSize, BufferedImage.SCALE_SMOOTH));
        caseP[i][j].setIcon(iconCase);
    }

    /**
     * Définir l'icône du nombre de bombes
     * @param i la ligne dans laquelle la case est
     * @param j la colonne dans laquelle la case est
     * @param nb le nombre de bombes
     */
    public void setNombreBombes(int i, int j, int nb){
        BufferedImage imageCase = null;
        try {
            imageCase = ImageIO.read(new File("./res/img/"+nb+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon iconCase = new ImageIcon(imageCase);
        iconCase = new ImageIcon(iconCase.getImage().getScaledInstance(iconSize, iconSize, BufferedImage.SCALE_SMOOTH));
        caseP[i][j].setIcon(iconCase);
    }

    /**
     * Définir l'icône de drapeau
     * @param i la ligne sur laquelle la case est
     * @param j la colonne sur laquelle la case est
     */
    public void setDrapeau(int i, int j){
        BufferedImage imageCase = null;
        try {
            imageCase = ImageIO.read(new File("./res/img/caseDrapeau.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon iconCase = new ImageIcon(imageCase);
        iconCase = new ImageIcon(iconCase.getImage().getScaledInstance(iconSize, iconSize, BufferedImage.SCALE_SMOOTH));
        caseP[i][j].setIcon(iconCase);
    }

    /**
     * Définir l'icône de point d'interrogation
     * @param i la ligne sur laquelle la case est
     * @param j la ligne sur laquelle la case est
     */
    public void setInterrogation(int i,int j){
        BufferedImage imageCase=null;
        try {
            imageCase = ImageIO.read(new File("./res/img/caseInterrogation.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon iconCase = new ImageIcon(imageCase);
        iconCase = new ImageIcon(iconCase.getImage().getScaledInstance(iconSize, iconSize, BufferedImage.SCALE_SMOOTH));
        caseP[i][j].setIcon(iconCase);
    }

    /**
     * Définir l'icône de point d'interrogation au survol
     * @param i la ligne sur laquelle la case est
     * @param j la colonne sur laquelle la case est
     */
    public void setHoverIconeInterrogation(int i,int j){
        BufferedImage imageCase=null;
        try {
            imageCase = ImageIO.read(new File("./res/img/HovercaseInterrogation.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon iconCase = new ImageIcon(imageCase);
        iconCase = new ImageIcon(iconCase.getImage().getScaledInstance(iconSize, iconSize, BufferedImage.SCALE_SMOOTH));
        caseP[i][j].setIcon(iconCase);
    }

    /**
     * Définir l'icône de base
     * @param i la ligne sur laquelle la case est
     * @param j la colonne sur laquelle la case est
     */
    public void setNormalIcon(int i, int j){
        BufferedImage imageCase = null;
        try {
            imageCase = ImageIO.read(new File("./res/img/case.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon iconCase = new ImageIcon(imageCase);
        iconCase = new ImageIcon(iconCase.getImage().getScaledInstance(iconSize, iconSize, BufferedImage.SCALE_SMOOTH));
        caseP[i][j].setIcon(iconCase);
    }

    /**
     * Définir l'icône de base au survol
     * @param i la ligne sur laquelle la case est
     * @param j la colonne la quelle la case est
     */
    public void setHoverIcon(int i, int j){
        BufferedImage imageCase = null;
        try {
            imageCase = ImageIO.read(new File("./res/img/Hovercase.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon iconCase = new ImageIcon(imageCase);
        iconCase = new ImageIcon(iconCase.getImage().getScaledInstance(iconSize, iconSize, BufferedImage.SCALE_SMOOTH));
        caseP[i][j].setIcon(iconCase);
    }

    /**
     * Définir l'icône de drapeau au survol
     * @param i la ligne sur laquelle la case est
     * @param j la colonne sur laquelle la case est
     */
    public void setHoverIconDrapeau(int i, int j){
        BufferedImage imageCase = null;
        try {
            imageCase = ImageIO.read(new File("./res/img/hoverCaseDrapeau.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon iconCase = new ImageIcon(imageCase);
        iconCase = new ImageIcon(iconCase.getImage().getScaledInstance(iconSize, iconSize, BufferedImage.SCALE_SMOOTH));
        caseP[i][j].setIcon(iconCase);
    }

    /**
     * Définir l'icône des boutons
     * @param nom le nom de l'icône
     * @param newGame état de la partie (nouvelle ou non)
     * @param width largeur
     * @param height hauteur
     * @return l'icône
     */
    public ImageIcon setIconButton(String nom, boolean newGame, int width, int height){

        BufferedImage image = null;
        try {
            if(!newGame) {
                image = ImageIO.read(new File("./res/img/" + nom + ".png"));
            }
            else{
                image = ImageIO.read(new File("./res/img/Nouvelle_Partie_"+ this.model.getLevel() + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageIcon icon = new ImageIcon(image);
        icon = new ImageIcon(icon.getImage().getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH));
        return icon;
    }

    /**
     * Définir le listener pour les boutons
     * @param listener le listener
     */
    public void setButtonControler(ActionListener listener){
        newGame.addActionListener(listener);
        aides.addActionListener(listener);
        home.addActionListener(listener);
        playMusic.addActionListener(listener);
    }

    /**
     * Afficher les aides
     */
    public void afficheAides(){
        File is = new File("./res/font/monodb_.ttf");
        Font font = null;
        try {
            //System.out.println("test");
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        Font sizedFont = font.deriveFont(25f);
        String aide = "Le chiffre qui  s'affiche sur les cases cliquées indique le nombre \n"  +
                "de mines se trouvant à proximité : \n" +
                "           -à gauche \n" +
                "           -à droite \n" +
                "           -en haut \n" +
                "           -en bas\n" +
                "           -en diagonale. \n" +
                "Grâce aux indications données par les chiffres, vous pouvez libérer \n" +
                " d'autres cases. En continuant de cette façon, vous pourrez \n" +
                "résoudre les premiers niveaux... Pour le niveau \"Expert\", vous \n" +
                "pourrez être confrontés à des situations plus compliquées, \n" +
                "où il faudra faire des suppositions. Une logique qui s'acquiert avec \n" +
                "l'expérience.";
        JTextArea jOptionAide = new JTextArea(aide);
        jOptionAide.setFont(sizedFont);
        jOptionAide.setOpaque(false);
        JOptionPane d = new JOptionPane();
        d.showMessageDialog( this,jOptionAide, "Aides",
                JOptionPane.INFORMATION_MESSAGE,iconeApp );

        JDialog fenInfos = d.createDialog(this, "Aides");

    }

    /**
     * Afficher la fenêtre A Propos
     */
    public void afficheAPropos(){
        File is = new File("./res/font/monodb_.ttf");
        Font font = null;
        try {
            //System.out.println("test");
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        Font sizedFont = font.deriveFont(25f);

        String aPropos = "Jeu développé en première année de DUT Informatique \n"+
                " à l'IUT de Belfort-Montbéliard par :\n\n" +
                "Thomas BICHOT \n" +
                "Maxime LUCAS\n" +
                "Benjamin ESCOBAR\n" +
                "Caroline PEUGEOT\n" +
                "Corentin DE OLIVEIRA\n" +
                "Thomas GARCENOT\n" +
                "Nathan MERCIER" +
                "\n\n @2017";
        JTextArea jOptionAPropos = new JTextArea(aPropos);
        jOptionAPropos.setFont(sizedFont);
        jOptionAPropos.setOpaque(false);

        JOptionPane d = new JOptionPane();
        d.showMessageDialog( this,jOptionAPropos , "À Propos",
                JOptionPane.INFORMATION_MESSAGE, iconeDUT);

        JDialog fenInfos = d.createDialog(this, "À Propos");

    }

    /**
     * Lancer une nouvelle partie
     * @param model le model
     */
    public void nouvellePartie(Model model){
        this.model = model;
        BufferedImage imageCase = null;
        try {
            imageCase = ImageIO.read(new File("./res/img/case.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon iconCase = new ImageIcon(imageCase);
        iconCase = new ImageIcon(iconCase.getImage().getScaledInstance(iconSize, iconSize, BufferedImage.SCALE_SMOOTH));
        for(int i=0;i<this.model.getSize();i++) {
            for (int j = 0; j < this.model.getSize(); j++) {
                caseP[i][j].setIcon(iconCase);
            }
        }
        this.model.setView(this);
        this.model.addTimer();
        this.jltime.setText("00min 00s");
    }

    /**
     * Récupérer le bouton pour la musique
     * @return le bouton
     */
    public JButton getPlayMusic() {
        return playMusic;
    }

    /**
     * Récupérer le bouton pour la nouvelle partie
     * @return le bouton
     */
    public JButton getNewGame() {
        return newGame;
    }

    /**
     * Récupérer le bouton d'accueil
     * @return le bouton
     */
    public JButton getHome() {
        return home;
    }

    /**
     * Récupérer le bouton d'aide
     * @return le bouton
     */
    public JButton getAides() {
        return aides;
    }
}
