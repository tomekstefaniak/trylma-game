package server.trylma.bot;

public class BotBrain {

    private ServerIOHandler serverIOHandler;

    private int board[][];
    private String thisPlayerID;
    
    public BotBrain(ServerIOHandler serverIOHandler) {
        this.serverIOHandler = serverIOHandler;
    }

    public void startGame(
        String variantString, 
        String turnString, 
        String boardString, 
        String idString, 
        String playersString
    ) {
        parseBoard(boardString);
        thisPlayerID = idString;
    }

    private void parseBoard(String boardString) {
        /**
         * LOGIKA PARSOWANIA STRINGA BOARDA DO TABLICY I AKTUALIZACJI FIELDA
         */
    }

    public void nextMove(String turnString, String boardString) {
        parseBoard(boardString);
        move();
    }

    public void move() {
        String moveString = "";

        /**
         * LOGIKA WYKONYWANIA RUCHU
         */

        serverIOHandler.sendMessageToServer(moveString);
    }
}
