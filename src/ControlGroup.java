/**
 * Created by thorx on 18/05/2017.
 */
public class ControlGroup {
    private Model model;
    protected View view;
    private ControlMenu controlMenu;
    private ControlMouse controlMouse;
    private ControlButtonPlateau controlButtonPlateau;

    /**
     * Créer un groupe de controllers
     * @param model le model
     * @param backgroundMusic la musique de fond
     */
    public ControlGroup(Model model, BackgroundMusic backgroundMusic) {
        this.model = model;
        this.view = new View(this.model, backgroundMusic);
        this.model.setView(this.view);
        this.model.addTimer();

        this.controlMouse = new ControlMouse(this.view, this.model, backgroundMusic);
        this.controlMenu = new ControlMenu(this.view, this.model, backgroundMusic);
        this.controlButtonPlateau = new ControlButtonPlateau(this.view, this.model, backgroundMusic);
        this.view.display();
    }
}
