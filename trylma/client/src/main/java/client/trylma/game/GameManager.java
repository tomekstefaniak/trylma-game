package client.trylma.game;

import client.trylma.io.IOManager;
import client.trylma.scenes.GameScene;

import javafx.scene.paint.Color;
import javafx.util.Pair;
import java.util.ArrayList;
import javafx.scene.layout.HBox;

/**
 * Klasa zarządzająca stanem gry. Odpowiada za:
 * - inicjalizację planszy gry,
 * - aktualizację planszy na podstawie danych z serwera,
 * - zarządzanie turami graczy.
 */
public class GameManager {

    /** Kolory przypisane polom graczy i planszy. */
    public static final Color[] COLORS = {
        Color.color(250 / 255.0, 245 / 255.0, 255 / 255.0), // 0, white
        Color.color(25 / 255.0, 25 / 255.0, 40 / 255.0), // 1, black
        Color.color(255 / 255.0, 100 / 255.0, 100 / 255.0), // 2, red
        Color.color(100 / 255.0, 180 / 255.0, 255 / 255.0), // 3, blue
        Color.color(55 / 255.0, 150 / 255.0, 0 / 255.0), // 4, green
        Color.color(255 / 255.0, 255 / 255.0, 0 / 255.0), // 5, yellow
        Color.color(250 / 255.0, 210 / 255.0, 150 / 255.0) // 6, neutral field
    };

    /** Lista wcięć pól na planszy, odpowiadających każdemu wierszowi. */
    private static final int[] FIELDS_INDENTS = {
        6, 5, 5, 4, 0, 0, 1, 1,
        2, // środek planszy
        1, 1, 0, 0, 4, 5, 5, 6
    };

    private final GameScene gameScene;
    public final IOManager ioManager;
    private final ArrayList<Pair<Integer, String>> players;

    public final int thisPlayerID;

    public boolean canMove;
    public volatile String moveString;
    public Field fieldAboutToMove;

    /**
     * Konstruktor klasy GameManager. Inicjalizuje planszę i ustawia początkowy stan gry.
     *
     * @param gameScene scena gry
     * @param ioManager manager wejścia/wyjścia do komunikacji z serwerem
     * @param variant wariant gry
     * @param turn numer gracza, który rozpoczyna turę
     * @param players lista graczy wraz z ich identyfikatorami
     * @param id identyfikator aktualnego gracza
     * @param board stan planszy w postaci listy wierszy
     */
    public GameManager(
        GameScene gameScene, 
        IOManager ioManager,
        String variant, 
        int turn, 
        ArrayList<Pair<Integer, String>> players, 
        int id, 
        ArrayList<String> board
    ) {
        this.gameScene = gameScene;
        this.ioManager = ioManager;
        this.thisPlayerID = id;
        this.players = players;

        this.moveString = "move ";

        // Inicjalizacja planszy gry
        initializeBoard(board);

        // Ustawienie początkowego stanu gry
        updateGameState(turn);
    }

    /**
     * Inicjalizuje planszę gry na podstawie danych z serwera.
     *
     * @param board lista wierszy planszy przesłanych przez serwer
     */
    private void initializeBoard(ArrayList<String> board) {
        for (int row = 0; row < board.size(); row++) {
            HBox rowContainer = (HBox) gameScene.getFieldsGrid().getChildren().get(row);

            for (int col = 0; col < board.get(row).length(); col++) {
                // Odczytanie ID gracza dla danego pola
                int playerID = getPlayerIDFromChar(board.get(row).charAt(col));

                // Wyznaczenie współrzędnych pola
                int x = row;
                int y = FIELDS_INDENTS[row] + col;

                // Utworzenie pola
                Field newField = new Field(this, playerID, x, y);

                // Dodanie pola do planszy
                rowContainer.getChildren().add(newField);
            }
        }
    }

    /**
     * Aktualizuje planszę gry na podstawie nowego stanu przesłanego przez serwer.
     *
     * @param board lista wierszy planszy przesłanych przez serwer
     */
    public void updateGameScene(ArrayList<String> board) {
        for (int row = 0; row < board.size(); row++) {
            HBox rowContainer = (HBox) gameScene.getFieldsGrid().getChildren().get(row);

            for (int col = 0; col < board.get(row).length(); col++) {
                // Odczytanie ID gracza dla danego pola
                int playerID = getPlayerIDFromChar(board.get(row).charAt(col));

                // Zaktualizowanie właściciela pola
                Field field = (Field) rowContainer.getChildren().get(col);
                field.changeOwner(playerID);
            }
        }
    }

    /**
     * Aktualizuje stan gry, ustawia aktualną turę i widoczność przycisku SKIP.
     *
     * @param turn numer gracza, którego jest teraz tura
     */
    public void updateGameState(int turn) {
        System.out.println("You are player with ID " + thisPlayerID + ". Now it's player with ID " + turn + "'s turn.");

        // Znajdź pseudonim aktualnego gracza wykonującego ruch
        String currentPlayerNickname = players.stream()
            .filter(pair -> pair.getKey() == turn)
            .map(Pair::getValue)
            .findFirst()
            .orElse("null");

        // Ustaw tekst w etykiecie tury
        gameScene.setTurnLabelText(currentPlayerNickname + "'s turn");

        // Sprawdź, czy aktualna tura należy do gracza
        if (turn == thisPlayerID) {
            canMove = true;
            gameScene.skipButton.setVisible(true);
        } else {
            canMove = false;
            gameScene.skipButton.setVisible(false);
        }
    }

    /**
     * Konwertuje symbol pola (znak) na identyfikator gracza.
     *
     * @param symbol symbol pola (znak z serwera)
     * @return identyfikator gracza
     */
    private int getPlayerIDFromChar(char symbol) {
        switch (symbol) {
            case '0': return 0;
            case '1': return 1;
            case '2': return 2;
            case '3': return 3;
            case '4': return 4;
            case '5': return 5;
            default: return 6; // Neutral field
        }
    }
}
