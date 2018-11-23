import com.apple.eawt.Application;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tbichot on 21/05/2017.
 */
public class ViewMenu extends JFrame {

    private JButton debutant, normal, expert, personnalise, quitter,aPropos;
    private JLabel title;
    private JButton playMusic;
    private BackgroundMusic backgroundMusic;
    private Image icone;
    private ImageIcon iconeDUT;
    private Image img;
    private Application app;

    public ViewMenu(BackgroundMusic backgroundMusic) {
        this.backgroundMusic = backgroundMusic;
        this.init();
        this.createView();
        this.pack();
        this.setSize(628, 400);
        this.setLocationRelativeTo(null);
        this.setTitle("Démineur");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.display();
        this.icone = Toolkit.getDefaultToolkit().getImage("res/img/bombico.png");
        this.img = new ImageIcon("res/img/bombico.png").getImage();  // your desired image
        this.app = Application.getApplication();
        try {
            app.setDockIconImage(img);
        } catch(NullPointerException e) {
            //e.printStackTrace();
        }
        this.setIconImage(icone);

    }

    public void setButtonControler(ActionListener listener) {
        debutant.addActionListener(listener);
        normal.addActionListener(listener);
        expert.addActionListener(listener);
        personnalise.addActionListener(listener);
        quitter.addActionListener(listener);
        aPropos.addActionListener(listener);
        playMusic.addActionListener(listener);
    }

    public void init(){
        playMusic = new JButton();
        debutant = new JButton();
        normal = new JButton();
        expert = new JButton();
        personnalise = new JButton();
        quitter = new JButton();
        aPropos = new JButton();
        title = new JLabel();
        BufferedImage imageTitle = null;
        try {
            imageTitle = ImageIO.read(new File("./res/img/logo_title.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon imageIcon = new ImageIcon("res/img/dut.png"); //unscaled image
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH); // resize it here
        this.iconeDUT = new ImageIcon(newimg);  // transform it back
        ImageIcon iconTitle = new ImageIcon(imageTitle);
        iconTitle = new ImageIcon(iconTitle.getImage().getScaledInstance(385, 45, BufferedImage.SCALE_SMOOTH));
        title.setIcon(iconTitle);

        playMusic.setPreferredSize(new Dimension(50, 50));
        debutant.setPreferredSize(new Dimension(128, 180));
        normal.setPreferredSize(new Dimension(128, 180));
        expert.setPreferredSize(new Dimension(128, 180));
        quitter.setPreferredSize(new Dimension(120, 50));
        aPropos.setPreferredSize(new Dimension(120, 50));

        if(this.backgroundMusic.isPlay()){
            playMusic.setIcon(setButtonIcon("play",50,50));
        }else{
            playMusic.setIcon(setButtonIcon("stop",50,50));
        }
        debutant.setIcon(setButtonIcon("debutant", 128,180));
        normal.setIcon(setButtonIcon("normal", 128,180));
        expert.setIcon(setButtonIcon("expert", 128,180));
        quitter.setIcon(setButtonIcon("quitter", 120,50));
        aPropos.setIcon(setButtonIcon("aPropos", 120,50));

    }
    public void createView(){
        PanelPrinc jpp = new PanelPrinc(628,400);
        jpp.setLayout(new GridLayout(1,1));
        JPanel jPanelGlobal = new JPanel(new GridBagLayout());
        JPanel jPanelNiveau = new JPanel(new GridBagLayout());
        JPanel jPanelTitle = new JPanel();
        JPanel jPanelOptions = new JPanel(new GridBagLayout());

        //jPanelOptions.setLayout(new BoxLayout(jPanelOptions, BoxLayout.Y_AXIS));

        jPanelGlobal.setOpaque(false);
        jPanelTitle.setOpaque(false);
        jPanelNiveau.setOpaque(false);
        jPanelOptions.setOpaque(false);

        jPanelTitle.add(title,new GridBagConstraints());

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = gbc2.gridy = 0; // la grille commence en (0, 0)

        gbc2.gridheight = 1; // valeur par défaut - peut s'étendre sur une seule ligne.

        gbc2.insets = new Insets(5, 20, 0, 20);
        jPanelNiveau.add(debutant,gbc2);
        gbc2.gridx = 1;
        gbc2.gridy = 0;

        jPanelNiveau.add(normal,gbc2);

        gbc2.gridx = 2;
        gbc2.gridy = 0;
        jPanelNiveau.add(expert,gbc2);

        gbc2.insets = new Insets(0, 10, 0, 10);
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        jPanelOptions.add(aPropos, gbc2);
        gbc2.gridx = 1;
        gbc2.gridy = 0;
        jPanelOptions.add(playMusic, gbc2);
        gbc2.gridx = 2;
        gbc2.gridy = 0;
        jPanelOptions.add(quitter, gbc2);


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0; // la grille commence en (0, 0)

        gbc.gridwidth = GridBagConstraints.REMAINDER; // seul composant de sa colonne, il est donc le dernier.
        gbc.gridheight = 1; // valeur par défaut - peut s'étendre sur une seule ligne.

        gbc.anchor = GridBagConstraints.CENTER; // ou BASELINE_LEADING mais pas WEST.

        gbc.insets = new Insets(5, 0, 0, 0); // Marge à gauche de 15 et marge au dessus de 10.
/* - les attributs ipadx, ipdady, weightx et weighty valent tous 0 (valeur par défaut).
 * - l'attribut fill est à NONE, car on ne souhaite pas de redimentionnement pour cette étiquette. */


        jPanelGlobal.add(jPanelTitle,gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        jPanelGlobal.add(jPanelNiveau, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        jPanelGlobal.add(jPanelOptions, gbc);
        jpp.add(jPanelGlobal);

        setContentPane(jpp);

    }
    public void display() {
        this.setVisible(true);
    }

    public void undisplay(){ this.setVisible(false); }

    public JButton getDebutant() {
        return debutant;
    }

    public JButton getNormal() {
        return normal;
    }

    public JButton getPlayMusic() {
        return playMusic;
    }

    public JButton getExpert() {
        return expert;
    }

    public JButton getQuitter() {
        return quitter;
    }

    public JButton getaPropos() {
        return aPropos;
    }

    public ImageIcon setButtonIcon(String buttonName, int width, int height){
        BufferedImage imageCase = null;
        try {
            imageCase = ImageIO.read(new File("./res/img/" + buttonName +".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon iconCase = new ImageIcon(imageCase);
        iconCase = new ImageIcon(iconCase.getImage().getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH));
        return iconCase;
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
        Font sizedFont = font.deriveFont(15f);

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
}
