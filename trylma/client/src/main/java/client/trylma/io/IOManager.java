package client.trylma.io;

import client.trylma.ClientApp;

import java.net.*;
import java.util.List;
import javafx.application.Platform;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;

/**
 * Klasa pośrednicząca między ServerIOHandler a resztą aplikacji.
 * Jest swojego rodzaju obustronnym interfejsem, umożliwiającym
 * komunikację między klientem a serwerem.
 */
public class IOManager {

    private final ClientApp clientApp;
    private ServerIOHandler serverIOHandler;

    /**
     * Konstruktor klasy IOManager.
     *
     * @param clientApp instancja głównej aplikacji klienta
     */
    public IOManager(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

    /**
     * Rozpoczyna komunikację z serwerem, tworząc instancję ServerIOHandler.
     *
     * @param port numer portu serwera
     * @param nickname pseudonim gracza
     * @throws IOException w przypadku problemów z połączeniem
     */
    public void joinLobby(int port, String nickname) throws IOException {
        Socket socket = new Socket("localhost", port);
        System.out.println("Connected to lobby (server) on port: " + port);

        // Tworzenie instancji ServerIOHandler
        serverIOHandler = new ServerIOHandler(this, socket);

        // Ustawienie wątku jako demona
        serverIOHandler.setDaemon(true);
        serverIOHandler.start();

        // Wyświetlenie sceny lobby w interfejsie użytkownika
        clientApp.showLobbyScene();

        // Wysłanie pseudonimu gracza do serwera
        try {
            serverIOHandler.sendMessageToServer(nickname);
        } catch (NullPointerException e) {
            System.err.println("Error while sending nickname: " + e.getMessage());
            leaveServer();
        }
    }

    /**
     * Aktualizuje listę graczy w lobby.
     *
     * @param responseParsed lista graczy otrzymana z serwera
     */
    public void updateLobbyPlayersList(List<String> responseParsed) {
        Platform.runLater(() -> {
            if (clientApp.clientState == ClientApp.ClientStates.LOBBY) {
                clientApp.lobbyScene.updateLobbyPlayersList(responseParsed);
            }
        });
    }

    /**
     * Inicjalizuje grę na podstawie danych otrzymanych z serwera.
     *
     * @param variantString dane dotyczące wariantu gry
     * @param turnString dane dotyczące aktualnej tury
     * @param boardString dane dotyczące planszy
     * @param idString identyfikator gracza
     * @param playersString lista graczy
     */
    public void startGame(String variantString, String turnString, String boardString, String idString, String playersString) {
        try {
            String variant = extractValue(variantString);
            int turn = Integer.parseInt(extractValue(turnString));
            ArrayList<String> board = new ArrayList<>(Arrays.asList(extractValue(boardString).split("/")));
            int id = Integer.parseInt(extractValue(idString));
            ArrayList<Pair<Integer, String>> players = parsePlayers(playersString);

            Platform.runLater(() -> clientApp.showGameScene(variant, turn, players, id, board));
        } catch (Exception e) {
            System.err.println("Error while starting the game: " + e.getMessage());
            leaveServer();
        }
    }

    /**
     * Aktualizuje stan gry na podstawie danych z serwera.
     *
     * @param turnString dane dotyczące aktualnej tury
     * @param boardString dane dotyczące planszy
     */
    public void updateGame(String turnString, String boardString) {
        try {
            int turn = Integer.parseInt(extractValue(turnString));
            ArrayList<String> board = new ArrayList<>(Arrays.asList(extractValue(boardString).split("/")));

            Platform.runLater(() -> clientApp.updateGame(turn, board));
        } catch (Exception e) {
            System.err.println("Error while updating the game: " + e.getMessage());
            leaveServer();
        }
    }

    /**
     * Powtarza ruch w przypadku błędu.
     */
    public void repeatMove() {
        System.out.println("Move invalid. Please try again.");
        clientApp.gameManager.canMove = true;
    }

    /**
     * Wysyła wiadomość do serwera.
     *
     * @param message wiadomość do wysłania
     */
    public void sendMessageToServer(String message) {
        if (serverIOHandler != null) {
            serverIOHandler.sendMessageToServer(message);
        } else {
            System.err.println("ServerIOHandler is not initialized.");
        }
    }

    /**
     * Zatrzymuje połączenie z serwerem.
     */
    public void stopConnection() {
        if (serverIOHandler != null) {
            serverIOHandler.stopConnection();
            serverIOHandler = null;
        }
    }

    /**
     * Opuść serwer i pokaż ekran łączenia.
     */
    public void leaveServer() {
        serverIOHandler = null;
        Platform.runLater(() -> clientApp.showJoinScene());
    }

    /**
     * Parsuje dane graczy z ciągu znaków.
     *
     * @param playersString dane graczy w formacie "id1:nickname1 id2:nickname2"
     * @return lista graczy jako pary <id, nickname>
     */
    private ArrayList<Pair<Integer, String>> parsePlayers(String playersString) {
        ArrayList<Pair<Integer, String>> players = new ArrayList<>();
        for (String playerString : playersString.trim().split(" ")) {
            int id = Integer.parseInt(playerString.substring(0, playerString.indexOf(':')));
            String nickname = playerString.substring(playerString.indexOf(':') + 1);
            players.add(new Pair<>(id, nickname));
        }
        return players;
    }

    /**
     * Wyodrębnia wartość z ciągu znaków w formacie "klucz wartość".
     *
     * @param data ciąg znaków w formacie "klucz wartość"
     * @return wyodrębniona wartość
     */
    private String extractValue(String data) {
        return Arrays.asList(data.split("\\s+")).get(1);
    }
}
