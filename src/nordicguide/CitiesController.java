package nordicguide;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

/**
 * Created by marbac on 4/12/2017.
 */
public class CitiesController extends Controller implements Initializable, Sort {

    @FXML
    private ListView<String> list;

    @FXML
    private Label countryNameLabel;

    @FXML
    private ComboBox comboBoxSortBy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> olist = FXCollections.observableArrayList();
        olist.add("Alphabetical order");
        comboBoxSortBy.setItems(olist);
    }

    public void setCitiesResult(ArrayList<String> results) {
        ObservableList<String> olist = FXCollections.observableArrayList();
        for (int i = 0; i < results.size(); i++) {
            olist.add(results.get(i));
        }
        list.setItems(olist);
        countryNameLabel.setText(searchHistory.peek());
    }

    public void handleMouseClick(MouseEvent me) throws IOException {
        if (list.getSelectionModel().getSelectedItem() != null) {
            sceneHistory.push("../fxml/Cities.fxml");
            searchHistory.push(list.getSelectionModel().getSelectedItem());
            super.switchToCityScene(me, super.db.getAllHotelsInCity(list.getSelectionModel().getSelectedItem()),
                    super.db.getAllRestaurantsInCity(list.getSelectionModel().getSelectedItem()),
                    super.db.getAllMuseumsInCity(list.getSelectionModel().getSelectedItem()),
                    super.db.getAllSightsInCity(list.getSelectionModel().getSelectedItem()));

        }
    }

    public void handleHelpButton() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Instructions");
        dialog.setContentText("Please select a city from the list to continue to the next page where you " +
                "can see all the attractions from the selected city.");
        dialog.showAndWait();
    }

    public void sort() {
        switch (comboBoxSortBy.getValue().toString()) {
            case ("Alphabetical order"):
                sortAlphabeticalOrder();
                break;
        }
    }

    @Override
    public void sortAlphabeticalOrder() {
        ObservableList<String> olist = list.getItems();
        olist.sort(Comparator.<String>naturalOrder());
        list.setItems(olist);
    }
}
