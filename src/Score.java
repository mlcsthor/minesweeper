/**
 * Created by tgarceno on 29/05/17.
 */
import java.io.Serializable;

public class Score implements Serializable {
    private String pseudo;
    private double score;
    java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");

    /**
     * Créér une entité score servant a être sauvegardée dans un fichier
     * @param pseudo le pseudo du joueur
     * @param score le score du joueur
     */
    public Score(String pseudo, double score) {
        this.pseudo = pseudo;
        this.score = score;
    }

    /**
     * Récupérer le score
     * @return le score
     */
    public int getScore() {
        return (int)score;
    }

    /**
     * Récupérer le pseudo
     * @return le pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    public String toString(){
        return (this.pseudo + " " + df.format(this.score));
    }
}
