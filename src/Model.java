import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by thorx on 18/05/2017.
 */
public class Model {
    private Cell[][] cells; // X définit par le controller en fonction du jeu
    private ArrayList<Cell> listBombs;
    private int nbBombes;
    private int score; //score de la partie actuelle, 0 au lancement
    private double scoreFinal; // score finale calculé à la fin de la partie en fonction du score normal et du temps
    private ArrayList<Score> highscore; // à récup dans un fichier ? de la forme "pseudo : score " ? => pb pour le traitement mais plus simple pour l'affichage
    private int time; // nombre de secondes
    private Timer timer;
    private int level;
    private View view;
    private int nbCases;
    private int size;
    private int sizeOfIcon;
    private int nbBombeTrouve;
    private int nbFlagPose;
    private ArrayList<Score> listScores;
    private boolean partieFinie=false;

    /**
     * Récupérer le timer
     * @return le timer
     */
    public Timer getTimer() {
        return this.timer;
    }

    /**
     * Créer un model à partir d'un niveau
     * @param level le niveau choisi
     */
    public Model(int level){
        this.level = level;
        init();
        this.cells = new Cell[size][size];
        this.listBombs = new ArrayList<Cell>();
        this.listScores=new ArrayList<Score>();
        this.highscore = getScoreFromFile();
        this.initCells();
        this.placerBombe();
        this.calculNBBombesAutour();
    }

    /**
     * Créer un model à partir d'une taille et d'un nombre de bombes
     * @param X nombre de lignes
     * @param Y nombre de colonnes
     * @param nbBombes nombre de bombes
     */
    public Model(int X,int Y ,int nbBombes) {
        this.level = 0;
        this.cells = new Cell[X][Y];
        this.listBombs = new ArrayList<Cell>(nbBombes);
        this.nbBombes=nbBombes;
        this.placerBombe();
        // récuperation highscore
    }

    /**
     * Placer les bombes de manière aléatoire
     */
    public void placerBombe() {
        Random random = new Random();
        int i, j;
        for(int x = 0 ; x < this.nbBombes ; x++) {
            i = random.nextInt(this.size);
            j = random.nextInt(this.size);

            if(!this.cells[i][j].isBomb()) {
                this.cells[i][j].setBomb();
                this.cells[i][j].setValue(-1);
                this.listBombs.add(this.cells[i][j]);
            } else {x--;}
        }
    }

    /**
     * Récupérer le nombre totale de bombes
     * @return le nombre de bombes
     */
    public int getNbBombes(){ return this.nbBombes; }

    /**
     * Initialiser les valeurs du model en fonction du level
     */
    public void init(){
        this.nbFlagPose = 0;
        if(level == 1){
           this.nbCases = 81;
           this.size = 9;
            this.nbBombes=10;
            this.sizeOfIcon = 64;
        }else if(level == 2){
            this.nbCases = 256;
            this.size = 16;
            this.nbBombes=40;
            this.sizeOfIcon = 36;
        }else{
            this.nbCases = 484;
            this.size = 22;
            this.nbBombes=99;
            this.sizeOfIcon = 26;
        }
    }

    /**
     * Initialiser les cellules du model
     */
    public void initCells(){
        for(int i = 0; i < this.size; i++){
            for(int j = 0; j < this.size; j++){
                this.cells[i][j] = new Cell();
            }
        }
    }

    /**
     * Récupérer le niveau actuel
     * @return le niveau actuel
     */
    public int getLevel() { return this.level; }

    /**
     * Définir le niveau niveau
     * @param level nouveau
     */
    public void setLevel(int level) {this.level = level;}

