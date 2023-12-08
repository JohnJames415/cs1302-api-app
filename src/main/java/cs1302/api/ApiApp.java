package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
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
import java.net.URI;
import com.google.gson.GsonBuilder;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {

    public static final String CS_API = "https://www.cheapshark.com/api/1.0/deals?storeID=1";
    public static final String INPUT_INSTRUCTIONS =
        "Enter criteria for the sales, then press the Search button. If you have no preference for"
        + " a given element, leave it blank";
    public static final String DISPLAY_INSTRUCTIONS = "After you have filtered your results,"
        + " select a game, then press the load button";

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

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new HBox();
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
        gameImageContainer.setPreserveRatio(true);
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
