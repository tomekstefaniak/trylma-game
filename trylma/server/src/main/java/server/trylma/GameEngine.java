package server.trylma;

import java.util.*;

import server.trylma.bot.Bot;
import server.trylma.game.*;

/**
 * Klasa odpowiadajaca za przeprowadzanie rozgrywki
 */
public class GameEngine {
	/** Instancja gry */
	private Game game;

	/** Liczba graczy, przez ktorych rozgrywana jest gra */
	private int players;

	/** Aktywny gracz */
	private int activePlayer;

	/** Lista graczy, ktorzy opuscili rozgrywke */
	private ArrayList<Integer> playersOut;

	/**
	 * Obecny stan gry
	 * @return true - gra trwa, false - gra sie nie zaczela lub skonczyla
	 */
	public boolean state() {
		if (game == null || ended()) return false;
		return true;
	}

	/**
	 * Rozpoczyna gre
	 * @param variant znak odpowiadajacy typowi rozgrywki 
	 * @param players liczba graczy
	 * @throws IllegalArgumentException jezeli nie mozna zaczac gry (aktywna gra, nieznany wariant, zla liczba graczy)
	 */
	public void start(char variant, int players) throws IllegalArgumentException {
		if (state()) throw new IllegalArgumentException("GameEngine.start: game active");
		switch(variant) {
			case 'c':
				try {game = new ClassicGame(players);} catch (IllegalArgumentException e) {throw e;}
				break;
			case 'o':
				try {game = new OOOCGame(players);} catch (IllegalArgumentException e) {throw e;}
				break;
			default:
				throw new IllegalArgumentException("GameEngine.start: unknown game type");
		}
		this.players = players;
		this.activePlayer = new Random().nextInt(this.players);
		this.playersOut = new ArrayList<Integer>();
	}

	/**
	 * Wykonuje ruch gracza
	 * @param player gracz wysylajacy zadanie ruchu
	 * @param args String zawierajacy zakodowany ruch gracza
	 * @throws IllegalArgumentException jezeli wykonanie ruchu nie jest mozliwe (gra nie jest aktywna, nieaktywny gracz, niepoprawnie zakodowany ruch, niepoprawny ruch)
	 */
	public void move(int player, String args) throws IllegalArgumentException {
		if (!state()) throw new IllegalArgumentException("GameEngine.move: no active game");
		if (player != activePlayer) throw new IllegalArgumentException("GameEngine.move: wrong player");
		int xS, yS, xF, yF;
		try {
			xS = Integer.parseInt(args.substring(0, args.indexOf(',')));
			yS = Integer.parseInt(args.substring(args.indexOf(',') + 1, args.indexOf('-')));
			xF = Integer.parseInt(args.substring(args.indexOf('-') + 1, args.lastIndexOf(',')));
			yF = Integer.parseInt(args.substring(args.lastIndexOf(',') + 1));
		} catch(Exception e) {throw new IllegalArgumentException("GameEngine.move: invalid field");}
		try {
			game.move(player, xS, yS, xF, yF);
			nextPlayer(activePlayer);
		} catch(IllegalArgumentException e) {throw e;}
	}

	/**
	 * Rysuje plansze w sposob zakodowany przez gre
	 * @return String zawierajacy zakodowana plansze
	 * @throws IllegalArgumentException jezeli gra nie zostala rozpoczeta
	 */
	public String draw() throws IllegalArgumentException {
		if (game == null) throw new IllegalArgumentException("GameEngine.draw: game not started");
		return game.draw();
	}

	/**
	 * @return aktywny gracz
	 * @throws IllegalArgumentException jezeli gra nie jest aktywna
	 */
	public int getActivePlayer() throws IllegalArgumentException {
		if (!state()) throw new IllegalArgumentException("GameEngine.getActivePlayer: no active game");
		return activePlayer;
	}

	/**
	 * Zmienia aktywnego gracza na kolejnego w kolejce pomijajac graczy, ktorzy opuscili rozgrywke
	 * @param player obecnie aktywny gracz
	 * @throws IllegalArgumentException jezeli nie mozna zmienic gracza (gra nieaktywna, zly gracz pomija kolejke)
	 */
	public void nextPlayer(int player) throws IllegalArgumentException {
		if (!state()) throw new IllegalArgumentException("GameEngine.nextPlayer: no active game");
		if (player != activePlayer) throw new IllegalArgumentException("GameEngine.nextPlayer: wrong player");
		activePlayer = (activePlayer + 1) % players;
		if (playersOut.contains(activePlayer)) {
			try {nextPlayer(activePlayer);} catch(IllegalArgumentException e) {throw e;}
		}
	}
	
	/**
	 * Dodaje gracza, ktory opuscil rozgrywke do listy, aby go pomijac
	 * @param id ID gracza, ktory opuscil rozgrywke
	 * @throws IllegalArgumentException jezeli ten gracz jest juz na liscie
	 */
	public void addPlayersOut(int id) throws IllegalArgumentException {
		if (playersOut.contains(id)) throw new IllegalArgumentException("GameEngine.addPlayersOut");
		playersOut.add(id);
	}
	
	/**
	 * Sprawdza czy gra sie juz zakonczyla
	 * @return true - gra sie skonczyla, false - gra nadal trwa
	 * @throws IllegalArgumentException jezeli gra sie nie zaczela
	 */
	public boolean ended() throws IllegalArgumentException {
		if (game == null) throw new IllegalArgumentException("GameEngine.ended: game not started");
		return game.gameEnded(players - playersOut.size());
	}

	/**
	 * Zwraca liste graczy, ktorzy wygrali w kolejnosci ukonczenia rozgrywki
	 * @return lista graczy
	 * @throws IllegalArgumentException jezeli gra sie nie zaczela
	 */
	public ArrayList<Integer> getResult() throws IllegalArgumentException {
		if (game == null) throw new IllegalArgumentException("GameEngine.getResult: game not started");
		return game.gameWon();
	}
	
	/** 
	 * Konczy rozgrywke
	 */
	public void end(ServerApp server) {
		game = null;
		server.bots = new ArrayList<Bot>();
	}

	public Board getBoard() {
		return game.getBoard();
	}
}
