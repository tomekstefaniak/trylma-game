package client.trylma.scenes;

import client.trylma.ClientApp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Scena lobby, wyświetlająca listę graczy oraz przycisk powrotu do ekranu JoinScene.
 */
public class LobbyScene extends Scene {

    /** Kontener na dynamicznie dodawane etykiety reprezentujące listę graczy. */
    private final VBox playersContainer;

    /**
     * Konstruktor klasy LobbyScene.
     *
     * @param clientApp instancja głównej aplikacji klienta
     */
    public LobbyScene(ClientApp clientApp) {
        super(new VBox(20), 750, 750);

        // Inicjalizacja kontenera na graczy
        playersContainer = createPlayersContainer();

        // Utworzenie przycisków od zarządzania botami
        Button addBotButton = createAddBotButton(clientApp);
        Button removeBotButton = createRemoveBotButton(clientApp);

        // Tworzenie przycisku powrotu
        Button backButton = createBackButton(clientApp);

        // Główny układ sceny
        VBox layout = (VBox) this.getRoot();
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: rgb(30, 30, 50);");
        layout.getChildren().addAll(playersContainer, addBotButton, removeBotButton, backButton);
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
    private Button createAddBotButton(ClientApp clientApp) {
        Button backButton = new Button("Add Bot");
        backButton.setStyle(
            "-fx-background-color: rgb(90, 90, 255); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10 20 10 20;" +
            "-fx-pref-width: 100;"
        );
        backButton.setOnAction(event -> clientApp.ioManager.sendMessageToServer("bot add"));
        return backButton;
    }

    /**
     * Wysyła prosbę o usunięcie bota do serwera
     *
     * @param clientApp instancja głównej aplikacji klienta
     * @return skonfigurowany przycisk
     */
    private Button createRemoveBotButton(ClientApp clientApp) {
        Button backButton = new Button("Remove Bot");
        backButton.setStyle(
            "-fx-background-color: rgb(90, 90, 255); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10 20 10 20;" +
            "-fx-pref-width: 100;"
        );
        backButton.setOnAction(event -> clientApp.ioManager.sendMessageToServer("bot remove"));
        return backButton;
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
            "-fx-pref-width: 100;"
        );
        backButton.setOnAction(event -> clientApp.showJoinScene());
        return backButton;
    }

    /**
     * Aktualizuje listę graczy w lobby, dynamicznie dodając etykiety (Label)
     * do kontenera playersContainer. Jeśli lista jest pusta, wyświetla komunikat "No players in the lobby".
     *
     * @param newPlayersList nowa lista graczy do wyświetlenia
     */
    public void updateLobbyPlayersList(List<String> newPlayersList) {
        // Wyczyść istniejące etykiety w kontenerze
        playersContainer.getChildren().clear();

        if (newPlayersList == null || newPlayersList.isEmpty()) {
            // Wyświetl komunikat w przypadku braku graczy
            Label noPlayersLabel = new Label("No players in the lobby");
            noPlayersLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            playersContainer.getChildren().add(noPlayersLabel);
        } else {
            // Dodaj nowe etykiety na podstawie listy graczy
            for (String player : newPlayersList) {
                Label playerLabel = createPlayerLabel(player);
                playersContainer.getChildren().add(playerLabel);
            }
        }
    }

    /**
     * Tworzy etykietę dla pojedynczego gracza.
     *
     * @param player nazwa gracza
     * @return skonfigurowana etykieta
     */
    private Label createPlayerLabel(String player) {
        Label playerLabel = new Label(player);
        playerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        return playerLabel;
    }
}