    /**
     * Calculer le nombre de bombe autour d'un case
     */
    public void calculNBBombesAutour(){
        for(int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                int nbBombeAutour = 0;
                for (int k = i - 1; k <= i + 1; k++) {
                    for (int l = j - 1; l <= j + 1; l++) {
                        if (k >= 0 && l >= 0 && k < this.size && l < this.size && (k != i || l != j)){
                            if(this.cells[k][l].isBomb()){
                                nbBombeAutour++;
                            }
                        }
                    }
                }
                this.cells[i][j].setValue(nbBombeAutour);
            }
        }
    }

    /**
     * Calculer le nombre de drapeau autour d'une case
     * @param i la ligne dans laquelle la case est
     * @param j la colonne dans laquelle la case est
     * @return int le nombre de drapeau autour de la case
     */
    public int calculNbFlagautour(int i,int j){
        int nbFlagAutour = 0;
        for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
                if (k >= 0 && l >= 0 && k < this.size && l < this.size && (k != i || l != j)){
                    if(this.cells[k][l].hasFlag()){
                        nbFlagAutour++;
                            }
                        }
                    }
                }
        return nbFlagAutour;
    }

    /**
     * Récupérer la taille de la grille
     * @return nombre de lignes ou colonnes
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Récupérer la taille d'un icône
     * @return la taille des icônes
     */
    public int getSizeOfIcon() {
        return this.sizeOfIcon;
    }

    /**
     * Récupérer une case
     * @param i ligne dans laquelle la case est
     * @param j colonne dans laquelle la case est
     * @return la case en question
     */
    public Cell getCell(int i, int j) {
        return this.cells[i][j];
    }


    /**
     * Augmenter le nombre de bombes trouvées
     * @param cell la case sur laquelle est la bombe
     */
    public void incrementeNbBombeTrouve(Cell cell){
        if(this.listBombs.contains(cell)) {
            this.nbBombeTrouve++;
            this.listBombs.remove(cell);
        }
    }

    /**
     * Calculer le score final de la partie avec coefficient multiplicateur selon le level
     */
    public void calculerScoreFinal() {
        if (this.level == 1) {
            this.scoreFinal = (this.score + 1) * (1.0 / this.time) * 100;
        } else if (this.level == 2) {
            this.scoreFinal = ((this.score + 1) * (1.0 / this.time) * 100) * 5;
        } else if (this.level == 3) {
            this.scoreFinal = ((this.score + 1) * (1.0 / this.time) * 100)*15;
        }
    }

    /**
     * Obtenir le score final
     * @return le score final
     */
    public double getScoreFinal(){
        return this.scoreFinal;
    }

    /**
     * Ajouter des points au score de la partie en cours
     * @param point le nombre de points à ajouter
     */
    public void ajouteScore(int point){ this.score+=point; }

    /**
     * Récupérer le score actuel
     * @return le score actuel
     */
    public int getScore(){return this.score;}

    /**
     * Augmenter le nombre de drapeau
     */
    public void incrementeNbFlag(){ this.nbFlagPose += 1; }

    /**
     * Diminuer le nombre de drapeau
     */
    public void decrementeNbFlag(){
        this.nbFlagPose -= 1;
    }

    /**
     * Récupérer le nombre de drapeau posé
     * @return le nombre de drapeau posé
     */
    public int getNbFlagPose() {
        return nbFlagPose;
    }

    /**
     * Augmenter le nombre de secondes
     */
    public void incrementeTime() {
        this.time++;
    }

    /**
     * Retourner le timer sous la forme de hh:mm:ss
     * @return String temps formaté
     */
    public String formatTime() {
        int minutes = this.time/60;
        int seconds = this.time%60;


        StringBuilder builder = new StringBuilder();

        if (minutes < 10)
            builder.append('0');

        builder.append(minutes);
        builder.append("min ");

        if(seconds < 10)
            builder.append('0');

        builder.append(seconds);
        builder.append('s');

        return builder.toString();
    }

    /**
     * Jouer un son provenant d'un fichier
     * @param file fichier à jouer
     */
    public void jouerSon(File file) {
        try {
            URL url = new URL("file:" + file);
            AudioClip ac = Applet.newAudioClip(url);
            ac.play();

        } catch(MalformedURLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Jouer un son d'explosion
     */
    public void joueExplosion(){
        File file = new File("res/sound/bomb3.wav");
        this.jouerSon(file);

    }

    /**
     * Jouer un son de victoire
     */
    public void joueVictoire(){
        File file=new File("res/sound/sonVictoire.wav");
        this.jouerSon(file);
    }

    /**
     * Jouer un son d'erreur
     */
    public void joueErreur(){
        File file= new File("res/sound/erreur.wav");
        this.jouerSon(file);
    }

    /**
     * Jouer un son de clic
     */
    public void joueClique(){
        File file = new File("res/sound/click.wav");
        this.jouerSon(file);
    }

    /**
     * Jouer un son de placement de drapeau
     */
    public void joueDrapeau() {
        File file = new File("res/sound/flag2.wav");
        this.jouerSon(file);
    }

    /**
     * Jouer un son de défaite
     */
    public void joueDefaite(){
        File file=new File("res/sound/defaite.wav");
        this.jouerSon(file);
    }

    /**
     * Définir la vue
     * @param view la vue
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Ajouter un timer
     */
    public void addTimer() {
        this.timer = new Timer(1000, new TimerListener(this, this.view));
        this.timer.start();
    }

    /**
     * Terminer la partie lorsque celle ci est gagné, demande un pseudo au joueur et l'ajoute avec son score dans le fichier scores.txt
     */
    public void gameOver(){
        if(this.score==this.nbCases-this.nbBombes){
            for(int i=0;i<this.size;i++){
                for(int j=0;j<this.size;j++) {
                    if (this.cells[i][j].isBomb() && !this.cells[i][j].hasFlag()){
                        this.view.setDrapeau(i,j);
                    }
                }
            }
            this.partieFinie=true;
            int musique=0;
            this.timer.stop();
            if (this.view.getBackgroundMusic().isPlay()) {
                this.view.getBackgroundMusic().change();
                musique=1;
            }
            joueVictoire();
            calculerScoreFinal();
            String pseudo=JOptionPane.showInputDialog(null, "Score final : "+(int)(this.scoreFinal) +" \nVeuillez entrer votre pseudo : ","Anonymous");
            if(pseudo==null){
                pseudo="";
            }
            boolean valide = Pattern.matches("[a-zA-Z0-9]*", pseudo) && pseudo.length() <= 10;
            while (!valide) {
                pseudo = JOptionPane.showInputDialog(null, "Score final : " + (int) (this.scoreFinal) + " \nVotre pseudo ne doit pas contenir d'espace ni depasser 10 caractères : ","Anonymous");
                if(pseudo==null){
                    pseudo="";
                }
                valide = Pattern.matches("[a-zA-Z0-9]*", pseudo) && pseudo.length()<=10;
            }
            if(pseudo.equals("")){
                pseudo="Anonymous";
            }
            Score nouveauScore = new Score(pseudo,this.scoreFinal);
            this.classementScore(nouveauScore);
            WriteScoreToFile(highscore);

            int choice = JOptionPane.showConfirmDialog(this.view, "Lancer une nouvelle partie ?", "Partie terminée", JOptionPane.YES_NO_OPTION);
            if(musique==1) {
                this.view.getBackgroundMusic().change();
            }
            if(choice == JOptionPane.YES_OPTION) {
                int level = this.getLevel();
                Model model = new Model(level);
                this.view.dispose();
                ControlGroup controlGroup = new ControlGroup(model,this.view.getBackgroundMusic());
            }
        }
    }

    /**
     * Déterminer si la partie est finie
     * @return True si la partie est finie
     */
    public boolean getPartieFinie(){
        return this.partieFinie;
    }

    /**
     * Définir si la partie est finie
     * @param b true si la partie est finie, false sinon
     */
    public void setPartieFinie(boolean b){
        this.partieFinie = b;
    }

    /**
     * Ajouter le score d'un joueur au classement et trier les scores
     * @param scoreJoueur le score à ajouter
     * @return le classement des scores
     */
   public ArrayList<Score> classementScore(Score scoreJoueur){
       ArrayList<Score> scoreList = this.highscore;
       scoreList.add(scoreJoueur);
       Collections.sort(scoreList,new Comparator<Score>()
               {
                   public int compare(Score lhs,Score rhs)
                   {
                       return Integer.signum(rhs.getScore()-lhs.getScore());
                   }
               });
       if (scoreList.size()>5) scoreList.remove(5);
       return listScores;
    }


    /**
     * Récupérer la liste des scores
     * @return la liste des scores
     */
    public ArrayList<Score> getListScores(){ return this.listScores; }

    /**
     * Récupèrer la liste des scores depuis le fichier
     * @return la liste des scores
     */
    public ArrayList<Score> getScoreFromFile() {
        ObjectInputStream ois;
        ArrayList<Score> tabScore = new ArrayList<Score>(5);
        File fichier = new File("res/scores.txt");
        if (fichier.isFile() && fichier.length() > 0) {
            try {
                ois = new ObjectInputStream(
                        new BufferedInputStream(
                                new FileInputStream(
                                        new File("res/scores.txt"))));

                try {
                    Score tscore;

                    try {
                        tscore = (Score) ois.readObject();
                    } catch (EOFException e) {
                        tscore = null;
                    }

                    while (tscore != null) {
                        tabScore.add(tscore);
                        try {
                            tscore = (Score) ois.readObject();
                        } catch (EOFException e) {
                            tscore = null;
                        }


                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                ois.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tabScore;
    }

    /**
     * Écrire les scores dans le fichier
     * @param scoreList la liste des scores
     */
    public void WriteScoreToFile(ArrayList<Score> scoreList){
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(
                                    new File("res/scores.txt"))));
            for (Score score : scoreList) {
                oos.writeObject(score);
            }
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Diminuer le nombre de bombes trouvées et rajoute la cellule dans la liste des bombes
     * @param cell :a bombe sur laquelle on a enlevé le drapeau
     */
    public void decrementeNbBombeTrouve(Cell cell) {
        this.listBombs.add(cell);
        this.nbBombeTrouve--;
    }

    /**
     * Récupérer les meilleurs scores
     * @return la liste des meilleures scores
     */
    public ArrayList<Score> getHighscore(){
        return this.highscore;
    }
}
