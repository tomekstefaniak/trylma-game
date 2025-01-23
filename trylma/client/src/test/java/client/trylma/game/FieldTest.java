package client.trylma.game;

import javafx.scene.paint.Color;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FieldTest {

    @Test
    public void testChangeOwner() {
        // Przygotowanie: Mockowanie GameManager
        GameManager mockGameManager = mock(GameManager.class);

        // Tworzenie instancji pola z początkowym właścicielem (ID = 0)
        Field field = new Field(mockGameManager, 0, 5, 5);

        // Sprawdzenie początkowego koloru pola
        assertEquals(Color.color(250 / 255.0, 245 / 255.0, 255 / 255.0), field.getFill());

        // Akcja: Zmiana właściciela na gracza o ID = 2 (czerwony)
        field.changeOwner(2);

        // Sprawdzenie, czy właściciel został zmieniony
        assertEquals(2, field.ownerID);

        // Sprawdzenie, czy kolor pola zmienił się na czerwony
        assertEquals(Color.color(255 / 255.0, 100 / 255.0, 100 / 255.0), field.getFill());
    }
}
