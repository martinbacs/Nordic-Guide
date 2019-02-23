package nordicguide;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by marbac on 4/12/2017.
 */
public class LoginController extends Controller implements Initializable {

    @FXML
    private TextField textFieldEmail;
    @FXML
    private PasswordField passwordField;

    private final String FILENAME = "latestLogIn.txt";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        FileReader read = null;
        BufferedReader reader = null;

        try {
            read = new FileReader(FILENAME);
            reader = new BufferedReader(read);

            reader = new BufferedReader(new FileReader(FILENAME));

            textFieldEmail.appendText(reader.readLine());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (read != null)
                    read.close();

            } catch (IOException ex) {

                ex.printStackTrace();
            }
        }
    }

    public void login(MouseEvent me) throws IOException {
    //    String accountType = super.db.handleLogIn(textFieldEmail.getText(), passwordField.getText());
    String accountType = "UserAccount";
        BufferedWriter writer = new BufferedWriter(new FileWriter("latestLogIn.txt"));

        writer.write(textFieldEmail.getText());
        writer.flush();
        writer.close();

        //String userName, String phoneNumber, String adress, String email, long civicNumber
        ArrayList<String> accInfo;
        switch (accountType) {
            case "UserAccount":
               // accInfo = super.db.getPrivateUser(textFieldEmail.getText());
                //UserAccount userAccount = new UserAccount(accInfo.get(0), accInfo.get(1), accInfo.get(2),
                 //       accInfo.get(3), Long.parseLong(accInfo.get(4)));
                UserAccount userAccount = new UserAccount("123", "123", "someaddress", "ng@test.se", 123);

                super.setAccount(userAccount);
                super.switchScene(me, "../fxml/MainPage.fxml");
                break;
            case "CorporateAccount":
                accInfo = super.db.getCorporateUser(textFieldEmail.getText());
                CorporateAccount corpAccount = new CorporateAccount(accInfo.get(0), Long.parseLong(accInfo.get(1)),
                        accInfo.get(2), accInfo.get(3), accInfo.get(4));
                super.setAccount(corpAccount);
                super.switchScene(me, "../fxml/MainPage.fxml");
                break;
            case "AdminAccount":
                accInfo = super.db.getAdminUser(textFieldEmail.getText());
                AdminAccount adminAccount = new AdminAccount(accInfo.get(0), Long.parseLong(accInfo.get(1)),
                        accInfo.get(2), accInfo.get(3), accInfo.get(4));
                super.setAccount(adminAccount);
                super.switchScene(me, "../fxml/MainPage.fxml");
                break;
            default:
                super.switchScene(me, "../fxml/MainPage.fxml");

                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("NordicGuide");
                dialog.setHeaderText("ERROR");
                dialog.setContentText("Wrong username/password");
                dialog.showAndWait();
        }
    }

    public void handleHelpButton() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Instructions");
        dialog.setContentText("Please enter your e-mail and password in the text fields. If you " +
                "have not done a registration, use the back button and select 'Register'.");
        dialog.showAndWait();
    }
}
