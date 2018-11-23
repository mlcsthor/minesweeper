import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by thorx on 18/05/2017.
 */
public class ControlMouse extends MouseAdapter {
    private View view;
    private Model model;
    private BackgroundMusic backgroundMusic;
    int i,j;
    private Point oldPoint;

    /**
     * Créer un controller pour la souris
     * @param view la vue
     * @param model le model
     * @param backgroundMusic la musique de fond
     */
    public ControlMouse(View view, Model model, BackgroundMusic backgroundMusic) {
        this.view = view;
        this.model = model;
        this.backgroundMusic = backgroundMusic;
        this.view.setMouseListener(this);
        this.i = -1;
        this.j = -1;
    }

    public void mouseClicked(MouseEvent e) {
        int button = e.getButton();
        Point point = e.getPoint();
        int[] coordonnees = detectionCase(point);
        int i = coordonnees[0];
        int j = coordonnees[1];
        if (!this.model.getPartieFinie()) {
            if (i != -1 && j != -1) {
                if (button == MouseEvent.BUTTON1) {

                    /// DOUBLE CLIQUE ///
                    if (e.getClickCount() == 2) {
                        if (this.model.getCell(i, j).isDiscovered() && this.model.getCell(i, j).getValue() == this.model.calculNbFlagautour(i, j)) {
                            int nbCaseDecouverte = 0;
                            boucle:
                            for (int k = i - 1; k <= i + 1; k++) {
                                for (int l = j - 1; l <= j + 1; l++) {
                                    if (k >= 0 && l >= 0 && k < this.model.getSize() && l < this.model.getSize() && (k != i || l != j)) {
                                        if (!this.model.getCell(k, l).hasFlag() && !this.model.getCell(k, l).isBomb() && !this.model.getCell(k, l).isDiscovered()) {
                                            if (this.model.getCell(k, l).getValue() == 0) {
                                                this.view.caseP[k][l].setIcon(null);
                                                propagation(k, l);
                                                this.model.ajouteScore(-1);
                                            } else {
                                                this.view.setNombreBombes(k, l, this.model.getCell(k, l).getValue());
                                            }
                                            this.model.getCell(k, l).setDiscovered();
                                            this.model.ajouteScore(1);
                                            nbCaseDecouverte++;
                                        } else if (this.model.getCell(k, l).hasFlag()) {
                                        } else if (this.model.getCell(k, l).isBomb()) {
                                            declencheBombe(k, l);
                                            break boucle;
                                        }
                                    }
                                }
                            }
                            if (this.model.getCell(i, j).getValue() != 0 && nbCaseDecouverte != 0) {
                                this.model.joueClique();
                            }
                        } else if (!this.model.getCell(i,j).isDiscovered()){}
                        else {
                            this.model.joueErreur();
                        }
                    }

                    /// CLIQUE SIMPLE ///

                    // Pose de point d'interrogation si drapeau sur la case
                    else if (!this.model.getCell(i, j).isDiscovered() && this.model.getCell(i, j).hasFlag()) {
                        this.view.setHoverIconeInterrogation(i, j);
                        this.model.decrementeNbFlag();
                        this.model.getCell(i, j).setFlag(false);
                        this.model.getCell(i, j).setInterrogation(true);
                        if (this.model.getCell(i, j).isBomb())
                            this.model.decrementeNbBombeTrouve(this.model.getCell(i, j));
                    }

                    // Pose de drapeau si point d'interrogation sur la case
                    else if (!this.model.getCell(i, j).isDiscovered() && this.model.getCell(i, j).hasInterrogation()) {
                        this.view.setHoverIconDrapeau(i, j);
                        this.model.incrementeNbFlag();
                        this.model.getCell(i, j).setFlag(true);
                        this.model.getCell(i, j).setInterrogation(false);
                        if (this.model.getCell(i, j).isBomb())
                            this.model.incrementeNbBombeTrouve(this.model.getCell(i, j));
                    }

                    // Découvre la case
                    else {
                        if (!this.model.getCell(i, j).hasFlag() && !this.model.getCell(i, j).isDiscovered()) {
                            this.view.caseP[i][j].setIcon(null);
                            this.model.getCell(i, j).setDiscovered();
                            if (this.model.getCell(i, j).isBomb()) {
                                declencheBombe(i, j);
                            } else {
                                this.model.joueClique();

                                if (this.model.getCell(i, j).getValue() == 0) {
                                    propagation(i, j);
                                } else {
                                    this.view.setNombreBombes(i, j, this.model.getCell(i, j).getValue());
                                }
                                this.model.ajouteScore(1);
                            }
                        }
                    }
                }
                /// CLIC DROIT ///
                else if (button == MouseEvent.BUTTON3) {
                    if (!this.model.getCell(i, j).isDiscovered()) {

                        // Pose un drapeau sur la case
                        if (!this.model.getCell(i, j).hasFlag() && !this.model.getCell(i, j).hasInterrogation()) {
                            if (this.model.getNbFlagPose() < this.model.getNbBombes()) {
                                this.model.joueDrapeau();
                                this.view.setHoverIconDrapeau(i, j);
                                this.model.getCell(i, j).setFlag(true);
                                this.model.incrementeNbFlag();

                                if (this.model.getCell(i, j).isBomb())
                                    this.model.incrementeNbBombeTrouve(this.model.getCell(i, j));
                            }
                        }

                        // Enlève point d'interrogation
                        else if (this.model.getCell(i, j).hasInterrogation()) {
                            this.view.setNormalIcon(i, j);
                            this.model.getCell(i, j).setInterrogation(false);
                        }

                        // Enlève drapeau
                        else {
                            this.view.setNormalIcon(i, j);
                            this.model.getCell(i, j).setFlag(false);
                            this.model.decrementeNbFlag();
                            if (this.model.getCell(i, j).isBomb())
                                this.model.decrementeNbBombeTrouve(this.model.getCell(i, j));
                        }
                    }
                }
            }
            this.view.jlScore.setText(Integer.toString(this.model.getScore()));
            this.view.jlNbDrapeaux.setText(Integer.toString(this.model.getNbBombes() - this.model.getNbFlagPose()));
            this.model.gameOver();
        }
    }



