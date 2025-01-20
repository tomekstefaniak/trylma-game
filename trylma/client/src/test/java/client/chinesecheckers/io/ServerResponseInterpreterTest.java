package client.chinesecheckers.io;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ServerResponseInterpreterTest {

    @Test
    public void testInterpretDisconnect() throws IOException {
        // Przygotowanie: Mockowanie zależności
        ServerIOHandler mockServerIOHandler = mock(ServerIOHandler.class);
        IOManager mockIOManager = mock(IOManager.class);

        // Tworzymy obiekt klasy ServerResponseInterpreter
        ServerResponseInterpreter interpreter = new ServerResponseInterpreter(mockServerIOHandler, mockIOManager);

        // Akcja: Interpretacja wiadomości "disconnect"
        interpreter.interpret("disconnect");

        // Sprawdzenie: Czy odpowiednie metody zostały wywołane?
        verify(mockIOManager, times(1)).leaveServer(); // Sprawdza, czy leaveServer() został wywołany raz
        assertFalse(mockServerIOHandler.running); // Flaga running powinna być ustawiona na false
    }
}
