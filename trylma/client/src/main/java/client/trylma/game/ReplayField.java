package client.trylma.game;


/**
 * Klasa reprezentująca pojedyncze pole planszy w grze.
 * Pole jest okręgiem, który może być własnością gracza, neutralnym polem
 * lub wybranym do wykonania ruchu.
 */
public class ReplayField extends Field {

    public int ownerID; // Identyfikator właściciela pola (0-5 dla graczy, 6 dla neutralnych pól)

    /**
     * Konstruktor klasy Field. Inicjalizuje pole na planszy.
     *
     * @param ownerID identyfikator właściciela pola (0-5 dla graczy, 6 dla neutralnych pól)
     * @param x współrzędna X pola
     * @param y współrzędna Y pola
     */
    public ReplayField(ReplayManager replayManager, int ownerID) {
        super(14); // Ustawienie promienia pola

        this.ownerID = ownerID;

        // Ustaw początkowy styl pola
        applyStyle();
    }
}