    public void mouseExited(MouseEvent e) {
        if (oldPoint != null){
            int[] coordonnees2 = detectionCase(oldPoint);
            int i2 = coordonnees2[0];
            int j2 = coordonnees2[1];
            if (!this.model.getCell(i2, j2).isDiscovered() && !this.model.getCell(i2, j2).hasFlag() && !this.model.getCell(i2,j2).hasInterrogation()) {
                this.view.setNormalIcon(i2, j2);
            } else if (!this.model.getCell(i2, j2).isDiscovered() && this.model.getCell(i2, j2).hasFlag() && !this.model.getCell(i2,j2).hasInterrogation()) {
                this.view.setDrapeau(i2, j2);
            } else if(!this.model.getCell(i2, j2).isDiscovered() && !this.model.getCell(i2, j2).hasFlag() && this.model.getCell(i2,j2).hasInterrogation()){
                this.view.setInterrogation(i2,j2);
            }
        }
    }

    public void mouseMoved(MouseEvent e){
        Point point = e.getPoint();
        int[] coordonnees = detectionCase(point);
        int i = coordonnees[0];
        int j = coordonnees[1];
        int i2 = -1;
        int j2 = -1;
        if (oldPoint != null){
            int[] coordonnees2 = detectionCase(oldPoint);
            i2 = coordonnees2[0];
            j2 = coordonnees2[1];
        }
        if (point != oldPoint ){
            if(i2 != -1 && j2 != -1) {
                if (!this.model.getCell(i2, j2).isDiscovered()&& !this.model.getCell(i2,j2).hasFlag() && !this.model.getCell(i2,j2).hasInterrogation()) {
                    this.view.setNormalIcon(i2, j2);
                }
                else if (!this.model.getCell(i2, j2).isDiscovered() && this.model.getCell(i2,j2).hasFlag() && !this.model.getCell(i2,j2).hasInterrogation()){
                    this.view.setDrapeau(i2, j2);
                }
                else if(!this.model.getCell(i2, j2).isDiscovered() && this.model.getCell(i2,j2).hasInterrogation() && !this.model.getCell(i2, j2).hasFlag()){
                    this.view.setInterrogation(i2,j2);
                }
            }
        }
        if(i != -1 && j != -1){
            if(!this.model.getCell(i,j).isDiscovered() && !this.model.getCell(i,j).hasFlag() && !this.model.getCell(i,j).hasInterrogation()){
                this.view.setHoverIcon(i,j);
                this.oldPoint =  e.getPoint();
            }
            else if (!this.model.getCell(i,j).isDiscovered() && this.model.getCell(i,j).hasFlag() && !this.model.getCell(i,j).hasInterrogation()){
                this.view.setHoverIconDrapeau(i,j);
                this.oldPoint =  e.getPoint();
            }
            else if (!this.model.getCell(i,j).isDiscovered() && !this.model.getCell(i,j).hasFlag() && this.model.getCell(i,j).hasInterrogation()){
                this.view.setHoverIconeInterrogation(i,j);
                this.oldPoint =  e.getPoint();
            }
        }
    }

