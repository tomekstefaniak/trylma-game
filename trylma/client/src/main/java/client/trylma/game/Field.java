package client.trylma.game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class Field extends Circle {

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
    
    public int ownerID; // Identyfikator właściciela pola (0-5 dla graczy, 6 dla neutralnych pól)

    public Field(int radius) {
        super(radius);
    }

    /**
     * Ustawia styl pola na podstawie jego właściciela.
     * Zarządza kolorem wypełnienia, ramką i promieniem pola.
     */
    protected void applyStyle() {
        // Ustaw kolor wypełnienia na podstawie właściciela pola
        this.setFill(Field.COLORS[ownerID]);

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
