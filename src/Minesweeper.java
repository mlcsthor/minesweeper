/**
 * Created by thorx on 18/05/2017.
 */
public class Minesweeper {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BackgroundMusic backgroundMusic = new BackgroundMusic();
                ViewMenu viewMenu = new ViewMenu(backgroundMusic);
                ControlButtonMenu controlButtonMenu = new ControlButtonMenu(viewMenu,backgroundMusic);
            }
        });
    }
}