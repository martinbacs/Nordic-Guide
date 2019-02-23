package nordicguide;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by marbac on 4/13/2017.
 */
public class MainPageController extends Controller implements Initializable {
    @FXML
    private TextField textFieldDestination, textFieldSearchAttraction;

    @FXML
    private Button backBtn, logoutBtn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (getAccount() == null) {
            logoutBtn.setVisible(false);
            backBtn.setVisible(true);
        } else {
            while (!searchHistory.isEmpty()) {
                searchHistory.pop();
            }
            while (!sceneHistory.isEmpty()) {
                sceneHistory.pop();
            }
        }
    }


    public void handleSearchButton(MouseEvent me) throws IOException {
        sceneHistory.push("../fxml/MainPage.fxml");
        if (!textFieldDestination.getText().isEmpty() && textFieldSearchAttraction.getText().isEmpty()) {
            searchHistory.push(textFieldDestination.getText());
            super.switchToSearchResultsScene(me, db.searchForRegion(textFieldDestination.getText()));
        }
        if (textFieldDestination.getText().isEmpty() && textFieldSearchAttraction.getText().isEmpty()) {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("NordicGuide");
            dialog.setHeaderText("ERROR");
            dialog.setContentText("Please enter a destination or attraction.");
            dialog.showAndWait();
        }
        if (textFieldDestination.getText().isEmpty() && !textFieldSearchAttraction.getText().isEmpty()) {
            searchHistory.push(textFieldSearchAttraction.getText());
            super.switchToSearchResultsScene(me, db.searchForAttractions(textFieldSearchAttraction.getText()));
        }
        if (!textFieldDestination.getText().isEmpty() && !textFieldSearchAttraction.getText().isEmpty()) {
            multiSearchHistory.push(textFieldDestination.getText());
            multiSearchHistory.push(textFieldSearchAttraction.getText());
            super.switchToSearchResultsScene(me, db.searchForAttractionsInRegion(textFieldDestination.getText(), textFieldSearchAttraction.getText()));
        }

    }

    public void handleViewAllCountriesButton(MouseEvent me) throws IOException {
        sceneHistory.push("../fxml/MainPage.fxml");
        super.switchToCountries(me, super.db.getAllCountries());
    }

    public void logout(MouseEvent me) throws IOException {
        super.setAccount(null);
        switchScene(me, "../fxml/StartPage.fxml");
    }

    public void handleHelpButton() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Instructions");
        dialog.setContentText("Please enter an attraction and a destination in the fields. " +
                "Either a country or a city in the Nordic area." +
                " \n \nIt is possible to search for only an attraction or a destination.");
        dialog.showAndWait();
    }

}
