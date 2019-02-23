package nordicguide;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by marbac on 4/13/2017.
 */
public class SearchResultsController extends Controller implements Initializable {

    @FXML
    private ListView<String> list;
    @FXML
    private Label searchResultsLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!multiSearchHistory.isEmpty()) {
            searchResultsLabel.setText("Attraction: " + multiSearchHistory.firstElement() + "  Destination: " + multiSearchHistory.peek());
        } else {
            searchResultsLabel.setText(searchHistory.peek());
        }
    }

    public void setSearchResult(ArrayList<String> results) {
        ObservableList<String> olist = FXCollections.observableArrayList();
        for (int i = 0; i < results.size(); i++) {
            olist.add(results.get(i));
        }
        list.setItems(olist);
    }

    public void handleMouseClick(MouseEvent me) throws IOException {
        if (list.getSelectionModel().getSelectedItem() != null) {
            searchHistory.push(list.getSelectionModel().getSelectedItem());
            sceneHistory.push("../fxml/SearchResults.fxml");
            if (super.db.isACity(list.getSelectionModel().getSelectedItem())) {
                super.switchToCityScene(me, super.db.getAllHotelsInCity(list.getSelectionModel().getSelectedItem()),
                        super.db.getAllRestaurantsInCity(list.getSelectionModel().getSelectedItem()),
                        super.db.getAllMuseumsInCity(list.getSelectionModel().getSelectedItem()),
                        super.db.getAllSightsInCity(list.getSelectionModel().getSelectedItem()));
            }
            if (super.db.isACountry(list.getSelectionModel().getSelectedItem())) {
                super.switchToSearchResultsScene(me, super.db.getAllCitiesInCountry(list.getSelectionModel().getSelectedItem()));
            }
            if (db.isARestaurant(list.getSelectionModel().getSelectedItem())) {
                super.switchToAttractionsScene(me, db.getRestaurant(list.getSelectionModel().getSelectedItem()), "../fxml/AttractionPage.fxml");
            }
            if (super.db.isAHotel(list.getSelectionModel().getSelectedItem())) {
                super.switchToAttractionsScene(me, db.getHotel(list.getSelectionModel().getSelectedItem()), "../fxml/AttractionPage.fxml");
            }
            if (super.db.isAMuseum(list.getSelectionModel().getSelectedItem())) {
                super.switchToAttractionsScene(me, db.getMuseum(list.getSelectionModel().getSelectedItem()), "../fxml/AttractionPage.fxml");
            }
            if (super.db.isASight(list.getSelectionModel().getSelectedItem())) {
                super.switchToAttractionsScene(me, db.getSight(list.getSelectionModel().getSelectedItem()), "../fxml/AttractionPage.fxml");
            }
        }
    }

    public void handleHelpButton() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Instructions");
        dialog.setContentText("Please click on the search result in the list to continue to the next page." +
                "\n \nIf the attraction or destination you have searched for is not shown, please use the back button" +
                " to make a new more specific search.");
        dialog.showAndWait();
    }
}



