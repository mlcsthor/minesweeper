import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by thorx on 29/05/2017.
 */
public class TimerListener implements ActionListener {
    private Model model;
    private View view;

    /**
     * Créer un listener pour le timer
     * @param model le model
     * @param view la vue
     */
    public TimerListener(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void actionPerformed(ActionEvent e) {
        this.model.incrementeTime();
        this.view.getJltime().setText(this.model.formatTime());
    }
}
