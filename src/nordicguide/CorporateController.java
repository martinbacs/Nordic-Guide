package nordicguide;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Vilhelm on 2017-05-02.
 */
public class CorporateController extends Controller implements Initializable {

    @FXML
    private TextField textFieldName, textFieldAddress, textFieldPhone, textFieldEmail, textFieldWebb;

    @FXML
    private RadioButton radioButtonDk, radioButtonSe, radioButtonNo, radioButtonIs, radioButtonFi, radioButtonRestaurant,
            radioButtonHotel, radioButtonMuseum;

    @FXML
    private ComboBox comboBoxCities;

    private String chosenCountry;

    private byte notVerified = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void radioButtonSelection(MouseEvent me) throws IOException {

        if (radioButtonIs.isSelected()) {
            ObservableList<String> oCitiesOption = FXCollections.observableArrayList(super.db.getAllCitiesInCountry(radioButtonIs.getText()));
            comboBoxCities.setItems(oCitiesOption);
            chosenCountry = "IS";
        }
        if (radioButtonDk.isSelected()) {
            ObservableList<String> oCitiesOption = FXCollections.observableArrayList(super.db.getAllCitiesInCountry(radioButtonDk.getText()));
            comboBoxCities.setItems(oCitiesOption);
            chosenCountry = "DK";
        }
        if (radioButtonNo.isSelected()) {
            ObservableList<String> oCitiesOption = FXCollections.observableArrayList(super.db.getAllCitiesInCountry(radioButtonNo.getText()));
            comboBoxCities.setItems(oCitiesOption);
            chosenCountry = "NO";
        }
        if (radioButtonFi.isSelected()) {
            ObservableList<String> oCitiesOption = FXCollections.observableArrayList(super.db.getAllCitiesInCountry(radioButtonFi.getText()));
            comboBoxCities.setItems(oCitiesOption);
            chosenCountry = "FI";
        }
        if (radioButtonSe.isSelected()) {
            ObservableList<String> oCitiesOption = FXCollections.observableArrayList(super.db.getAllCitiesInCountry(radioButtonSe.getText()));
            comboBoxCities.setItems(oCitiesOption);
            chosenCountry = "SE";
        }

    }

    public void addCompany(MouseEvent me) {

        try {
            if (!(comboBoxCities.getSelectionModel().isEmpty() || textFieldName.getText().equals("") || textFieldWebb.getText().equals("") || textFieldAddress.getText().equals("") &&
                    textFieldPhone.getText().equals("") || textFieldEmail.getText().equals(""))) {

                if (radioButtonHotel.isSelected()) {

                    super.db.addHotel(textFieldName.getText(), textFieldAddress.getText(), textFieldPhone.getText(),
                            textFieldEmail.getText(), textFieldWebb.getText(), super.db.getCityCode(comboBoxCities.getValue().toString()),
                            chosenCountry, ((CorporateAccount) getAccount()).getOcrNr(), notVerified);

                    Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
                    dialog.setTitle("NordicGuide");
                    dialog.setHeaderText("Confirmation");
                    dialog.setContentText("Your hotel has been added to The Nordic-Guide and is awaiting " +
                            " to be reviewed by an admin before it is visible for the users. ");
                    dialog.showAndWait();
                    super.switchScene(me, "../fxml/MainPage.fxml");

                }
                if (radioButtonRestaurant.isSelected()) {

                    super.db.addRestaurant(textFieldName.getText(), textFieldAddress.getText(), textFieldPhone.getText(),
                            textFieldEmail.getText(), textFieldWebb.getText(), super.db.getCityCode(comboBoxCities.getValue().toString()),
                            chosenCountry, ((CorporateAccount) getAccount()).getOcrNr(), notVerified);

                    Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
                    dialog.setTitle("NordicGuide");
                    dialog.setHeaderText("Confirmation");
                    dialog.setContentText("Your restaurant has been added to The Nordic-Guide and is awaiting " +
                            " to be reviewed by an admin before it is visible for the users. ");
                    dialog.showAndWait();
                    super.switchScene(me, "../fxml/MainPage.fxml");
                }
                if (radioButtonMuseum.isSelected()) {

                    super.db.addMuseum(textFieldName.getText(), textFieldAddress.getText(), textFieldPhone.getText(),
                            textFieldEmail.getText(), textFieldWebb.getText(), super.db.getCityCode(comboBoxCities.getValue().toString()),
                            chosenCountry, ((CorporateAccount) getAccount()).getOcrNr(), notVerified);

                    Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
                    dialog.setTitle("NordicGuide");
                    dialog.setHeaderText("Confirmation");
                    dialog.setContentText("Your museum has been added to The Nordic-Guide and is awaiting " +
                            " to be reviewed by an admin before it is visible for the users. ");
                    dialog.showAndWait();
                    super.switchScene(me, "../fxml/MainPage.fxml");

                }

            } else {
                Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                errorDialog.setTitle("NordicGuide");
                errorDialog.setHeaderText("Error");
                errorDialog.setContentText("All information about your company must be added to submit your company!!!" + "\n" +
                        "please make sure you have added all information to the text fields and chosen the location of you company");
                errorDialog.showAndWait();
            }
        } catch (Exception io) {

            io.printStackTrace();
            Alert errorDialog = new Alert(Alert.AlertType.ERROR);
            errorDialog.setTitle("NordicGuide");
            errorDialog.setHeaderText("Error");
            errorDialog.setContentText("All information about your company must be added to submit your company!!!" + "\n" +
                    "please make sure you have added all information and chosen the location of you company");
            errorDialog.showAndWait();
        }

    }

    public void deleteAccount(MouseEvent me) throws IOException {

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Delete account");
        dialog.setContentText("Are you sure you want to delete your account from The Nordic-Guide." +
                "All your adds will be deleted as well.");
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == ButtonType.OK) {

            super.db.deleteUserAccount(((CorporateAccount) getAccount()).getCompanyName(), getAccount());

            Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
            infoDialog.setTitle("NordicGuide");
            infoDialog.setHeaderText("Account deleted");
            infoDialog.setContentText("Your account has now been removed from The Nordic-Guide.");
            infoDialog.showAndWait();

            try {
                super.setAccount(null);
                switchScene(me, "../fxml/StartPage.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            dialog.close();
        }

    }

    public void handleHelpButton() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Instructions");
        dialog.setContentText("Please enter your information in the empty text fields for registration of your company. " +
                "\n \nIf you want to register a hotel, restaurant or museum please click your choice on the lower right corner. " +
                "\n \nYou will also have to choose your company location in the right side," +
                "you choose country and after that you will be able to choose a city from the chosen country.\n\n" +
                "If you wish to cancel your account in the Nordic guide you can do so by pressing the button on the top right corner");
        dialog.showAndWait();
    }

}
