package nordicguide;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by marbac on 4/12/2017.
 */
public class RegisterController extends Controller implements Initializable {

    @FXML
    private javafx.scene.control.TextField textFieldName, textFieldAddress, textFieldCivic, textFieldPhone, textFieldEmail,
            textFieldCompanyName, textFieldOCR, textFieldPassword;
    @FXML
    private CheckBox checkBoxIfCompany;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        textFieldOCR.editableProperty().bind(checkBoxIfCompany.selectedProperty());
        textFieldCompanyName.editableProperty().bind(checkBoxIfCompany.selectedProperty());

        textFieldName.editableProperty().bind(checkBoxIfCompany.selectedProperty().not());
        textFieldCivic.editableProperty().bind(checkBoxIfCompany.selectedProperty().not());

    }

    public void handleCheckBox() {
        if (checkBoxIfCompany.isSelected()) {
            textFieldName.clear();
            textFieldCivic.clear();
        } else {
            textFieldCompanyName.clear();
            textFieldOCR.clear();
        }

    }

    public void register(MouseEvent me) throws IOException {

        // Method that saves corporateUser/privateUser depending on checkbox selection.
        if (checkBoxIfCompany.isSelected()) {

            try {
                if (!(textFieldCompanyName.getText().equals("") || textFieldOCR.getText().equals("") ||
                        textFieldEmail.getText().equals("") || textFieldPassword.getText().equals(""))) {

                    super.db.addCorporateUser(textFieldCompanyName.getText(), textFieldPhone.getText(), textFieldAddress.getText(),
                            textFieldEmail.getText(), Integer.parseInt(textFieldOCR.getText()), textFieldPassword.getText());

                    super.switchScene(me, "../fxml/LogInMenu.fxml");
                } else {
                    Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                    errorDialog.setTitle("NordicGuide");
                    errorDialog.setHeaderText("Error");
                    errorDialog.setContentText("All textfields whit * must be added to be able to register you corporate account");
                    errorDialog.showAndWait();
                }
            } catch (Exception e) {
                Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                errorDialog.setTitle("NordicGuide");
                errorDialog.setHeaderText("Error");
                errorDialog.setContentText("OCR-number must be numbers only!");
                errorDialog.showAndWait();

            }

        } else {

            try {
                if (!(textFieldName.getText().equals("") || textFieldCivic.getText().equals("") || textFieldEmail.getText().equals("") ||
                        textFieldPassword.getText().equals(""))) {

                    if (textFieldEmail.getText().contains("@")) {

                        super.db.addPrivateUser(textFieldName.getText(), textFieldPhone.getText(), textFieldAddress.getText(),
                                textFieldEmail.getText(), Integer.parseInt(textFieldCivic.getText()), textFieldPassword.getText());

                        super.switchScene(me, "../fxml/LogInMenu.fxml");

                    } else {

                        Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                        errorDialog.setTitle("NordicGuide");
                        errorDialog.setHeaderText("Error");
                        errorDialog.setContentText("Your E-mail must contain @ ");
                        errorDialog.showAndWait();
                    }

                } else {
                    Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                    errorDialog.setTitle("NordicGuide");
                    errorDialog.setHeaderText("Error");
                    errorDialog.setContentText("All textfields whit * must be added to be able to register you private account");
                    errorDialog.showAndWait();
                }


            } catch (Exception e) {
                Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                errorDialog.setTitle("NordicGuide");
                errorDialog.setHeaderText("Error");
                errorDialog.setContentText("Civicnumber must be numbers only!");
                errorDialog.showAndWait();

            }

        }
    }

    public void handleHelpButton() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Instructions");
        dialog.setContentText("Please enter your information in the empty text fields for registration. " +
                "\n \nIf you want to register a corporate user, please use the box in the upper right corner " +
                "and fill in the information.");
        dialog.showAndWait();
    }

}
