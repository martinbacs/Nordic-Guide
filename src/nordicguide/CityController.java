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
 * Created by lovisafinnbck on 2017-04-17.
 */
public class CityController extends Controller implements Initializable, Sort {

    @FXML
    private ListView<String> hotelsList, restaurantsList, sightsList, museumsList;

    @FXML
    private Label cityNameLabel;

    @FXML
    private ComboBox comboBoxSortBy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> olist = FXCollections.observableArrayList();
        olist.add("Alphabetical order");
        comboBoxSortBy.setItems(olist);
    }

    public void setCityResult(ArrayList<String> hotels, ArrayList<String> restaurants, ArrayList<String> museums, ArrayList<String> sights) {

        ObservableList<String> oHotelsList = FXCollections.observableArrayList();
        for (int i = 0; i < hotels.size(); i++) {
            oHotelsList.add(hotels.get(i));
        }
        hotelsList.setItems(oHotelsList);

        ObservableList<String> oRestaurantList = FXCollections.observableArrayList();
        for (int i = 0; i < restaurants.size(); i++) {
            oRestaurantList.add(restaurants.get(i));
        }
        restaurantsList.setItems(oRestaurantList);

        ObservableList<String> oMuseumsList = FXCollections.observableArrayList();
        for (int i = 0; i < museums.size(); i++) {
            oMuseumsList.add(museums.get(i));
        }
        museumsList.setItems(oMuseumsList);

        ObservableList<String> oSightsList = FXCollections.observableArrayList();
        for (int i = 0; i < sights.size(); i++) {
            oSightsList.add(sights.get(i));
        }
        sightsList.setItems(oSightsList);
        cityNameLabel.setText(searchHistory.peek());
    }

    public void handleMouseClick(MouseEvent me) throws IOException {
        if (hotelsList.getSelectionModel().getSelectedItem() != null || restaurantsList.getSelectionModel().getSelectedItem() != null ||
                museumsList.getSelectionModel().getSelectedItem() != null || sightsList.getSelectionModel().getSelectedItem() != null) {
            sceneHistory.push("../fxml/City.fxml");

            if (db.isARestaurant(restaurantsList.getSelectionModel().getSelectedItem())) {
                searchHistory.push(restaurantsList.getSelectionModel().getSelectedItem());
                super.switchToAttractionsScene(me, db.getRestaurant(restaurantsList.getSelectionModel().getSelectedItem()), "../fxml/AttractionPage.fxml");
            }
            if (db.isAHotel(hotelsList.getSelectionModel().getSelectedItem())) {
                searchHistory.push(hotelsList.getSelectionModel().getSelectedItem());
                super.switchToAttractionsScene(me, db.getHotel(hotelsList.getSelectionModel().getSelectedItem()), "../fxml/AttractionPage.fxml");
            }
            if (db.isAMuseum(museumsList.getSelectionModel().getSelectedItem())) {
                searchHistory.push(museumsList.getSelectionModel().getSelectedItem());
                super.switchToAttractionsScene(me, db.getMuseum(museumsList.getSelectionModel().getSelectedItem()), "../fxml/AttractionPage.fxml");
            }
            if (db.isASight(sightsList.getSelectionModel().getSelectedItem())) {
                searchHistory.push(sightsList.getSelectionModel().getSelectedItem());
                super.switchToAttractionsScene(me, db.getSight(sightsList.getSelectionModel().getSelectedItem()), "../fxml/AttractionPage.fxml");
            }
        }

    }

    public void handleHelpButton() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Instructions");
        dialog.setContentText("Please click on the attraction you want to see more information about.");
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
        ObservableList<String> hotelsSorted  = hotelsList.getItems();
        ObservableList<String> restaurantsSorted  = restaurantsList.getItems();
        ObservableList<String> museumsSorted  = museumsList.getItems();
        ObservableList<String> sightsSorted = sightsList.getItems();
        hotelsSorted.sort(Comparator.<String>naturalOrder());
        restaurantsSorted.sort(Comparator.<String>naturalOrder());
        museumsSorted.sort(Comparator.<String>naturalOrder());
        sightsSorted.sort(Comparator.<String>naturalOrder());
        hotelsList.setItems(hotelsSorted);
        restaurantsList.setItems(restaurantsSorted);
        museumsList.setItems(museumsSorted);
        sightsList.setItems(sightsSorted);
    }
}
