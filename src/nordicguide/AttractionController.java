package nordicguide;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Pablo on 2017-04-20.
 */
public class AttractionController extends Controller implements Initializable {


    @FXML
    private ListView<String> attractionInfoList, commentsList;

    @FXML
    private Button submitCommentButton, returnButton, submitReplyButton, submitRatingButton, rmAttractionButton;

    @FXML
    private RadioButton r1, r2, r3, r4, r5;

    @FXML
    private Text ratingTxt1, ratingTxt2, ratingTxt3, ratingTxt4, ratingTxt5, rating1, rating2, rating3, rating4, rating5;

    @FXML
    private Label attractionLabel;

    @FXML
    private TextField commentTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        RadioButton[] radioButtons = {r1, r2, r3, r4, r5};
        Text[] ratingStars = {ratingTxt1, ratingTxt2, ratingTxt3, ratingTxt4, ratingTxt5};

        if (db.getCommentsFromAttraction(searchHistory.peek()) != null) {
            updateComments();
        }

        if (getAccount() instanceof UserAccount) {
            if (db.isRated(searchHistory.peek(), ((UserAccount) getAccount()).getUserName())) {
                submitRatingButton.setVisible(false);
                for (int i = 0; i <= 4; i++) {
                    ratingStars[i].setVisible(false);
                    radioButtons[i].setVisible(false);
                }
            }

        } else if (!(getAccount() instanceof UserAccount)) {
            submitCommentButton.setVisible(false);
            submitReplyButton.setVisible(false);
            submitRatingButton.setVisible(false);
            for (int i = 0; i <= 4; i++) {
                ratingStars[i].setVisible(false);
                radioButtons[i].setVisible(false);
            }
        }

        if (getAccount() instanceof AdminAccount) {
            rmAttractionButton.setVisible(true);
            commentTextField.setVisible(false);
        }

        if ((getAccount() instanceof CorporateAccount)) {
            submitReplyButton.setVisible(true);
        }

        int rating = db.getRating(db.getAttractionId(searchHistory.peek()));
        switch (rating) {
            case 0:
                rating5.setVisible(false);
                rating4.setVisible(false);
                rating3.setVisible(false);
                rating2.setVisible(false);
                rating1.setVisible(false);
                break;
            case 1:
                rating5.setVisible(false);
                rating4.setVisible(false);
                rating3.setVisible(false);
                rating2.setVisible(false);
                break;
            case 2:
                rating5.setVisible(false);
                rating4.setVisible(false);
                rating3.setVisible(false);
                break;
            case 3:
                rating5.setVisible(false);
                rating4.setVisible(false);
                break;
            case 4:
                rating5.setVisible(false);
                break;
        }

    }

    public void setAttractionInfo(ArrayList<String> attractionInfo) {
        ObservableList<String> oAttractionInfo = FXCollections.observableArrayList();
        oAttractionInfo.addAll(attractionInfo);
        attractionInfoList.setItems(oAttractionInfo);
        attractionLabel.setText(attractionInfo.get(0));

    }
    public void handleWebAddress(MouseEvent me)throws IOException{
        if (attractionInfoList.getSelectionModel().getSelectedItem().startsWith("www")||
                attractionInfoList.getSelectionModel().getSelectedItem().startsWith("http")){

        if(Desktop.isDesktopSupported())
        {
            try {
                Desktop.getDesktop().browse(new URI(attractionInfoList.getSelectionModel().getSelectedItem()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }}
    }

    public void handleHelpButton() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Instructions");
        dialog.setContentText("The information about the attraction is shown in the upper left corner." +
                "\n \nIf you want to submit a comment, please write the comment in the text field under the " +
                "'Submit comment' button and then press the button.");
        dialog.showAndWait();
    }

    public void handleCommentButton(MouseEvent me) {
        if (!commentTextField.getText().isEmpty()) {
            db.writeCommentToDB(commentTextField.getText(), searchHistory.peek(),
                    ((UserAccount) getAccount()).getUserName());
        } else {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("NordicGuide");
            dialog.setHeaderText("ERROR");
            dialog.setContentText("Please write a comment in the text field.");
            dialog.showAndWait();
        }
        updateComments();
        commentTextField.clear();
    }

    public void handleReplyButton(MouseEvent me) {
        String tmp = commentsList.getSelectionModel().getSelectedItem();
        int tmp2 = tmp.length() - 21;
        String replyOrComment = tmp.substring(3, 5);

        String userName = tmp.substring(0, tmp.indexOf(":"));
        String date = tmp.substring(tmp2);

        if (!replyOrComment.equals(">>")) {
            int commentId = db.getCommentId(userName, date).get(0);
            if (commentTextField != null) {
                db.writeReplyToDataBase(commentId, getAccount(), commentTextField.getText());
            } else {
                Alert dialog = new Alert(Alert.AlertType.ERROR);
                dialog.setTitle("NordicGuide");
                dialog.setHeaderText("ERROR");
                dialog.setContentText("Please write a reply in the text field.");
                dialog.showAndWait();
            }
        } else {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("NordicGuide");
            dialog.setHeaderText("ERROR");
            dialog.setContentText("It is only possible to submit a reply to a comment.");
            dialog.showAndWait();
        }
        updateComments();
        commentTextField.clear();
    }

    public void updateComments() {
        ArrayList<String> comments ;
        ArrayList<Integer> commentsId ;
        ArrayList<String> replies ;

        comments = db.getCommentsFromAttraction(searchHistory.peek());
        commentsId = db.getCommentsIdFromAttraction(searchHistory.peek());
        ObservableList<String> oCommentsList = FXCollections.observableArrayList();

        for (int i = 0; i < comments.size(); i++) {

            replies = db.getReplyFromComment(commentsId.get(i));

            oCommentsList.add(comments.get(i));
            oCommentsList.addAll(replies);

        }
        commentsList.setItems(oCommentsList);
    }

    public void handleRatingButton(MouseEvent me) throws IOException {
        int rating = 0;
        RadioButton[] radioButtons = {r1, r2, r3, r4, r5};
        Text[] ratingStars = {ratingTxt1, ratingTxt2, ratingTxt3, ratingTxt4, ratingTxt5};

        if (r1.isSelected()) {
            rating = 1;
        } else if (r2.isSelected()) {
            rating = 2;
        } else if (r3.isSelected()) {
            rating = 3;
        } else if (r4.isSelected()) {
            rating = 4;
        } else if (r5.isSelected()) {
            rating = 5;
        }
        if (rating != 0) {
            db.writeRatingToDatabase(searchHistory.peek(), ((UserAccount) getAccount()).getUserName(), rating);
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("NordicGuide");
            dialog.setHeaderText("Rating done");
            dialog.setContentText("Thank you for your rating.");
            dialog.showAndWait();
        } else {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("NordicGuide");
            dialog.setHeaderText("ERROR");
            dialog.setContentText("Please select a rating before submitting it");
            dialog.showAndWait();
        }

        if (db.isRated(searchHistory.peek(), ((UserAccount) getAccount()).getUserName())) {
            submitRatingButton.setVisible(false);
            for (int i = 0; i <= 4; i++) {
                ratingStars[i].setVisible(false);
                radioButtons[i].setVisible(false);
            }
        }
    }

    public void removeAttraction(MouseEvent me) throws IOException {
        String attractionName = attractionLabel.getText();
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("NordicGuide");
        dialog.setHeaderText("Remove Attraction");
        dialog.setContentText("Are you sure you want to remove this attraction from the application?");
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == ButtonType.OK) {
            super.db.removeAttraction(attractionName, super.db.getAttractionId(attractionName));
            goBack(me);
        }
    }
}
