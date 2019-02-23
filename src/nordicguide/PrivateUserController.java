package nordicguide;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by marbac on 4/12/2017.
 */
public class PrivateUserController extends Controller implements Initializable {

    @FXML
    private TextField emailTxtField, phoneTxtField, addressTxtField, passwordTxtField;
    @FXML
    private Label userNameLabel, civicNumberLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userNameLabel.setText(((UserAccount) getAccount()).getUserName());
        civicNumberLabel.setText(String.valueOf(((UserAccount) getAccount()).getCivicNumber()));
        emailTxtField.setText((getAccount().getEmail()));
        phoneTxtField.setText(getAccount().getPhoneNumber());
        addressTxtField.setText(getAccount().getAdress());
        passwordTxtField.setText(db.getPassword(getAccount().getEmail()));
    }

    public void handleSaveButton() {
        if (!(phoneTxtField.getText().equals("") || addressTxtField.getText().equals("") ||
                emailTxtField.getText().equals("") || passwordTxtField.getText().equals("") || userNameLabel.getText().equals(""))) {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setTitle("NordicGuide");
            dialog.setHeaderText("Confirmation");
            dialog.setContentText("Are you sure you want to save these changes?");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == ButtonType.OK) {
                db.updatePrivateUserInfo(phoneTxtField.getText(), addressTxtField.getText(),
                        emailTxtField.getText(), passwordTxtField.getText(), userNameLabel.getText());
                dialog.setContentText("Your changes have been saved.");
                dialog.show();
            }
        } else {
            Alert errorDialog = new Alert(Alert.AlertType.ERROR);
            errorDialog.setTitle("NordicGuide");
            errorDialog.setHeaderText("Error");
            errorDialog.setContentText("All the fields must be filled out in order to save your information.");
            errorDialog.showAndWait();
        }
    }

    public void handleCloseAccountBtn(MouseEvent me) {

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("NordicGuide");
        confirmationDialog.setHeaderText("Delete account");
        confirmationDialog.setContentText("Are you sure you want to delete/close your account from the Nordic-Guide.");
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.get() == ButtonType.OK) {
            db.deleteUserAccount(userNameLabel.getText(), getAccount());

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
            confirmationDialog.close();
        }
    }

    public void handleHelpButton() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Account information");
        dialog.setContentText("Here you can edit the information about your account, click the 'Save' button to commit your changes." +
                "Click 'Close account' to delete your account permanently.");
        dialog.showAndWait();
    }

}
