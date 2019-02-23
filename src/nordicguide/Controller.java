package nordicguide;


import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;


public abstract class Controller {

    static NordicDatabase db = new NordicDatabase();
    //used for click and single search history
    static Stack<String> searchHistory = new Stack<>();
    //used when user searches with both destination and attraction
    static Stack<String> multiSearchHistory = new Stack<>();
    //used to know which scene to go back to
    static Stack<String> sceneHistory = new Stack<>();

    private static BasicAccount account;

    public void setAccountButtons(AnchorPane aPane) {
        Button btnAccount = new Button("Guest");
        btnAccount.setStyle("-fx-border-color: transparent; -fx-border-width: 0; -fx-background-radius: 0; -fx-background-color: transparent; " +
                "-fx-font-family:Quicksand; -fx-font-size: 1.8em; -fx-text-fill: black; -fx-effect: dropshadow( three-pass-box , white , 10 , 0 , 0 , 0 );");
        btnAccount.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                btnAccount.setStyle("-fx-border-color: transparent; -fx-border-width: 0; -fx-background-radius: 0; -fx-background-color: transparent; " +
                        "-fx-font-family:Quicksand; -fx-font-size: 1.8em; -fx-text-fill: white; -fx-effect: dropshadow( three-pass-box , white , 10 , 0 , 0 , 0 );");
            }
        });

        btnAccount.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                btnAccount.setStyle("-fx-border-color: transparent; -fx-border-width: 0; -fx-background-radius: 0; -fx-background-color: transparent; " +
                        "-fx-font-family:Quicksand; -fx-font-size: 1.8em; -fx-text-fill: black; -fx-effect: dropshadow( three-pass-box , white , 10 , 0 , 0 , 0 );");
            }
        });
        aPane.getChildren().add(btnAccount);
        if (account instanceof AdminAccount) {
            btnAccount.setText("Admin: " + ((AdminAccount) account).getEmployeeName());
            btnAccount.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    try {
                        switchToAdminScene(me, db.getAllUnVerifiedHotels(), db.getAllUnVerifiedRestauraunts(), db.getAllUnVerifiedMuseums());
                        sceneHistory.push("../fxml/MainPage.fxml");
                    } catch (IOException e) {
                    }
                }
            });
        } else if (account instanceof CorporateAccount) {
            btnAccount.setText("Company: " + ((CorporateAccount) account).getCompanyName());
            btnAccount.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    try {
                        switchScene(me, "../fxml/CorporateScene.fxml");
                        sceneHistory.push("../fxml/MainPage.fxml");
                    } catch (IOException e) {
                    }
                }
            });
        } else if (account instanceof UserAccount) {
            btnAccount.setText("User: " + ((UserAccount) account).getUserName());
            btnAccount.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    try {
                        switchScene(me, "../fxml/PrivateUserScene.fxml");
                        sceneHistory.push("../fxml/MainPage.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void setAccount(BasicAccount account) {
        this.account = account;
    }

    public BasicAccount getAccount() {
        return this.account;
    }

    public void goBack(MouseEvent me) throws IOException {

        switch (sceneHistory.peek()) {
            case "../fxml/StartPage.fxml":
                switchScene(me, "../fxml/StartPage.fxml");
                break;
            case "../fxml/MainPage.fxml":
                sceneHistory.pop();
                switchScene(me, "../fxml/Mainpage.fxml");
                break;
            case "../fxml/Countries.fxml":
                sceneHistory.pop();
                searchHistory.pop();
                switchToCountries(me, db.getAllCountries());
                break;
            case "../fxml/Cities.fxml":
                searchHistory.pop();
                sceneHistory.pop();
                switchToCitiesScene(me, db.getAllCitiesInCountry(searchHistory.peek()));
                break;
            case "../fxml/SearchResults.fxml":
                sceneHistory.pop();
                searchHistory.pop();
                if (!multiSearchHistory.isEmpty()) {
                    switchToSearchResultsScene(me, db.searchForAttractionsInRegion(multiSearchHistory.firstElement(), multiSearchHistory.peek()));
                    multiSearchHistory.pop();
                    multiSearchHistory.pop();
                } else if (db.isACountry(searchHistory.peek())) {
                    //if you have only searched for a country it will be added to the searchHistory twice,
                    //once when you search and once when you click the searchResult
                    //therefore we first need to get the cities in the country and then the original search
                    if (searchHistory.size() >= 2) {
                        switchToSearchResultsScene(me, db.getAllCitiesInCountry(searchHistory.peek()));
                    } else
                        switchToSearchResultsScene(me, db.searchForRegion(searchHistory.peek()));
                } else if (db.isAAttraction(searchHistory.peek())) {
                    switchToSearchResultsScene(me, db.searchForAttractions(searchHistory.peek()));
                } else {
                    switchToSearchResultsScene(me, db.searchForRegion(searchHistory.peek()));
                }
                break;
            case "../fxml/City.fxml":
                searchHistory.pop();
                sceneHistory.pop();
                switchToCityScene(me, db.getAllHotelsInCity(searchHistory.peek()), db.getAllRestaurantsInCity(searchHistory.peek()),
                        db.getAllMuseumsInCity(searchHistory.peek()), db.getAllSightsInCity(searchHistory.peek()));
                break;
            case "../fxml/AdminScene.fxml":
                switchScene(me, "../fxml/MainPage.fxml");
                break;
            case "../fxml/UnverifiedAttractionScene.fxml":
                sceneHistory.pop();
                switchToAdminScene(me, db.getAllUnVerifiedHotels(), db.getAllUnVerifiedRestauraunts(), db.getAllUnVerifiedMuseums());
                break;
        }
    }


    public void switchScene(MouseEvent me, String view) throws IOException {
        Node node = (Node) me.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Sets account specific buttons
        Controller ctrl = loader.getController();
        if (!(ctrl instanceof StartPageController || ctrl instanceof LoginController || ctrl instanceof RegisterController)) {
            setAccountButtons((AnchorPane) root);
        }
    }

    public void switchToCountries(MouseEvent me, ArrayList<String> results) throws IOException {
        Node node = (Node) me.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Countries.fxml"));
        Parent root = loader.load();

        // Get controller in order to send data between scenes.
        CountriesController countriesCon = loader.getController();
        countriesCon.setCountriesResult(results);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        // Sets account specific buttons
        setAccountButtons((AnchorPane) root);
    }

    public void switchToCitiesScene(MouseEvent me, ArrayList<String> results) throws IOException {
        Node node = (Node) me.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Cities.fxml"));
        Parent root = loader.load();

        // Get controller in order to send data between scenes.
        CitiesController citiesCon = loader.getController();
        citiesCon.setCitiesResult(results);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        // Sets account specific buttons
        setAccountButtons((AnchorPane) root);
    }

    public void switchToSearchResultsScene(MouseEvent me, ArrayList<String> results) throws IOException {

        Node node = (Node) me.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/SearchResults.fxml"));
        Parent root = loader.load();

        // Get controller in order to send data between scenes.
        SearchResultsController srCon = loader.getController();
        srCon.setSearchResult(results);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        // Sets account specific buttons
        setAccountButtons((AnchorPane) root);
    }

    public void switchToCityScene(MouseEvent me, ArrayList<String> hotels, ArrayList<String> restaurants, ArrayList<String> museums, ArrayList<String> sights) throws IOException {
        Node node = (Node) me.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/City.fxml"));
        Parent root = loader.load();

        // Get controller in order to send data between scenes.
        CityController cityCon = loader.getController();
        cityCon.setCityResult(hotels, restaurants, museums, sights);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        // Sets account specific buttons
        setAccountButtons((AnchorPane) root);
    }

    public void switchToAdminScene(MouseEvent me, ArrayList<String> hotels, ArrayList<String> restaurants, ArrayList<String> museums) throws IOException {
        Node node = (Node) me.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/AdminScene.fxml"));
        Parent root = loader.load();

        // Get controller in order to send data between scenes.
        AdminController adminCon = loader.getController();
        adminCon.setUnVerifiedAttractions(hotels, restaurants, museums);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        // Sets account specific buttons
        setAccountButtons((AnchorPane) root);
    }

    /* This method is used to switch to both AttractionScene and UnverifiedAttractionScene
    * because the information sent to both are the same.*/
    public void switchToAttractionsScene(MouseEvent me, ArrayList<String> attractionInfo, String sceneFxml) throws IOException {
        Node node = (Node) me.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneFxml));
        Parent root = loader.load();

        // Get controller in order to send data between scenes.
        switch (sceneFxml) {
            case "../fxml/AttractionPage.fxml":
                AttractionController attractCon = loader.getController();
                attractCon.setAttractionInfo(attractionInfo);
                break;
            case "../fxml/UnverifiedAttractionScene.fxml":
                UnverifiedAttractionController unverifiedAttractionCon = loader.getController();
                unverifiedAttractionCon.setAttractionInfo(attractionInfo);
                break;
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        // Sets account specific buttons
        setAccountButtons((AnchorPane) root);
    }

    public void switchToSightsScene(String City) {

    }

}
