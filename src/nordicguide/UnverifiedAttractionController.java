package nordicguide;

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
 * Created by Vilhelm on 2017-05-04.
 */
public class UnverifiedAttractionController extends Controller implements Initializable {
    @FXML
    private TextField textField0, textField1, textField2, textField3, textField4, textField5, textField6, textField7;
    private TextField[] textFields;
    @FXML
    private Label attractionLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFields = new TextField[]{textField0, textField1, textField2, textField3, textField4, textField5, textField6, textField7};
        sceneHistory.push("../fxml/UnverifiedAttractionScene.fxml");
    }

    public void setAttractionInfo(ArrayList<String> attractionInfo) {
        for (int i = 0; i < textFields.length; i++) {
            textFields[i].setText(attractionInfo.get(i));
        }
        attractionLabel.setText(textFields[1].getText());
    }

    public void verifyAttraction(MouseEvent me) throws IOException {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Verify Attraction");
        dialog.setContentText("Are you sure you want to verify this attraction to be shown in the application?");
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == ButtonType.OK) {
            super.db.verifyAddRequest(textFields[1].getText(), Integer.parseInt(textFields[0].getText()));
            super.switchScene(me, "../fxml/MainPage.fxml");
        }
    }

    public void declineAttraction(MouseEvent me) throws IOException {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Verify Attraction");
        dialog.setContentText("Are you sure you want to decline and remove this attraction?");
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == ButtonType.OK) {
            super.db.removeAttraction(textFields[1].getText(), Integer.parseInt(textFields[0].getText()));
            super.switchScene(me, "../fxml/MainPage.fxml");
        }
    }

    public void handleHelpButton() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Instructions");
        dialog.setContentText("Check that the information in this attraction is correct and verify if satisfactory");
        dialog.showAndWait();
    }
}
