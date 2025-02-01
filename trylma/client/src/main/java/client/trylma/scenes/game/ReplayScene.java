package client.trylma.scenes.game;

import client.trylma.ClientApp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Scena gry głównej (GameScene). Zawiera elementy interfejsu użytkownika,
 * takie jak: lista graczy, informacje o turze, plansza gry oraz przyciski "EXIT" i "SKIP".
 */
public class ReplayScene extends Scene implements IGameScene {

    /** Pasek nawigacyjny z przyciskami "EXIT" i "SKIP". */
    private HBox buttonsBar;

    /** Kontener na dynamicznie generowane pola planszy gry. */
    public VBox fieldsGrid;

    /** Etykieta wyświetlająca informację o aktualnym graczu wykonującym ruch. */
    public Label turnLabel;

    /**
     * Konstruktor klasy GameScene. Tworzy główny layout sceny oraz jej elementy.
     *
     * @param players lista graczy wraz z ich identyfikatorami i pseudonimami
     * @param clientApp referencja do głównej klasy aplikacji klienta
     */
    public ReplayScene(ClientApp clientApp) {
        super(new BorderPane(), 750, 750);

        // Główny layout
        BorderPane layout = (BorderPane) this.getRoot();
        layout.setStyle("-fx-background-color: rgb(45, 45, 80);");

        // Tworzenie paska z graczami (topBar1) i przyciskami (topBar2)
        VBox topBarsContainer = new VBox();
        buttonsBar = createButtonsBar(clientApp);

        // Dodanie obu pasków nawigacyjnych do kontenera
        topBarsContainer.getChildren().addAll(buttonsBar);
        layout.setTop(topBarsContainer);

        // Tworzenie planszy gry
        fieldsGrid = createFieldsGrid();
        layout.setCenter(fieldsGrid);
    }

    /**
     * Tworzy pasek nawigacyjny z przyciskami "EXIT" i "SKIP".
     *
     * @param clientApp referencja do głównej klasy aplikacji klienta
     * @return pasek (HBox) z przyciskami
     */
    public HBox createButtonsBar(ClientApp clientApp) {
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
        Button skipButton = new Button("NEXT");
        skipButton.setStyle(
            "-fx-font-size: 14; " +
            "-fx-background-color: rgb(60, 60, 90); " +
            "-fx-text-fill: white; " +
            "-fx-border-color: white; " +
            "-fx-border-width: 1; " +
            "-fx-pref-height: 30; " +
            "-fx-pref-width: 80;"
        );
        skipButton.setOnAction(event -> clientApp.ioManager.sendMessageToServer("replay next"));

        // Dodanie przycisków do paska
        buttonsBar.getChildren().addAll(exitButton, skipButton);

        return buttonsBar;
    }

    /**
     * Tworzy planszę gry (fieldsGrid) w formie siatki pól.
     *
     * @return kontener VBox z siatką pól gry
     */
    public VBox createFieldsGrid() {
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
