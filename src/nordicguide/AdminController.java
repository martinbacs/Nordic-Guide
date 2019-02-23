package nordicguide;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Vilhelm on 2017-05-02.
 */
public class AdminController extends Controller implements Initializable {
    @FXML
    private ListView<String> hotelsList, restaurantsList, museumsList;
    @FXML
    private ComboBox comboBoxCountries, comboBoxCities;
    @FXML
    private TextField tfSightName, tfSightAddress, tfUsername;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> oCountriesList = FXCollections.observableArrayList(super.db.getAllCountries());
        comboBoxCountries.setItems(oCountriesList);
    }

    public void setUnVerifiedAttractions(ArrayList<String> hotels, ArrayList<String> restaurants, ArrayList<String> museums) {

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
    }

    public void handleMouseClick(MouseEvent me) throws IOException {
        if (hotelsList.getSelectionModel().getSelectedItem() != null || restaurantsList.getSelectionModel().getSelectedItem() != null ||
                museumsList.getSelectionModel().getSelectedItem() != null) {
            sceneHistory.push("../fxml/AdminScene.fxml");
            if (db.isARestaurant(restaurantsList.getSelectionModel().getSelectedItem())) {
                super.switchToAttractionsScene(me, db.getRestaurantAllColumns(restaurantsList.getSelectionModel().getSelectedItem()), "../fxml/UnverifiedAttractionScene.fxml");
            }
            if (db.isAHotel(hotelsList.getSelectionModel().getSelectedItem())) {
                super.switchToAttractionsScene(me, db.getHotelAllColumns(hotelsList.getSelectionModel().getSelectedItem()), "../fxml/UnverifiedAttractionScene.fxml");
            }
            if (db.isAMuseum(museumsList.getSelectionModel().getSelectedItem())) {
                super.switchToAttractionsScene(me, db.getMuseumAllColumns(museumsList.getSelectionModel().getSelectedItem()), "../fxml/UnverifiedAttractionScene.fxml");
            }
        }

    }

    public void selectCountry() {
        ObservableList<String> oCitiesList = FXCollections.observableArrayList(super.db.getAllCitiesInCountry(comboBoxCountries.getValue().toString()));
        comboBoxCities.setItems(oCitiesList);
    }

    public void addSight() {
        if (!(tfSightName.getText().equals("") || tfSightAddress.getText().equals("")
                || comboBoxCountries.getValue() == null || comboBoxCities.getValue() == null)) {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setTitle("NordicGuide");
            dialog.setHeaderText("Add Sight");
            dialog.setContentText("Are you sure you want to add this sight to be shown in the application?");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == ButtonType.OK) {
                super.db.addSight(tfSightName.getText(), tfSightAddress.getText(),
                        super.db.getCityCode(comboBoxCities.getValue().toString()),
                        super.db.getCountryCode(comboBoxCountries.getValue().toString()));
                tfSightName.setText("");
                tfSightAddress.setText("");
                comboBoxCities.getSelectionModel().clearSelection();
                comboBoxCountries.getSelectionModel().clearSelection();

            }
        } else {
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("NordicGuide");
            dialog.setHeaderText("Error in adding sight");
            dialog.setContentText("Please fill out all the fields regarding the sight you are trying to add.");
            dialog.showAndWait();
        }
    }

    public void removeUser() {
        if (!tfUsername.getText().equals("")) {
            super.db.deleteUserAccount(tfUsername.getText(), null);
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("NordicGuide");
            dialog.setHeaderText("Remove user complete!");
            dialog.setContentText("The user has successfully been removed from the database.");
            dialog.showAndWait();
            tfUsername.setText("");
        } else {
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("NordicGuide");
            dialog.setHeaderText("Error in removing user");
            dialog.setContentText("You must enter a username to remove from the database");
            dialog.showAndWait();
        }
    }

    public void handleHelpButton() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Instructions");
        dialog.setContentText("Please click on an attraction to view the information in order to verify and publish it to the rest of the users." +
                "\n\nFill out the information under 'Add sight' and click the 'Add Sight' button in order to add a sight to the database.");
        dialog.showAndWait();
    }
}
