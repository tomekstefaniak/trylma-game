package server.trylma.components;

import java.util.ArrayList;
import java.util.List;

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
    private ArrayList<String> gameTurnsHistory;

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
    public void loadGame(String gameID) {
        try {
            /**
             * ŁADOWANIE GRY Z BAZY danych
             * 
             * NA DOLE TESTOWE DANE
             */

            int port = Integer.parseInt(gameID.substring(0, gameID.indexOf(' ')));
            int game = Integer.parseInt(gameID.substring(gameID.indexOf(' ') + 1));

            ArrayList<Move> moves = moveService.getGame(port, game);

            gameStatesHistory = new ArrayList<String>();
            for (Move move : moves) {
                gameStatesHistory.add(move.getBoard());
            }

            // gameStatesHistory = new ArrayList<>(List.of(
            //     "O/OO/OOO/OOOO/OOOO0O1OOOOOO/OOOOOOO1OOOO/OO1OO2201OO/O12O00O0OO/OO2211012/OO0OOOOOOO/OOOOOO012OO/OOO0OO021OOO/OOOOO2O2OOOOO/OOOO/OOO/OO/O/",
            //     "O/OO/OOO/OOOO/OOOO0O1OOOOOO/OOOOOOO1OOOO/OO1OO2201OO/O12O00O0OO/OO2211012/OO0OOOOOOO/OOOOOO01OOO/OOO0OO021OOO/OOOO22O2OOOOO/OOOO/OOO/OO/O/",
            //     "O/OO/OOO/OOOO/OOOO0O1OOOOOO/OOOOOOO10OOO/OO1OO2201OO/O12OO0O0OO/OO2211012/OO0OOOOOOO/OOOOOO01OOO/OOO0OO021OOO/OOOO22O2OOOOO/OOOO/OOO/OO/O/",
            //     "O/OO/OOO/OOOO/OOOO0O1OOOOOO/OOOOOOO10OOO/OO1OO2201OO/OO2OO0O0OO/OO2211012/OO0O1OOOOO/OOOOOO01OOO/OOO0OO021OOO/OOOO22O2OOOOO/OOOO/OOO/OO/O/",
            //     "O/OO/OOO/OOOO/OOOO0O1OOOOOO/OOOOOOO10OOO/OO1OO2201OO/OO2OO0O0OO/OO2211012/O20O1OOOOO/OOOOOO01OOO/OOO0OO0O1OOO/OOOO22O2OOOOO/OOOO/OOO/OO/O/",
            //     "O/OO/OOO/O0OO/OOOO0O1OOOOOO/OOOOOOO1OOOO/OO1OO2201OO/OO2OO0O0OO/OO2211012/O20O1OOOOO/OOOOOO01OOO/OOO0OO0O1OOO/OOOO22O2OOOOO/OOOO/OOO/OO/O/"
            // ));
            gameTurnsHistory = new ArrayList<>(List.of(
                "1",
                "2",
                "0",
                "1",
                "2",
                "0"
            ));

            /**
             * WYSŁANIE INFO Z PIERWSZEGO RUCHU (PODOBNIE DO STARTU GRY) 
             * TYLKO STAN JEST JUŻ PO PIERWSZYM RUCHU
             */

            client.print("replay started");
            client.print("variant c"); // tryb
            client.print("turn 1"); // kto się ruszył
            client.print("board O/OO/OOO/OOOO/OOOO0O1OOOOOO/OOOOOOO1OOOO/OO1OO2201OO/O12O00O0OO/OO2211012/OO0OOOOOOO/OOOOOO012OO/OOO0OO021OOO/OOOOO2O2OOOOO/OOOO/OOO/OO/O/"); // stan board
            client.print("0:Obama 1:Trump 2:Putin"); // lista graczy (identyczny format jak przy grze)

            curr = 1;

            gameFetched = true;
        } catch (Exception e) {}
    }

    /**
     * Metoda do wysyłania następnego ruchu z wczytanej rozgrywki z bazy danych
     */
    public void sendNextMove() {
        try {
            if (gameFetched) {
                
                if (curr < gameStatesHistory.size()) {
                    client.print("replay next");
                    client.print("turn " + gameTurnsHistory.get(curr));
                    client.print("board " + gameStatesHistory.get(curr));
                    curr++;

                } else {
                    client.print("ended");
                }
            }
        } catch (Exception e) {}
    }
}
