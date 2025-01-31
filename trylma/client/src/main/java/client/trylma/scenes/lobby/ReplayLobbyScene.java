package client.trylma.scenes.lobby;

import client.trylma.ClientApp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Scena lobby, wyświetlająca listę graczy oraz przycisk powrotu do ekranu JoinScene.
 */
public class ReplayLobbyScene extends Scene {

    /** Kontener na dynamicznie dodawane etykiety reprezentujące listę graczy. */
    private final VBox playersContainer;

    /** TextField na id gry którą chcemy odtworzyć */
    private TextField gameIDField;

    /**
     * Konstruktor klasy LobbyScene.
     *
     * @param clientApp instancja głównej aplikacji klienta
     */
    public ReplayLobbyScene(ClientApp clientApp) {
        super(new VBox(20), 750, 750);

        // Inicjalizacja kontenera na graczy
        playersContainer = createPlayersContainer();

        // Tworzenie text fielda na id gry
        Label gameIDLabel = createLabel("Game ID:");
        this.gameIDField = createTextField("Enter game ID");

        // Utworzenie przycisków od rozpoczynania replay
        Button startButton = createStartButton(clientApp);

        // Tworzenie przycisku powrotu
        Button backButton = createBackButton(clientApp);

        // Główny układ sceny
        VBox layout = (VBox) this.getRoot();
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: rgb(30, 30, 50);");
        layout.getChildren().addAll(playersContainer, gameIDLabel, gameIDField, startButton, backButton);
    }

    /**
     * Tworzy etykietę z określonym tekstem.
     *
     * @param text tekst etykiety
     * @return skonfigurowana etykieta
     */
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        label.setTextFill(Color.WHITE);
        return label;
    }

    /**
     * Tworzy pole tekstowe z podpowiedzią.
     *
     * @param promptText podpowiedź widoczna w polu tekstowym
     * @return skonfigurowane pole tekstowe
     */
    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle(
            "-fx-background-color: rgb(45, 45, 80); " +
            "-fx-text-fill: white; " +
            "-fx-prompt-text-fill: #6A6A80;"
        );
        textField.setMaxWidth(250); // Ustawienie maksymalnej szerokości pola
        return textField;
    }

    /**
     * Tworzy i konfiguruje kontener na dynamiczną listę graczy.
     *
     * @return skonfigurowany kontener VBox
     */
    private VBox createPlayersContainer() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: rgb(30, 30, 50);");
        return container;
    }

    /**
     * Tworzy i konfiguruje przycisk powrotu do ekranu JoinScene.
     *
     * @param clientApp instancja głównej aplikacji klienta
     * @return skonfigurowany przycisk
     */
    private Button createStartButton(ClientApp clientApp) {
        Button startButton = new Button("Start Replay");
        startButton.setStyle(
            "-fx-background-color: rgb(90, 90, 255); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10 20 10 20;" +
            "-fx-pref-width: 160;"
        );

        startButton.setOnAction(event -> {
            // try { 
                String gameID = gameIDField.getText();
                clientApp.ioManager.sendMessageToServer("replay ID " + gameID);
                System.out.println("Send request to start replay with ID: " + gameID);
            // } catch (Exception e) {}
        });
        return startButton;
    }

    /**
     * Tworzy i konfiguruje przycisk powrotu do ekranu JoinScene.
     *
     * @param clientApp instancja głównej aplikacji klienta
     * @return skonfigurowany przycisk
     */
    private Button createBackButton(ClientApp clientApp) {
        Button backButton = new Button("Back to Join");
        backButton.setStyle(
            "-fx-background-color: #FF5A5A; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10 20 10 20;" +
            "-fx-pref-width: 160;"
        );
        backButton.setOnAction(event -> clientApp.showJoinScene());
        return backButton;
    }
}
