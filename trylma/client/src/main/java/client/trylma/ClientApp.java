package client.trylma;

import client.trylma.scenes.*;
import client.trylma.game.GameManager;
import client.trylma.io.IOManager;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 * Główna klasa aplikacji klienta, rozszerzająca klasę Application z JavaFX
 * Zarządza cyklem życia aplikacji, scenami oraz przepływem interfejsu użytkownika
 */
public class ClientApp extends Application {

    /**
     * Stany aplikacji reprezentujące aktualny ekran:
     * JOIN - ekran łączenia z serwerem,
     * LOBBY - lobby z listą graczy,
     * GAME - aktywna gra
     */
    public static enum ClientStates {
        JOIN, LOBBY, GAME, REPLAY
    }

    /** Domyślny port serwera */
    public static String defaultPort = "6000";

    /** Aktualny stan aplikacji */
    public ClientStates clientState;

    /** Manager gry - kontroluje logikę gry */
    public GameManager gameManager;

    /** Manager komunikacji z serwerem (wejście/wyjście) */
    public IOManager ioManager;

    /** Główne okno aplikacji JavaFX */
    private Stage primaryStage;

    /** Scena łączenia z serwerem */
    public JoinScene joinScene;

    /** Scena lobby */
    public LobbyScene lobbyScene;

    /** Scena gry */
    public GameScene gameScene;

     /** Scena powtórki */
     public ReplayScene replayScene;

    /**
     * Główna metoda startowa JavaFX, wywoływana po uruchomieniu aplikacji.
     * Inicjalizuje główne okno, sceny oraz manager I/O.
     *
     * @param primaryStage główna scena aplikacji JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Trylma");

        // Ustawienie ikony aplikacji
        Image icon = new Image(getClass().getResourceAsStream("/icons/icon.png"));
        primaryStage.getIcons().add(icon);

        // Inicjalizacja managera komunikacji
        ioManager = new IOManager(this);

        // Inicjalizacja sceny łączenia z serwerem
        joinScene = new JoinScene(this);
        showJoinScene();

        // Wyświetlenie głównego okna aplikacji
        primaryStage.show();
    }

    /**
     * Ustawia scenę łączenia z serwerem.
     * Jeśli użytkownik był połączony z serwerem (stan LOBBY lub GAME),
     * następuje zamknięcie połączenia i wyczyszczenie odpowiednich zasobów.
     */
    public void showJoinScene() {
        if (clientState != null) {
            // Zamknij połączenie z serwerem, jeśli było aktywne
            ioManager.stopConnection();

            // Uwolnij zasoby scen w zależności od stanu aplikacji
            if (clientState == ClientStates.LOBBY) {
                lobbyScene = null; // lobbyScene zgarnie garbage collector
            } else if (clientState == ClientStates.GAME) {
                gameScene = null; // gameScene zgarnie garbage collector
                gameManager = null; // gameManager zgarnie garbage collector
            } else {
                replayScene = null; // replayScene zgarnie garbage collector
                // replayManager = null; // replayManager zgarnie garbage collector
            }
        }

        // Zmień stan aplikacji na ekran łączenia
        clientState = ClientStates.JOIN;
        primaryStage.setScene(joinScene);
    }

    /**
     * Ustawia scenę lobby.
     * Tworzy nową scenę lobby i ustawia ją jako aktywną.
     */
    public void showLobbyScene() {
        clientState = ClientStates.LOBBY;

        // Inicjalizacja sceny lobby
        lobbyScene = new LobbyScene(this);
        primaryStage.setScene(lobbyScene);
    }

    /**
     * Ustawia scenę gry.
     * Tworzy nową scenę gry, inicjalizuje manager gry i zmienia aktualny stan aplikacji.
     *
     * @param variant wariant gry (np. liczba graczy)
     * @param turn numer aktualnej tury
     * @param players lista graczy z ich identyfikatorami i pseudonimami
     * @param id identyfikator aktualnego gracza
     * @param board stan planszy w formie listy pól
     */
    public void showGameScene(
        String variant, 
        int turn, 
        ArrayList<Pair<Integer, String>> players, 
        int id, 
        ArrayList<String> board
    ) {
        clientState = ClientStates.GAME;

        // Tworzenie sceny gry i managera gry
        gameScene = new GameScene(players, this);
        gameManager = new GameManager(gameScene, ioManager, variant, turn, players, id, board);

        // Ustawienie nowej sceny
        primaryStage.setScene(gameScene);

        // Uwolnienie zasobów sceny lobby
        lobbyScene = null; // lobbyScene zgarnie garbage collector
    }

    /**
     * Ustawia scenę gry.
     * Tworzy nową scenę gry, inicjalizuje manager gry i zmienia aktualny stan aplikacji.
     *
     * @param variant wariant gry (np. liczba graczy)
     * @param turn numer aktualnej tury
     * @param players lista graczy z ich identyfikatorami i pseudonimami
     * @param id identyfikator aktualnego gracza
     * @param board stan planszy w formie listy pól
     */
    public void showReplayScene(
        String variant, 
        int turn, 
        ArrayList<Pair<Integer, String>> players, 
        int id, 
        ArrayList<String> board
    ) {
        clientState = ClientStates.REPLAY;

        // Tworzenie sceny gry i managera gry
        replayScene = new ReplayScene(players, this);
        // gameManager = new GameManager(gameScene, ioManager, variant, turn, players, id, board);

        // Ustawienie nowej sceny
        primaryStage.setScene(gameScene);

        // Uwolnienie zasobów sceny lobby
        lobbyScene = null; // lobbyScene zgarnie garbage collector
    }

    /**
     * Aktualizuje stan gry, przekazując nową planszę i numer tury.
     * Wywoływana przez manager komunikacji, gdy serwer wysyła aktualizację gry.
     *
     * @param turn numer aktualnej tury
     * @param board nowy stan planszy
     */
    public void updateGame(int turn, ArrayList<String> board) {
        gameManager.updateGameScene(board);
        gameManager.updateGameState(turn);
    }

    /**
     * Główna metoda aplikacji, uruchamiająca JavaFX.
     *
     * @param args argumenty wiersza poleceń
     */
    public static void main(String[] args) {
        launch(args);
    }
}
