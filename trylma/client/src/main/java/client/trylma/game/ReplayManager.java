package client.trylma.game;

import client.trylma.io.IOManager;
import client.trylma.scenes.game.ReplayScene;
import javafx.util.Pair;
import java.util.ArrayList;
import javafx.scene.layout.HBox;

/**
 * Klasa zarządzająca stanem gry. Odpowiada za:
 * - inicjalizację planszy gry,
 * - aktualizację planszy na podstawie danych z serwera,
 * - zarządzanie turami graczy.
 */
public class ReplayManager implements IManager {

    public final IOManager ioManager;
    private final ReplayScene replayScene;
    private final ArrayList<Pair<Integer, String>> players;

    /**
     * Konstruktor klasy ReplayManager. Inicjalizuje planszę i ustawia początkowy stan gry.
     *
     * @param replayScene scena gry
     * @param ioManager manager wejścia/wyjścia do komunikacji z serwerem
     * @param variant wariant gry
     * @param turn numer gracza, który rozpoczyna turę
     * @param players lista graczy wraz z ich identyfikatorami
     * @param id identyfikator aktualnego gracza
     * @param board stan planszy w postaci listy wierszy
     */
    public ReplayManager(
        ReplayScene replayScene, 
        IOManager ioManager,
        String variant, 
        int turn, 
        ArrayList<Pair<Integer, String>> players,
        ArrayList<String> board
    ) {
        this.replayScene = replayScene;
        this.ioManager = ioManager;
        this.players = players;

        // Inicjalizacja planszy gry
        initializeBoard(board);

        // Ustawienie początkowego stanu gry
        updateState(turn);
    }

    /**
     * Inicjalizuje planszę gry na podstawie danych z serwera.
     *
     * @param board lista wierszy planszy przesłanych przez serwer
     */
    public void initializeBoard(ArrayList<String> board) {
        for (int row = 0; row < board.size(); row++) {
            HBox rowContainer = (HBox) replayScene.getFieldsGrid().getChildren().get(row);

            for (int col = 0; col < board.get(row).length(); col++) {
                // Odczytanie ID gracza dla danego pola
                int playerID = getPlayerIDFromChar(board.get(row).charAt(col));

                // Utworzenie pola
                ReplayField newField = new ReplayField(this, playerID);
                System.out.println("New field for ID: " + playerID);
                // Dodanie pola do planszy
                rowContainer.getChildren().add(newField);
            }
        }

        // Ustawienie kolorów fieldów
        updateScene(board);
    }

    /**
     * Aktualizuje planszę gry na podstawie nowego stanu przesłanego przez serwer.
     *
     * @param board lista wierszy planszy przesłanych przez serwer
     */
    public void updateScene(ArrayList<String> board) {
        for (int row = 0; row < board.size(); row++) {
            HBox rowContainer = (HBox) replayScene.getFieldsGrid().getChildren().get(row);

            for (int col = 0; col < board.get(row).length(); col++) {
                // Odczytanie ID gracza dla danego pola
                int playerID = getPlayerIDFromChar(board.get(row).charAt(col));

                // Zaktualizowanie właściciela pola
                ReplayField field = (ReplayField) rowContainer.getChildren().get(col);
                field.changeOwner(playerID);
            }
        }
    }

    /**
     * Aktualizuje stan gry, ustawia aktualną turę i widoczność przycisku SKIP.
     *
     * @param turn numer gracza, którego jest teraz tura
     */
    public void updateState(int turn) {
        System.out.println("Now it's player with ID " + turn + "'s turn.");

        // Znajdź pseudonim aktualnego gracza wykonującego ruch
        String currentPlayerNickname = players.stream()
            .filter(pair -> pair.getKey() == turn)
            .map(Pair::getValue)
            .findFirst()
            .orElse("null");

        // Ustaw tekst w etykiecie tury
        replayScene.setTurnLabelText(currentPlayerNickname + "'s turn");
    }

    /**
     * Konwertuje symbol pola (znak) na identyfikator gracza.
     *
     * @param symbol symbol pola (znak z serwera)
     * @return identyfikator gracza
     */
    public int getPlayerIDFromChar(char symbol) {
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
