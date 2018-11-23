import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by tbichot on 21/05/2017.
 */
public class ControlButtonMenu implements ActionListener {
    private ViewMenu viewMenu;
    private BackgroundMusic backgroundMusic;

    /**
     * Créer un controller pour les boutons du menu
     * @param viewMenu la vue
     * @param backgroundMusic la musique de fond
     */
    public ControlButtonMenu(ViewMenu viewMenu, BackgroundMusic backgroundMusic) {
        this.viewMenu = viewMenu;
        this.backgroundMusic = backgroundMusic;
        viewMenu.setButtonControler(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewMenu.getQuitter()) {
            System.exit(0);
        } else if(e.getSource() == viewMenu.getaPropos()) {
            viewMenu.afficheAPropos();
        }else if(e.getSource() == viewMenu.getPlayMusic()){
            if(backgroundMusic.change()){
                viewMenu.getPlayMusic().setIcon(viewMenu.setButtonIcon("play",50,50));
            }else{
                viewMenu.getPlayMusic().setIcon(viewMenu.setButtonIcon("stop",50,50));
            }

        }
        else{
            int level = 0;
            if (e.getSource() == viewMenu.getDebutant()) {
                level = 1;
            } else if (e.getSource() == viewMenu.getNormal()) {
                level = 2;
            } else if (e.getSource() == viewMenu.getExpert()) {
                level = 3;
            }
            this.viewMenu.undisplay();
            Model model = new Model(level);
            ControlGroup controlGroup = new ControlGroup(model,backgroundMusic);
        }
    }
}
