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
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import java.net.URL;
import com.google.gson.GsonBuilder;
import javafx.scene.layout.Priority;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {

    public static final String API_KEY = "ld2YNdZSjRnKAsMxzaxrfw==HKnxy4J95zXmNJYy";
    public static final String CC_API = "https://api.api-ninjas.com/v1/convertcurrency";
    public static final String CS_API = "https://www.cheapshark.com/api/1.0/deals?storeID=1";
    public static final String INPUT_INSTRUCTIONS =
        "Enter criteria for the sales, then press the Search button.\nIf you have no preference for"
        + " a given option,\nleave it blank.";
    public static final String DISPLAY_INSTRUCTIONS = "After you have filtered your results,\n"
        + "select a game, then press the load button.";
    public static final String NOTICE_TEXT = "Note: This app prioritizes giving you discounted"
        + " games\n(and those will gravitate toward the top of the dropdown).\nAfter which, the"
        + " program will give you options for any\ngame within the specified criteria.";

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
    Label normalPriceLabel;
    Label salePriceLabel;
    Label scoreLabel;
    Label discount;
    Button loadButton;
    EventHandler<ActionEvent> getGames;
    EventHandler<ActionEvent> displayGame;
    Game[] results;
    Label noticeLabel;

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
        this.salePriceLabel = new Label();
        this.normalPriceLabel = new Label();
        this.scoreLabel = new Label();
        this.discount = new Label();
        this.loadButton = new Button("Load");
        this.noticeLabel = new Label(NOTICE_TEXT);
        this.getGames = e -> {
            try {
                results = getGamesInfo(minPriceBar, maxPriceBar, metacriticScoreBar);
                updateDropdown(gameList, results);
                loadButton.setDisable(false);
            } catch (InterruptedException | IOException error) {
                alertError(error);
            }
        };
        this.displayGame = e -> {
            for (int i = 0; i < results.length; i++) {
                if (results[i].title.equals(gameList.getValue())) {
                    try {
                        gameTitle.setText(results[i].title);
                        gameImageContainer.setImage(new Image(results[i].thumb));
                        CurrencyConvert data = getCCInfo(currencyDropdown, results, false, i);
                        normalPriceLabel.setText("Normal Price: " + data.newAmount + " "
                                                 + currencyDropdown.getValue());
                        data = getCCInfo(currencyDropdown, results, true, i);
                        salePriceLabel.setText("Sale Price: " + data.newAmount + " "
                                               + currencyDropdown.getValue());
                        scoreLabel.setText("Metacritic Score: " + results[i].metacriticScore);
                        discount.setText("Discount %: " + results[i].savings);
                    } catch (InterruptedException | IOException error) {
                        alertError(error);
                    }
                }
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
                                         gameImageContainer, salePriceLabel, normalPriceLabel,
                                         scoreLabel, discount, loadButton, noticeLabel);
        gameImageContainer.setFitWidth(120);
        gameImageContainer.setFitHeight(45);
        currencyDropdown.getItems().addAll("USD", "EUR", "CNY", "CHF", "AUD",
                                           "PLN", "TRY", "CAD", "JPY", "GBP",
                                           "NZD", "KRW", "DKK", "HKD");
        HBox.setHgrow(gameList, Priority.ALWAYS);
        currencyDropdown.getSelectionModel().select(0);
        searchButton.setOnAction(getGames);
        loadButton.setOnAction(displayGame);
        loadButton.setDisable(true);
        dropdownInstructions.setWrapText(true);
        inputInstructions.setWrapText(true);
        noticeLabel.setWrapText(true);
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

    // From hw 7.6
    /**
     * This method opens an error window for the given throwable.
     *
     * @param cause the throwable to display on the window.
     */
    public static void alertError(Throwable cause) {
        TextArea text = new TextArea(cause.toString());
        text.setEditable(false);
        Alert alert = new Alert(AlertType.ERROR);
        alert.getDialogPane().setContent(text);
        alert.setResizable(true);
        alert.showAndWait();
    } // alertError


    /**
     * This method assigns a provided value to a string iff the provided
     * is not empty.
     *
     * @param textfield the textfield to evaluate.
     * @param value String to be assigned if it's not empty.
     * @return the string to be assigned to the variable.
     */
    public static String stringAssignment(TextField textfield, String value) {
        if (textfield.getText().equals("")) {
            return "";
        } else {
            return value + textfield.getText();
        }
    }

    /**
     * This method goes through the process of sending out the HttpRequest, getting the
     * response, and returning the CurrencyConvert object created by it.
     *
     * @param currencyDropdown the dropdown used to get the currency value from.
     * @param results the array of Game objects to get prices from.
     * @param isSalePrice true if converting the salePrice of a game, and false if it's the
     * normal price.
     * @param index the index to get the prices from in the Game array.
     * @return the CurrencyConvert object retrieved from the API.
     */
    public static CurrencyConvert getCCInfo(ComboBox<String> currencyDropdown, Game[] results,
                                            boolean isSalePrice, int index) throws IOException,
        InterruptedException {
        String haveString = "?have=USD";
        String wantString = "&want=" + currencyDropdown.getValue();
        String amountString;
        if (isSalePrice) {
            amountString = "&amount=" + results[index].salePrice;
        } else {
            amountString = "&amount=" + results[index].normalPrice;
        }
        String uri = CC_API + haveString + wantString + amountString;
        //build request

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri))
            .header("X-Api-Key", API_KEY)
            .header("Content-Type", "application/json")
            .build();
        HttpResponse<String> response =
            HTTP_CLIENT.send(request, BodyHandlers.ofString());

        return GSON.fromJson(response.body(), CurrencyConvert.class);
    }


    /**
     * This method goes through the process of sending out the HttpRequest, getting the
     * response, and returning the CurrencyConvert object created by it.
     *
     * @param minPriceBar the text bar to get the minimum price data from.
     * @param maxPriceBar the text bar to get the maximum price data from.
     * @param metacriticScoreBar the text bar to get the metacritic score data from.
     * @return the Game[] object retrieved from the API.
     */
    public static Game[] getGamesInfo(TextField minPriceBar, TextField maxPriceBar,
                                            TextField metacriticScoreBar) throws IOException,
        InterruptedException {
        String minPriceData = stringAssignment(minPriceBar, "&lowerPrice=");
        String maxPriceData = stringAssignment(maxPriceBar, "&upperPrice=");
        String metacriticData = stringAssignment(metacriticScoreBar, "&metacritic=");
        String uri = CS_API + minPriceData + maxPriceData + metacriticData;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());

        if (GSON.fromJson(response.body(), Game[].class).length == 0) {
            throw new IOException("No results found for given criteria.");
        }

        return GSON.fromJson(response.body(), Game[].class);

    }

    /**
     * This method updates the dropdown whenever a new search is made.
     *
     * @param dropdown the dropdown to be updated.
     * @param results the Game[] to add titles from.
     */
    public static void updateDropdown(ComboBox<String> dropdown, Game[] results) {
        dropdown.getItems().clear();
        for (int i = 0; i < results.length; i++) {
            dropdown.getItems().addAll(results[i].title);
        }
        dropdown.getSelectionModel().select(0);
    }

} // ApiApp
