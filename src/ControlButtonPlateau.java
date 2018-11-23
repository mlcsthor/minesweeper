/**
 * Created by tbichot on 01/06/2017.
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by tbichot on 21/05/2017.
 */
public class ControlButtonPlateau implements ActionListener {
    private View view;
    private Model model;
    private BackgroundMusic backgroundMusic;

    /**
     * Créer une controller pour les boutons du jeu
     * @param view la vue
     * @param model le model
     * @param backgroundMusic la musique de fond
     */
    public ControlButtonPlateau(View view, Model model, BackgroundMusic backgroundMusic) {
        this.view = view;
        this.model = model;
        this.backgroundMusic = backgroundMusic;
        view.setButtonControler(this);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == view.getHome()){
            this.view.dispose();
            ViewMenu viewMenu = new ViewMenu(backgroundMusic);
            ControlButtonMenu controlButtonMenu = new ControlButtonMenu(viewMenu, backgroundMusic);
        }else if(e.getSource() == view.getAides()){
            this.view.afficheAides();
        }else if(e.getSource() == view.getPlayMusic()){
                if(backgroundMusic.change()){
                    view.getPlayMusic().setIcon(view.setIconButton("play",false,50,50));
                }else{
                    view.getPlayMusic().setIcon(view.setIconButton("stop",false,50,50));
                }
        }
        else{
            int level = this.model.getLevel();
            this.model = new Model(level);
            this.view.dispose();
            ControlGroup controlGroup = new ControlGroup(model,backgroundMusic);
        }

    }
}

