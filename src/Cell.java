/**
 * Created by corentin on 22/05/2017.
 */
public class Cell {
    private boolean bomb;
    private boolean flag;
    private boolean discovered;
    private boolean interrogation;
    private int value; // nombre de bombe autour

    /**
     * Créer une nouvelle cellule
     */
    public Cell(){
        this.bomb = false;
        this.flag = false;
        this.value = 0;
        this.discovered = false;
    }

    /**
     * Placer ou retirer un drapeau sur la case
     * @param flag : True si on place un drapeau sur la case, false sinon
     */
    public void setFlag(boolean flag){ this.flag =flag; }

    /**
     * Déterminer si un drapeau est placé sur la case
     * @return True si un drapeau est sur la case, false sinon
     */
    public boolean hasFlag(){ return this.flag; }

    /**
     * Placer une bombe sur la case
     */
    public void setBomb(){ this.bomb = true; }

    /**
     * Déterminer si une bombe est placée sur la case
     * @return True s'il y a une bombe, false sinon
     */
    public boolean isBomb(){ return this.bomb; }


    /**
     * Définir le nombre de bombes autour de la case
     * @param x : le nombre de bombes autour de la case
     */
    public void setValue(int x){this.value=x;}

    /**
     * Récupérer le nombre de bombes autour de la case
     * @return le nombre de bombe autour de la case
     */
    public int getValue(){return this.value;}

    /**
     * Déterminer si la case est découverte
     * @return True si la case est découverte, false sinon
     */
    public boolean isDiscovered() {
        return this.discovered;
    }

    /**
     * Placer ou retirer un point d'interrogation sur la case
     * @param interro : True pour placer un point d'interrogation, false sinon
     */
    public void setInterrogation(boolean interro){ this.interrogation = interro; }

    /**
     * Déterminer si un point d'interrogation est sur la case
     * @return True s'il y a un point d'interrogation, false sinon
     */
    public boolean hasInterrogation(){ return this.interrogation; }

    /**
     * Découvrir une case
     */
    public void setDiscovered() { this.discovered = true; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (bomb != cell.bomb) return false;
        if (flag != cell.flag) return false;
        if (discovered != cell.discovered) return false;
        if (interrogation != cell.interrogation) return false;
        return value == cell.value;
    }
}
