
package server.trylma;

import java.io.*;
import java.net.*;
import java.util.*;

import server.trylma.bot.Bot;
import server.trylma.components.*;

/**
 * Klasa serwera, na ktorym odbywa sie rozgrywka
 */
public class ServerApp {

	/** Maksymalna pojemnosc serwera, takze liczba graczy, dla ktorych odbywa sie rozgrywka */
	private final int maxCapacity;

	/** Wariant gry rozgrywany na tym serwerze */
	private final char variant;

	/** Lista graczy na serwerze */
	public ArrayList<ClientThread> players;

	public ArrayList<Bot> bots;

	/** Instancja GameEngine zarzadzajaca gra na serwerze */
	public GameEngine game;

	public ReplayEngine replay;

	/**
	 * Konstruktor klasy
	 * @param port port serwera
	 * @param maxCapacity maksymalna pojemnosc serwera
	 * @param variant wariant gry rozgrywanej na serwerze
	 * @throws IOException jezeli wystapil blad przy pracy serwera
	 * @throws IllegalArgumentException jezeli podano niepoprawny port serwera
	 */
	public ServerApp(int port, int maxCapacity, char variant, GameEngine game, ReplayEngine replay) throws IOException, IllegalArgumentException {
		this.maxCapacity = maxCapacity;
		this.variant = variant;
		this.game = game;
		this.game.setPort(port);
		this.replay = replay;

		// Utwórz server socket i włącz prawidłowy loop w zależności od trybu serwera (game albo replay)
		try (ServerSocket serverSocket = new ServerSocket(port)) {

			// Ustawienie kontrolowanego zamknięcia serverSocketa, by klienci mogli odczytać null
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (serverSocket != null && !serverSocket.isClosed()) {
						printForAll("null");
                        serverSocket.close();
                        System.out.println("Server socket closed after sending null to all connected clients");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));

			System.out.println("[SERVER] server listening on port " + port);

			this.players = new ArrayList<ClientThread>();

			switch (variant) {
				case 'r':
					runReplayServerLoop(serverSocket);
					break;
				default:
					runGameServerLoop(serverSocket);
			}
		}
		catch (IOException e) {throw new IOException("ServerApp.run: IOException " + e.getMessage());}
		catch (IllegalArgumentException e) {throw new IllegalArgumentException("ServerApp.run: invalid port");}
	}

	/**
	 * Glowna metoda odpowiadajaca za prace serwera
	 * @throws IOException jezeli wystapil blad przy pracy serwera
	 * @throws IllegalArgumentException jezeli port jest niepoprawny
	 */
	private void runReplayServerLoop(ServerSocket serverSocket) throws IOException, IllegalArgumentException {

		while (true) {
			Socket socket = serverSocket.accept();
			updatePlayers();
			
			// nie pozwol na wejscie jezeli jest maksymalna liczba graczy na serwerze lub trwa gra
			if (players.size() < maxCapacity) {
				ClientThread client = new ClientThread(socket, this, false, 'r');
				client.start();
				players.add(client);

				client.replayEngine = replay;
				client.replayEngine.setClient(client);
			} else {
				new ClientThread(socket, this, true).start(); 
			}
		}
	}

	/**
	 * Glowna metoda odpowiadajaca za prace serwera
	 * @throws IOException jezeli wystapil blad przy pracy serwera
	 * @throws IllegalArgumentException jezeli port jest niepoprawny
	 */
	private void runGameServerLoop(ServerSocket serverSocket) throws IOException, IllegalArgumentException {
		this.bots = new ArrayList<Bot>();

		while (true) {
			Socket socket = serverSocket.accept();
			updatePlayers();
			
			// nie pozwol na wejscie jezeli jest maksymalna liczba graczy na serwerze lub trwa gra
			if (players.size() + bots.size() < maxCapacity && !game.state()) {
				ClientThread client = new ClientThread(socket, this, false, variant);
				client.start();
				players.add(client);

				// Czekanie na odczytanie nickname - oraz wyświetlenie informacji o dołączeniu gracza
				int count = 0; // Ustawienie limitu na wypadek nicknamu "Player"
				while (client.nickname.equals("Player") && count < 40) // Czekamy maksymalnie 2 sekundy
					try { Thread.sleep(50); } catch (Exception e) {}

				// Wyślij informację o nicknames do każdego gracza w lobby
				updateLobbyPlayers();

				// rozpoczecie gry jezeli na serwer weszla ostatnia potrzebna osoba
				if (players.size() + bots.size() == maxCapacity && !game.state())
					startGame();
			} else {
				new ClientThread(socket, this, true).start(); 
			}
		}
	}

	/**
	 * Metoda sprawdzajaca czy ktorys z klientow nie opuscil serwera i aktualizujaca wtedy liste graczy
	 */
	public void updatePlayers() {
		int n = players.size();
		for (int i = 0; i < n; i++) {
			ClientThread client = players.get(i);
			if (client == null || client.ended) {
				players.remove(client);
				n--;
			}
		}
	}

	/**
	 * Wysyla do graczy w lobby liste nicknamow graczy bedacych na serwerze
	 */
	public void updateLobbyPlayers() {
		String playersInLobbyString = "lobby ";
		for (ClientThread player: players)
			playersInLobbyString += player.nickname + " ";

		// Wyślij wszystkim listę graczy z tym który dołączył do lobby
		printForAll(playersInLobbyString);
	}

	/**
	 * Wysyla dana wiadomosc wszystkim graczom na serwerze
	 * @param message wiadomosc do wyslania
	 */
	public void printForAll(String message) {
		for (ClientThread client : players)
			client.print(message);
		for (Bot bot : bots) {
			bot.inform(message);
		}
	}

	public void addBot() {
		if (players.size() + bots.size() < maxCapacity && !game.state()) {
			int botID = bots.size();
			bots.add(new Bot(botID, this, game));
		} else {
			throw new IllegalArgumentException("cannot add bot");
		}

		if (players.size() + bots.size() == maxCapacity && !game.state())
			startGame();
	}

	public void removeBot() {
		try {bots.remove(0);}
		catch(IndexOutOfBoundsException e) {throw new IllegalArgumentException("cannot remove bot.");}
	}

	/**
	 * Metoda rozpoczynajaca rozgrywke na serwerze
	 */
	public void startGame() {
		try {
			game.start(variant, players.size() + bots.size());
			System.out.println("[SERVER] started game");
			
			printForAll("started");
			printForAll("variant " + variant);
			printForAll("turn " + game.getActivePlayer());
			printForAll("board " + game.draw());
			
			// Ustawienie id graczy i wysłanie wiadomości z nickami i ids
			String playersString = "";
			for(int i = 0; i < players.size(); i++) {
				players.get(i).setID(i);
				players.get(i).print("ID " + i);
				playersString += String.valueOf(i) + ":" + players.get(i).nickname + " ";
			}
			for(int i = 0; i < bots.size(); i++) {
				bots.get(i).setID(i + players.size());
				playersString += String.valueOf(i + players.size()) + ":" + bots.get(i).nickname + " ";
			}
			
			printForAll(playersString);

			new GameThread(this).start();
		} catch(Exception e) { System.out.println(e.getMessage()); }
	}

	/**
	 * Metoda pozwalajaca na rozpoczecie pracy serwera przez ten plik
	 * @param args port pojemnosc serwera wariant gry
	 */
	public static void main(String[] args) {
		try {
			int port, maxCapacity; 

			try { port = Integer.parseInt(args[0]); } 
			catch(NumberFormatException e) { throw new IllegalArgumentException("ServerApp.main: invalid port"); }

			try {maxCapacity = Integer.parseInt(args[1]);} 
			catch(NumberFormatException e) { throw new IllegalArgumentException("ServerApp.main: invalid capacity"); }

			// Sprawdzenie poprawności wariantu

			if (!args[2].matches("r|c|o"))
				throw new IllegalArgumentException("SeverApp.main: wrong variant (cmd line arg at idx 2)");

			char variant = args[2].charAt(0);
			new ServerApp(port, maxCapacity, variant, new GameEngine(), new ReplayEngine());
		} 
		catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
		catch (IndexOutOfBoundsException e) { System.out.println("ServerApp.main: invalid number of arguments"); }
		catch (IOException e) { System.out.println(e.getMessage()); }
	}
}
