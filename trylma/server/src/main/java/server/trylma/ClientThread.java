package server.trylma;

import java.io.*;
import java.net.*;

/**
 * Klasa odpowiadajaca za polaczenie klienta z serwerem
 */
public class ClientThread extends Thread {
	/** Socket laczacy klienta i serwer */
	private Socket socket;

	/** Instancja serwera, z ktorym laczy sie serwer */
	private ServerApp server;

	/** ID gracza */
	private int id;

	/** Nickname gracza */
	public String nickname;

	/** Flaga sprawdzajaca, czy serwer nie odrzucil klientowi polaczenia */
	private boolean deny;

	/** Flaga sprawdzajaca, czy watek laczacy klienta z serwerem sie nie zakonczyl */
	public boolean ended;

	/** BufferedReader czytajacy input klienta */
	private BufferedReader in;

	/** PrintWriter wysylajacy output serwera do klienta */
	private PrintWriter out;

	/** Tryb sesji serwera */
	private char variant;

	/**
	 * Konstruktor klasy na wypadek blokowania połączenia z klientem przy pełnym lobby 
	 * 
	 * @param socket socket laczacy klienta z serwerem
	 * @param server instancja serwera, z ktorym laczy sie klient
	 * @param deny flaga, mowiaca czy serwer pozwolil na polaczenie
	 */
	public ClientThread(Socket socket, ServerApp server, boolean deny) {
		this.socket = socket;
		this.server = server;

		id = -1; // Przed rozpoczęciem rozgrywki id jest ustawione na -1
		nickname = "Player"; // Domyslny nickname

		this.deny = deny;
		this.ended = false;
	}

	/**
	 * Konstruktor klasy
	 * 
	 * @param socket socket laczacy klienta z serwerem
	 * @param server instancja serwera, z ktorym laczy sie klient
	 * @param deny flaga, mowiaca czy serwer pozwolil na polaczenie
	 */
	public ClientThread(Socket socket, ServerApp server, boolean deny, char variant) {
		this.socket = socket;
		this.server = server;

		id = -1; // Przed rozpoczęciem rozgrywki id jest ustawione na -1
		nickname = "Player"; // Domyslny nickname

		this.deny = deny;
		this.ended = false;

		this.variant = variant;
	}

	/**
	 * Metoda watku, ktora posredniczy miedzy klientem i serwerem
	 */
	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			// Wysłanie polecenia rozlaczenia do klienta, jezeli serwer nie zezwolil na polaczenie
			if (deny)
				out.println("disconnect");
			else {
				// Powiadomienie gracza o modzie pracy serwera
				switch (variant) {
					case 'r': print("mode replay"); break;
					default: print("mode game");
				}

				// ustaw nickname, czyli pierwszą wiadomość jaką wyśle klient
				nickname = in.readLine();
				
				System.out.println("[SERVER] player " + nickname + " connected to server");
				
				String messageFromClient, messageToClient;
				do {
					try {
						messageFromClient = in.readLine();
						if (messageFromClient == null) {
							throw new IOException();
						}
					} 
					catch(IOException e) {
						System.out.println("[SERVER] player " + id + " disconnected");
						
						ended = true; // Ustawienie flagi ended dla update
						server.updatePlayers();
						server.updateLobbyPlayers();
						
						break; // Jedyne miejsce zrywające pętlę
					}
					System.out.println("Client " + id + " > " + messageFromClient);
					
					messageToClient = Interpreter.respond(messageFromClient, server, id);
					if (messageToClient.startsWith("[ALL]")) {
						server.printForAll(messageToClient);
					} else {
						print(messageToClient);
					}
				} while (true);
			}
		} catch (IOException e) { 
			System.out.println("ClientThread.run: IOException " + e.getMessage()); 
		}

		try {server.game.nextPlayer(id);} catch(Exception e) {}
		try {server.game.addPlayersOut(id);} catch(Exception e) {}
		try {server.printForAll("player " + id + " left; turn: " + server.game.getActivePlayer());} catch(Exception e) {}
		
		this.ended = true;
		server.updatePlayers();
	}

	/**
	 * Wysyla wiadomosc do klienta
	 * @param message wiadomosc do wyslania
	 */
	public void print(String message) { 
		out.println(message); 
	}

	/**
	 * @return ID klienta
	 */
	public int getID() { 
		return id; 
	}

	/**
	 * Ustawia ID klienta
	 * @param newID nowe ID klienta
	 */
	public void setID(int newID) { 
		this.id = newID; 
	}
}
