package client.trylma.game;

import java.util.ArrayList;

public interface IManager {

    /**
     * Inicjalizuje planszę gry na podstawie danych z serwera.
     *
     * @param board lista wierszy planszy przesłanych przez serwer
     */
    void initializeBoard(ArrayList<String> board);

    /**
     * Aktualizuje planszę gry na podstawie nowego stanu przesłanego przez serwer.
     *
     * @param board lista wierszy planszy przesłanych przez serwer
     */
    void updateScene(ArrayList<String> board);

    /**
     * Aktualizuje stan gry, ustawia aktualną turę i widoczność przycisku SKIP.
     *
     * @param turn numer gracza, którego jest teraz tura
     */
    void updateState(int turn);

    /**
     * Konwertuje symbol pola (znak) na identyfikator gracza.
     *
     * @param symbol symbol pola (znak z serwera)
     * @return identyfikator gracza
     */
    int getPlayerIDFromChar(char symbol);
}
