 package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import com.google.gson.Gson;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import java.net.URL;
import com.google.gson.GsonBuilder;
import javafx.scene.layout.Priority;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {

    public static final String CS_API = "https://www.cheapshark.com/api/1.0/deals?storeID=1";
    public static final String INPUT_INSTRUCTIONS =
        "Enter criteria for the sales, then press the Search button.\nIf you have no preference for"
        + " a given option,\nleave it blank.";
    public static final String DISPLAY_INSTRUCTIONS = "After you have filtered your results,\n"
        + "select a game, then press the load button.";

    // HttpClient taken from project 4
    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    // GSON taken from project 4
    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    Stage stage;
    Scene scene;
    HBox root;

    //Instance Variables

    VBox inputVBox;
    VBox displayVBox;
    Label inputInstructions;
    Label minPrice;
    Label maxPrice;
    TextField minPriceBar;
    TextField maxPriceBar;
    Label metacriticScore;
    TextField metacriticScoreBar;
    Label currencyLabel;
    ComboBox<String> currencyDropdown;
    Button searchButton;

    Label dropdownInstructions;
    ComboBox<String> gameList;
    Label gameTitle;
    ImageView gameImageContainer;
    Label normalPrice;
    Label salePrice;
    Label scoreLabel;
    Label discount;
    Button loadButton;
    //Runnable getGames;
    //Runnable displayGame;
    EventHandler<ActionEvent> getGames;
    EventHandler<ActionEvent> displayGame;

    EventHandler<ActionEvent> getGamesEvent;
    EventHandler<ActionEvent> displayGameEvent;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new HBox(60);
        this.inputVBox = new VBox();
        this.displayVBox = new VBox();
        this.minPrice = new Label("Minimum Price of Game (in USD)");
        this.maxPrice = new Label("Maximum Price of Game (in USD)");
        this.inputInstructions = new Label(INPUT_INSTRUCTIONS);
        this.minPriceBar = new TextField();
        this.maxPriceBar = new TextField();
        this.metacriticScore = new Label("Minimum Metacritic score (0 to 100)");
        this.metacriticScoreBar = new TextField();
        this.currencyLabel = new Label("Currency (to display prices in)");
        this.currencyDropdown = new ComboBox<String>();
        this.searchButton = new Button("Search");

        this.dropdownInstructions = new Label(DISPLAY_INSTRUCTIONS);
        this.gameList = new ComboBox<String>();
        this.gameTitle = new Label();
        this.gameImageContainer = new ImageView(new Image("file:resources/Solid_White.png"));
        this.salePrice = new Label();
        this.normalPrice = new Label();
        this.scoreLabel = new Label();
        this.discount = new Label();
        this.loadButton = new Button("Load");
        //this.getGamesEvent = e -> getGames;
        //this.displayGameEvent = e -> displayGame;
        this.getGames = e -> {
            try {
                String minPriceData = "";
                String maxPriceData = "";
                String metacriticData = "";
                if (!minPriceBar.getText().equals("")) {
                    minPriceData = "&lowerPrice="+ minPriceBar.getText();
                }
                if (!maxPriceBar.getText().equals("")) {
                    maxPriceData = "&upperPrice="+ maxPriceBar.getText();
                }
                if (!metacriticScoreBar.getText().equals("")) {
                    metacriticData = "&metacritic="+ metacriticScoreBar.getText();
                }
                String uri = CS_API + minPriceData + maxPriceData + metacriticData;
                //build request
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
                HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
                // ensure good request
                if(response.statusCode() != 200) {
                    throw new IOException(response.toString());
                }
                Game[] results = GSON.fromJson(response.body(), Game[].class);

                for (int i = 0; i < results.length; i++) {
                    gameList.getItems().addAll(results[i].title);
                }
            } catch (InterruptedException | IOException error) {
                System.out.println("hi");
            }
        };

    } // ApiApp

    //Based of init() method in project 4
    /** {@inheritDoc} */
    @Override
    public void init() {
        root.getChildren().addAll(inputVBox, displayVBox);
        inputVBox.getChildren().addAll(inputInstructions, minPrice, minPriceBar, maxPrice,
                                       maxPriceBar, metacriticScore, metacriticScoreBar,
                                       currencyLabel, currencyDropdown, searchButton);
        displayVBox.getChildren().addAll(dropdownInstructions, gameList, gameTitle,
                                         gameImageContainer, salePrice, normalPrice, scoreLabel,
                                         discount, loadButton);
        gameImageContainer.setFitWidth(120);
        gameImageContainer.setFitHeight(45);
        currencyDropdown.getItems().addAll("USD", "EUR", "CNY", "CHF", "AUD",
                                           "PLN", "TRY", "CAD", "JPY", "GBP",
                                           "NZD", "KRW", "DKK", "HKD");
        HBox.setHgrow(gameList, Priority.ALWAYS);
        currencyDropdown.getSelectionModel().select(0);
        searchButton.setOnAction(getGames);
        dropdownInstructions.setWrapText(true);
        inputInstructions.setWrapText(true);
    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;
        scene = new Scene(root);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start



    //Taken from project 4
    /** {@inheritDoc} */
    @Override
    public void stop() {
        System.out.println("stop() called");
    } // stop

} // ApiApp