    /**
     * Propager la découverte de case autour d'elle si sa valeur est égale à 0
     * @param i ligne sur laquelle la case cliquée est
     * @param j colonne sur laquelle la case cliquée est
     */
    public void propagation(int i, int j) {
        int point=0;
        for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
                if (k >= 0 && l >= 0 && k < this.model.getSize() && l < this.model.getSize() && !this.model.getCell(k,l).isDiscovered() && !this.model.getCell(k,l).hasFlag() && !this.model.getCell(k,l).isBomb()) {
                    this.view.caseP[k][l].setIcon(null);
                    this.model.getCell(k,l).setDiscovered();
                    point++;
                    if (this.model.getCell(k, l).getValue() == 0) {
                        propagation(k, l);
                    }else{
                        this.view.setNombreBombes(k,l,this.model.getCell(k, l).getValue());
                    }
                }
            }
        }
        this.model.ajouteScore(point);
    }

    /**
     * Détecter quelle case à été cliqué
     * @param point coordonnées du clic
     * @return les coordonnées de la case cliquée
     */
    public int[] detectionCase(Point point){
        int [] coordonnees = new int[2];
        coordonnees[0] = -1;
        coordonnees[1] = -1;
        for(int i = 0; i<this.model.getSize(); i++){
            for(int j=0; j<this.model.getSize(); j++){
                if( (point.getX() > view.caseP[i][j].getX()) && (point.getX() < (view.caseP[i][j].getX()+this.model.getSizeOfIcon())) && (point.getY() > view.caseP[i][j].getY()) && (point.getY() < (view.caseP[i][j].getY()+this.model.getSizeOfIcon()))){
                    coordonnees[0] = i;
                    coordonnees[1] = j;
                }
            }
        }
        return coordonnees;
    }

    /**
     * Découvrir toutes les bombes
     */
    public void decouvreBombe(){
        for (int k = 0; k < this.model.getSize(); k++) {
            for (int l = 0; l < this.model.getSize(); l++) {
                if (this.model.getCell(k, l).isBomb()) {
                    this.view.caseP[k][l].setIcon(null);
                    this.model.getCell(k, l).setDiscovered();
                    this.view.setBombe(k, l, "");
                }
            }
        }
    }

    /**
     * Arrêter la partie lors de la découverte d'une bombe
     * @param i ligne sur laquelle se trouve la bombe
     * @param j colonne sur laquelle se trouve la bombe
     */
    public void declencheBombe(int i,int j){

        this.model.getTimer().stop();
        this.model.joueDefaite();
        this.model.setPartieFinie(true);
        int musique =0;
        if(this.view.getBackgroundMusic().isPlay()) {
            this.view.getBackgroundMusic().change();
            musique =1;
        }
        decouvreBombe();
        this.view.setBombe(i, j, "r");
        this.model.joueExplosion();
        ImageIcon iconBomb = new ImageIcon("res/img/bombe" + model.getLevel() + ".png");
        Image image = iconBomb.getImage();
        Image newimg = image.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH); // resize it here
        iconBomb = new ImageIcon(newimg);
        int choice = JOptionPane.showConfirmDialog(this.view, "Lancer une nouvelle partie ?", "Partie terminée", JOptionPane.YES_NO_OPTION,0, iconBomb);
        if(musique==1){
            this.view.getBackgroundMusic().change();
        }
        if (choice == JOptionPane.OK_OPTION) {
            int level = this.model.getLevel();
            this.model = new Model(level);
            this.view.dispose();
            ControlGroup controlGroup = new ControlGroup(model,backgroundMusic);
        }
    }

    /** Méthodes inutiles mais nécessaires pour IDEA **/
    public void mouseEntered(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
}
