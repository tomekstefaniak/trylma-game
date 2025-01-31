package client.trylma.io;

import java.net.*;
import java.io.*;

/**
 * Klasa odpowiedzialna za obsługę komunikacji wejścia i wyjścia
 * z serwerem w osobnym wątku.
 */
public class ServerIOHandler extends Thread {

    private final IOManager ioManager;
    private final Socket socket;
    private ServerResponseInterpreter interpreter;

    public BufferedReader in;
    private PrintWriter out;
    public volatile boolean running; // Flaga kontrolująca działanie wątku

    /**
     * Konstruktor klasy.
     *
     * @param ioManager obiekt zarządzający wejściem/wyjściem gracza
     * @param socket gniazdo do komunikacji z serwerem
     */
    public ServerIOHandler(IOManager ioManager, Socket socket) {
        this.ioManager = ioManager;
        this.socket = socket;
        this.running = true;

        try {
            // Inicjalizacja strumieni wejścia/wyjścia
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error during communication initialization: " + e.getMessage());
        }
    }

    /**
     * Główna metoda wątku obsługująca odbieranie wiadomości z serwera.
     */
    @Override
    public void run() {
        try {
            interpreter = new ServerResponseInterpreter(this, ioManager);

            String serverResponse;
            while (running) {
                serverResponse = in.readLine();

                if (serverResponse == null) {
                    System.out.println("Server has been shutdown");
                    ioManager.leaveServer();
                    break; // Wyjście z pętli
                }

                System.out.println("server > " + serverResponse);
                interpreter.interpret(serverResponse);
            }
        } catch (SocketException e) {
            System.out.println("Socket closed");
        } catch (IOException e) {
            System.err.println("Error during communication: " + e.getMessage());
        } finally {
            stopConnection();
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
            System.out.println("Połączenie zostało zamknięte.");
        } catch (IOException e) {
            System.err.println("Błąd przy zamykaniu połączenia: " + e.getMessage());
        }
    }
}
