package client.trylma.scenes;

import client.trylma.ClientApp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Scena gry głównej (GameScene). Zawiera elementy interfejsu użytkownika,
 * takie jak: lista graczy, informacje o turze, plansza gry oraz przyciski "EXIT" i "SKIP".
 */
public class GameScene extends Scene {

    /** Pasek nawigacyjny z informacjami o graczach i aktualnej turze. */
    private HBox topBar1;

    /** Pasek nawigacyjny z przyciskami "EXIT" i "SKIP". */
    private HBox topBar2;

    /** Kontener na dynamicznie generowane pola planszy gry. */
    public VBox fieldsGrid;

    /** Etykieta wyświetlająca informację o aktualnym graczu wykonującym ruch. */
    public Label turnLabel;

    /** Przycisk pozwalający graczowi pominąć swoją turę. */
    public Button skipButton;

    /** Kolory przypisane graczom w grze. */
    private static final String[] COLORS = {
        "WHITE", "BLACK", "RED", "BLUE", "GREEN", "YELLOW"
    };

    /**
     * Konstruktor klasy GameScene. Tworzy główny layout sceny oraz jej elementy.
     *
     * @param players lista graczy wraz z ich identyfikatorami i pseudonimami
     * @param clientApp referencja do głównej klasy aplikacji klienta
     */
    public GameScene(ArrayList<Pair<Integer, String>> players, ClientApp clientApp) {
        super(new BorderPane(), 750, 750);

        // Główny layout
        BorderPane layout = (BorderPane) this.getRoot();
        layout.setStyle("-fx-background-color: rgb(45, 45, 80);");

        // Tworzenie paska z graczami (topBar1) i przyciskami (topBar2)
        VBox topBarsContainer = new VBox();
        topBar1 = createPlayersBar(players);
        topBar2 = createButtonsBar(clientApp);

        // Dodanie obu pasków nawigacyjnych do kontenera
        topBarsContainer.getChildren().addAll(topBar1, topBar2);
        layout.setTop(topBarsContainer);

        // Tworzenie planszy gry
        fieldsGrid = createFieldsGrid();
        layout.setCenter(fieldsGrid);
    }

    /**
     * Tworzy pasek nawigacyjny z listą graczy i ich kolorami.
     *
     * @param players lista graczy z ich identyfikatorami i pseudonimami
     * @return pasek (HBox) z informacjami o graczach
     */
    private HBox createPlayersBar(ArrayList<Pair<Integer, String>> players) {
        HBox playersBar = new HBox();
        playersBar.setSpacing(15);
        playersBar.setAlignment(Pos.CENTER);
        playersBar.setStyle("-fx-background-color: rgb(30, 30, 50); -fx-padding: 10; -fx-min-height: 50;");

        // Kontener na informacje o graczach
        HBox playersContainer = new HBox();
        playersContainer.setSpacing(20);
        playersContainer.setAlignment(Pos.CENTER);

        // Dodanie etykiet graczy z ich kolorami
        for (Pair<Integer, String> player : players) {
            Label playerLabel = new Label(player.getValue() + ": " + COLORS[player.getKey()]);
            playerLabel.setStyle("-fx-font-size: 12; -fx-text-fill: white;");
            playersContainer.getChildren().add(playerLabel);
        }

        // Etykieta wyświetlająca aktualną turę
        turnLabel = new Label("Player's Turn");
        turnLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #00ff00;");
        turnLabel.setAlignment(Pos.CENTER_RIGHT);

        // Dodanie elementów do głównego paska graczy
        playersBar.getChildren().addAll(playersContainer, turnLabel);
        HBox.setHgrow(turnLabel, javafx.scene.layout.Priority.ALWAYS);

        return playersBar;
    }

    /**
     * Tworzy pasek nawigacyjny z przyciskami "EXIT" i "SKIP".
     *
     * @param clientApp referencja do głównej klasy aplikacji klienta
     * @return pasek (HBox) z przyciskami
     */
    private HBox createButtonsBar(ClientApp clientApp) {
        HBox buttonsBar = new HBox();
        buttonsBar.setAlignment(Pos.CENTER);
        buttonsBar.setSpacing(10);
        buttonsBar.setPadding(new Insets(0, 10, 0, 10));
        buttonsBar.setStyle("-fx-background-color: rgb(40, 40, 60); -fx-padding: 10; -fx-min-height: 50;");

        // Przycisk EXIT
        Button exitButton = new Button("EXIT");
        exitButton.setStyle(
            "-fx-font-size: 14; " +
            "-fx-background-color: rgb(90, 30, 30); " +
            "-fx-text-fill: white; " +
            "-fx-border-color: white; " +
            "-fx-border-width: 1; " +
            "-fx-pref-height: 30; " +
            "-fx-pref-width: 80;"
        );
        exitButton.setOnAction(event -> clientApp.showJoinScene());

        // Przycisk SKIP
        skipButton = new Button("SKIP");
        skipButton.setStyle(
            "-fx-font-size: 14; " +
            "-fx-background-color: rgb(60, 60, 90); " +
            "-fx-text-fill: white; " +
            "-fx-border-color: white; " +
            "-fx-border-width: 1; " +
            "-fx-pref-height: 30; " +
            "-fx-pref-width: 80;"
        );
        skipButton.setOnAction(event -> clientApp.ioManager.sendMessageToServer("skip"));

        // Dodanie przycisków do paska
        buttonsBar.getChildren().addAll(exitButton, skipButton);

        return buttonsBar;
    }

    /**
     * Tworzy planszę gry (fieldsGrid) w formie siatki pól.
     *
     * @return kontener VBox z siatką pól gry
     */
    private VBox createFieldsGrid() {
        VBox grid = new VBox();
        grid.setAlignment(Pos.CENTER);
        grid.setSpacing(9); // Odstępy między wierszami

        for (int i = 0; i < 17; i++) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER);
            row.setSpacing(13); // Odstępy między kolumnami w wierszu

            // Dodaj rząd do siatki
            grid.getChildren().add(row);
        }

        return grid;
    }

    /**
     * Zwraca kontener na siatkę pól planszy gry.
     *
     * @return kontener VBox z planszą gry
     */
    public VBox getFieldsGrid() {
        return fieldsGrid;
    }

    /**
     * Ustawia tekst etykiety aktualnej tury.
     *
     * @param text tekst do wyświetlenia na etykiecie
     */
    public void setTurnLabelText(String text) {
        turnLabel.setText(text);
    }
}
