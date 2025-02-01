package client.trylma.io;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

/**
 * Klasa odpowiedzialna za interpretowanie odpowiedzi z serwera
 * i przekazywanie ich do odpowiednich komponentów klienta.
 */
public class ServerResponseInterpreter {

    private final ServerIOHandler serverIOHandler;
    private final IOManager ioManager;

    /**
     * Konstruktor klasy
     *
     * @param serverIOHandler obiekt obsługujący komunikację z serwerem
     * @param ioManager menedżer odpowiedzialny za interakcję z użytkownikiem
     */
    public ServerResponseInterpreter(ServerIOHandler serverIOHandler, IOManager ioManager) {
        this.serverIOHandler = serverIOHandler;
        this.ioManager = ioManager;
    }

    /**
     * Interpretuje odpowiedź serwera i podejmuje odpowiednie działania.
     *
     * @param serverResponse odpowiedź od serwera w postaci tekstu
     * @throws IOException w przypadku problemów z wejściem/wyjściem
     */
    public void interpret(String serverResponse) throws IOException, IndexOutOfBoundsException {
        // Podziel string po spacji i usuń puste elementy
        List<String> responseParsed = Arrays.stream(serverResponse.split("\\s+"))
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());

        // Obsługa różnych typów wiadomości od serwera
        switch (responseParsed.get(0)) {
            case "disconnect":
                System.out.println("Server told us to disconnect");
                ioManager.leaveServer();
                serverIOHandler.running = false; // Kończymy wątek
                break;

            case "mode":
                ioManager.gotModeFromLobby(responseParsed.get(1));

            case "replay":
                switch (responseParsed.get(1)) {
                    case "started":
                        // Gra rozpoczęła się: wczytanie danych początkowych gry
                        ioManager.startReplay(
                            serverIOHandler.in.readLine() // plansza
                        );
                        break;
                    case "next":
                        // Aktualizacja planszy i tury
                        ioManager.updateReplay(
                            serverIOHandler.in.readLine()  // Nowy stan planszy
                        );
                        break;
                }

            case "lobby":
                // Aktualizacja listy graczy w lobby
                ioManager.updateLobbyPlayersList(responseParsed.subList(1, responseParsed.size()));
                break;

            case "started":
                // Gra rozpoczęła się: wczytanie danych początkowych gry
                ioManager.startGame(
                    serverIOHandler.in.readLine(), // wariant gry
                    serverIOHandler.in.readLine(), // kto zaczyna
                    serverIOHandler.in.readLine(), // plansza
                    serverIOHandler.in.readLine(), // ID gracza
                    serverIOHandler.in.readLine()  // lista graczy
                );
                break;

            case "next":
                // Aktualizacja planszy i tury
                ioManager.updateGame(
                    serverIOHandler.in.readLine(), // kto teraz wykonuje ruch
                    serverIOHandler.in.readLine()  // stan planszy
                );
                break;

            case "ended":
                // Gra zakończona
                ioManager.leaveServer();
                serverIOHandler.running = false;
                break;

            case "[ALL]":
                if (responseParsed.size() > 1 &&
                        (responseParsed.get(1).equals("moved") || responseParsed.get(1).equals("skipped"))) {
                    ioManager.updateGame(
                        serverIOHandler.in.readLine(), // kto teraz wykonuje ruch
                        serverIOHandler.in.readLine()  // stan planszy
                    );
                }
                break;

            default:
                // Obsługa błędów związanych z ruchem gracza
                if (serverResponse.matches("GameEngine.move: invalid field|invalid starting field|invalid end field|no piece on starting field|wrong piece on starting field|other piece on end field|illegal move|invalid field")) {
                    ioManager.repeatMove();
                }
        }
    }
}
