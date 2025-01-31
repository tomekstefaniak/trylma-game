package client.trylma.scenes.game;

import java.util.ArrayList;

import client.trylma.ClientApp;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public interface IGameScene {
    
    /** Kolory przypisane graczom w grze. */
    static final String[] COLORS = {
        "WHITE", "BLACK", "RED", "BLUE", "GREEN", "YELLOW"
    };

    /**
     * Tworzy pasek nawigacyjny z listą graczy i ich kolorami.
     *
     * @param players lista graczy z ich identyfikatorami i pseudonimami
     * @return pasek (HBox) z informacjami o graczach
     */
    HBox createPlayersBar(ArrayList<Pair<Integer, String>> players);

    /**
     * Tworzy pasek nawigacyjny z przyciskami "EXIT" i "SKIP".
     *
     * @param clientApp referencja do głównej klasy aplikacji klienta
     * @return pasek (HBox) z przyciskami
     */
    HBox createButtonsBar(ClientApp clientApp);

    /**
     * Tworzy planszę gry (fieldsGrid) w formie siatki pól.
     *
     * @return kontener VBox z siatką pól gry
     */
    VBox createFieldsGrid();

    /**
     * Zwraca kontener na siatkę pól planszy gry.
     *
     * @return kontener VBox z planszą gry
     */
    public VBox getFieldsGrid();

    /**
     * Ustawia tekst etykiety aktualnej tury.
     *
     * @param text tekst do wyświetlenia na etykiecie
     */
    public void setTurnLabelText(String text);
}
