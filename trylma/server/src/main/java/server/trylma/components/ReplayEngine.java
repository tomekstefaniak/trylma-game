package server.trylma.components;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.trylma.database.Move;
import server.trylma.database.MoveService;

/**
 * Klasa obsługująca replay dla danego klienta
 */
@Component
public class ReplayEngine {

    @Autowired
    private MoveService moveService;

    private ClientThread client;

    /** !!!!!!!! Listy bardziej do testów, spokojnie można zmienić sposób przechowywania ruchów */
    private ArrayList<String> gameStatesHistory;

    private int curr;
    private boolean gameFetched = false;

    public void setClient(ClientThread client) {
        this.client = client;
    }

    /**
     * Metoda do pobiarania rozgrywki z bazy danych i wysyłania pierwszego stanu gry (po pierwszym ruchu)
     * 
     * @param gameID
     */
    public void loadGame(String gamePort, String gameID) {
        // try {
            System.out.println("replay started");
            int port = Integer.parseInt(gamePort);
            int game = Integer.parseInt(gameID);

            ArrayList<Move> moves = moveService.getGame(port, game);

            gameStatesHistory = new ArrayList<String>();
            for (Move move : moves) {
                gameStatesHistory.add(move.getBoard());
            }

            // Catch na wypadek gdyby lista ruchów była pusta
            try { client.print(gameStatesHistory.get(0)); } 
            catch (Exception e) { 
                client.print("end"); 
                return;
            }

            curr = 1;
            gameFetched = true;

            System.out.println("Replay started!");
        // } catch (Exception e) { System.out.println("ERROR during starting replay!"); }
    }

    /**
     * Metoda do wysyłania następnego ruchu z wczytanej rozgrywki z bazy danych
     */
    public void sendNextMove() {
        try {
            if (gameFetched) {
                
                if (curr < gameStatesHistory.size()) {
                    // Wysłanie kolejnego stanu rozgrywki
                    client.print("board " + gameStatesHistory.get(curr));
                    curr++;

                } else {
                    client.print("ended");
                }
            }
        } catch (Exception e) {}
    }
}
