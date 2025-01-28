package client.trylma.game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Klasa reprezentująca pojedyncze pole planszy w grze.
 * Pole jest okręgiem, który może być własnością gracza, neutralnym polem
 * lub wybranym do wykonania ruchu.
 */
public class ReplayField extends Circle {

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

    /**
     * Ustawia styl pola na podstawie jego właściciela.
     * Zarządza kolorem wypełnienia, ramką i promieniem pola.
     */
    private void applyStyle() {
        // Ustaw kolor wypełnienia na podstawie właściciela pola
        this.setFill(ReplayManager.COLORS[ownerID]);

        // Jeśli właścicielem jest czarny gracz (ID = 1), ustaw ramkę
        if (ownerID == 1) {
            this.setRadius(13); 
            this.setStroke(Color.color(1.0, 1.0, 1.0));
            this.setStrokeWidth(2);
        } else {
            // Usuń ramkę dla pozostałych właścicieli
            this.setRadius(14);
            this.setStroke(Color.TRANSPARENT);
            this.setStrokeWidth(0);
        }
    }

    /**
     * Zmienia właściciela pola i aktualizuje jego styl.
     *
     * @param newOwnerID identyfikator nowego właściciela pola
     */
    public void changeOwner(int newOwnerID) {
        this.ownerID = newOwnerID;

        // Zastosuj nowy styl dla pola
        applyStyle();
    }

    /**
     * Resetuje kolor pola do stanu odpowiadającego jego właścicielowi.
     */
    public void resetColor() {
        applyStyle();
    }
}
