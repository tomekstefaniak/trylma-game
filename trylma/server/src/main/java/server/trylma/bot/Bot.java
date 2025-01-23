package server.trylma.bot;

import java.net.Socket;
import java.io.IOException;

public class Bot {

    private static String[] botNicknames = {
        "Shinpiung", "Bożydar", "Uzghul", "Zutgrud", "Bocian", "Prycz"
    };
    
    public Bot(int port, int botNumber) throws IOException {
        Socket socket = new Socket("localhost", port);
        System.out.println("Connected to lobby (server) on port: " + port);

        // Tworzenie instancji ServerIOHandler
        ServerIOHandler serverIOHandler = new ServerIOHandler(socket);

        // Ustawienie wątku jako demona
        serverIOHandler.setDaemon(true);
        serverIOHandler.start();

        // Wysłanie pseudonimu gracza do serwera
        String nickname = botNicknames[botNumber];

        try {
            serverIOHandler.sendMessageToServer(nickname);
        } catch (Exception e) {
            System.err.println("Error while sending nickname: " + e.getMessage());
            serverIOHandler.running = false;
        }
    }
}
