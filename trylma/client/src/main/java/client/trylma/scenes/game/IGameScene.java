package client.trylma.scenes.game;

import javafx.scene.layout.VBox;

public interface IGameScene {
    
    /** Kolory przypisane graczom w grze. */
    static final String[] COLORS = {
        "WHITE", "BLACK", "RED", "BLUE", "GREEN", "YELLOW"
    };

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
