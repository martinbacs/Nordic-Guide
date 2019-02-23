package nordicguide;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by marbac on 4/12/2017.
 */
public class CountriesController extends Controller implements Initializable {

    @FXML
    private ListView<String> list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setCountriesResult(ArrayList<String> results) {
        ObservableList<String> olist = FXCollections.observableArrayList();
        for (int i = 0; i < results.size(); i++) {
            olist.add(results.get(i));
        }
        list.setItems(olist);
    }

    public void handleMouseClick(MouseEvent me) throws IOException {
        if (list.getSelectionModel().getSelectedItem() != null) {
            sceneHistory.push("../fxml/Countries.fxml");
            searchHistory.push(list.getSelectionModel().getSelectedItem());
            super.switchToCitiesScene(me, super.db.getAllCitiesInCountry(list.getSelectionModel().getSelectedItem()));
        }
    }

    public void handleHelpButton() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Instructions");
        dialog.setContentText("Please select a country from the list to move onto the next page " +
                "where you can see all the cities from the selected country.");
        dialog.showAndWait();
    }

}
