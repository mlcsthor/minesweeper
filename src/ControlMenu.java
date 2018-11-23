import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by thorx on 18/05/2017.
 */
public class ControlMenu implements ActionListener {
    private View view;
    private Model model;
    private BackgroundMusic backgroundMusic;

    /**
     * Créer un controller pour le menu
     * @param view la vue
     * @param model le model
     * @param backgroundMusic la musique de fond
     */
    public ControlMenu(View view, Model model, BackgroundMusic backgroundMusic) {
        this.view = view;
        this.model = model;
        this.backgroundMusic = backgroundMusic;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == view.mQuit){
            System.exit(0);
        }else if(e.getSource() == view.mNDebutant){
            this.view.dispose();
            this.model = new Model(1);
            ControlGroup controlGroup = new ControlGroup(model,backgroundMusic);
        }else if(e.getSource() == view.mNNormal){
            this.view.dispose();
            this.model = new Model(2);
            ControlGroup controlGroup = new ControlGroup(model,backgroundMusic);
        }else if(e.getSource() == view.mNExpert){
            this.view.dispose();
            this.model = new Model(3);
            ControlGroup controlGroup = new ControlGroup(model,backgroundMusic);
        }else if(e.getSource() == view.mAide){
            this.view.afficheAides();
        }else{
            this.view.afficheAPropos();
        }
    }
}
