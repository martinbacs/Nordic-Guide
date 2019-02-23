package nordicguide;

import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by marbac on 4/13/2017.
 */
public class StartPageController extends Controller implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void switchToLoginScene(MouseEvent me) throws IOException {
        super.switchScene(me, "../fxml/LogInMenu.fxml");
        sceneHistory.push("../fxml/StartPage.fxml");
    }

    public void switchToRegistrationScene(MouseEvent me) throws IOException {
        super.switchScene(me, "../fxml/RegistrationMenu.fxml");
        sceneHistory.push("../fxml/StartPage.fxml");
    }

    public void switchToMainPageScene(MouseEvent me) throws IOException {
        super.switchScene(me, "../fxml/MainPage.fxml");
        sceneHistory.push("../fxml/StartPage.fxml");
    }

}
