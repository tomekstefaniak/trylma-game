package server.trylma.game;

/**
 * Klasa reprezentujaca pionek na planszy
 */
public class Piece {
	/** Gracz, ktorego to jest pionek */
	private int player;

	/** Wspolrzedna x na planszy */
	private int x;

	/** Wspolrzedna y na planszy */
	private int y;

	/** Cel pionka - typ pola, na ktorym musi sie znalezc pionek, aby gracz mogl wygrac */
	private int destination;

	/** 
	 * Konstruktor klasy
	 * @param x wspolrzedna x pionka
	 * @param y wspolrzedna y pionka
	 * @param player gracz, ktorego to jest pionek
	 * @param destination cel pionka
	 */
	public Piece(int x, int y, int player, int destination) {
		this.player = player;
		changePosition(x, y);
		this.destination = destination;
	}

	/**
	 * Zmienia wspolrzedne pionka
	 * @param x nowa wspolrzedna x
	 * @param y nowa wspolrzedna y
	 */
	public void changePosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return gracz, ktorego to jest pionek
	 */
	public int getPlayer() {
		return player;
	}

	/**
	 * @return wpolrzedna x pionka
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return wspolrzedna y pionka
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return cel pionka
	 */
	public int getDestination() {
		return destination;
	}
}
