package client.trylma.game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Klasa reprezentująca pojedyncze pole planszy w grze.
 * Pole jest okręgiem, który może być własnością gracza, neutralnym polem
 * lub wybranym do wykonania ruchu.
 */
public class Field extends Circle {

    private final GameManager gameManager;
    private int x; // Współrzędna X pola
    private int y; // Współrzędna Y pola
    public int ownerID; // Identyfikator właściciela pola (0-5 dla graczy, 6 dla neutralnych pól)

    /**
     * Konstruktor klasy Field. Inicjalizuje pole na planszy.
     *
     * @param gameManager referencja do GameManagera zarządzającego stanem gry
     * @param ownerID identyfikator właściciela pola (0-5 dla graczy, 6 dla neutralnych pól)
     * @param x współrzędna X pola
     * @param y współrzędna Y pola
     */
    public Field(GameManager gameManager, int ownerID, int x, int y) {
        super(14); // Ustawienie promienia pola

        this.gameManager = gameManager;
        this.ownerID = ownerID;
        this.x = x;
        this.y = y;

        // Ustaw początkowy styl pola
        applyStyle();

        // Ustaw handler kliknięcia
        setupClickHandler();
    }

    /**
     * Ustawia styl pola na podstawie jego właściciela.
     * Zarządza kolorem wypełnienia, ramką i promieniem pola.
     */
    private void applyStyle() {
        // Ustaw kolor wypełnienia na podstawie właściciela pola
        this.setFill(GameManager.COLORS[ownerID]);

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
     * Konfiguruje obsługę kliknięcia na polu.
     * Obsługa kliknięcia zależy od aktualnej tury gracza oraz wybranego pola.
     */
    private void setupClickHandler() {
        this.setOnMouseClicked(event -> {
            // Jeśli jest tura gracza i pole należy do gracza
            if (gameManager.canMove && gameManager.thisPlayerID == ownerID && gameManager.moveString.equals("move ")) {
                // Oznacz pole jako wybrane (zmień kolor na różowy)
                this.setFill(Color.color(255 / 255.0, 0 / 255.0, 255 / 255.0));

                // Dodaj współrzędne pola do komunikatu o ruchu
                gameManager.moveString += this.x + "," + this.y + "-";

                // Zapisz wybrane pole
                gameManager.fieldAboutToMove = this;
            }
            // Jeśli jest tura gracza i wybrano już startowe pole
            else if (gameManager.canMove && !gameManager.moveString.equals("move ")) {
                // Skompletuj komunikat o ruchu
                String finalMove = gameManager.moveString + this.x + "," + this.y;

                // Zresetuj dane ruchu
                gameManager.moveString = "move ";
                gameManager.canMove = false;

                // Zresetuj kolor poprzednio wybranego pola
                gameManager.fieldAboutToMove.resetColor();

                // Wyślij ruch do serwera
                gameManager.ioManager.sendMessageToServer(finalMove);
                System.out.println(finalMove);
            }
        });
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
