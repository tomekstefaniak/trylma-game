package server.trylma.bot;

import java.net.*;
import java.io.*;

/**
 * Klasa odpowiedzialna za obsługę komunikacji wejścia i wyjścia z serwerem.
 */
public class ServerIOHandler extends Thread {

    private final BotBrain botBrain;
    private final Socket socket;
    private ServerResponseInterpreter interpreter;

    public BufferedReader in;
    private PrintWriter out;
    public volatile boolean running; // Flaga kontrolująca działanie wątku

    /**
     * Konstruktor klasy.
     *
     * @param BotBrain obiekt zarządzający wejściem/wyjściem bota
     * @param socket gniazdo do komunikacji z serwerem
     */
    public ServerIOHandler(Socket socket) {
        this.botBrain = new BotBrain(this);
        this.socket = socket;
        this.running = true;

        try {
            // Inicjalizacja strumieni wejścia/wyjścia
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Błąd podczas inicjalizacji komunikacji: " + e.getMessage());
        }
    }

    /**
     * Główna metoda wątku obsługująca odbieranie wiadomości z serwera.
     */
    @Override
    public void run() {
        try {
            interpreter = new ServerResponseInterpreter(this, botBrain);

            String serverResponse;
            while (running) {
                serverResponse = in.readLine();

                System.out.println("server > " + serverResponse);
                interpreter.interpret(serverResponse);
            }
        } catch (SocketException e) {
            System.out.println("BOT ERROR: Socket zamknięty.");
        } catch (IOException e) {
            System.err.println("BOT ERROR: Błąd podczas komunikacji: " + e.getMessage());
        }
    }

    /**
     * Wysyła wiadomość do serwera.
     *
     * @param message tekst wiadomości
     */
    public void sendMessageToServer(String message) {
        out.println(message);
    }

    /**
     * Zatrzymuje połączenie z serwerem i zamyka strumienie.
     */
    public void stopConnection() {
        running = false;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("BOT ERROR: Błąd przy zamykaniu połączenia: " + e.getMessage());
        }
    }

    /**
     * W kontrolowany sposób zamyka otworzone IO oraz socket z serwerem
     */
    protected void finalize() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("BOT ERROR: Błąd przy zamykaniu połączenia: " + e.getMessage());
        }
    }
}
