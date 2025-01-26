
package server.trylma.game;

import java.util.ArrayList;

/**
 * Klasa reprezentujaca gre w wariancie order out of chaos
 */
public class OOOCGame implements Game {
	/** Plansza, na ktorej rozgrywana jest gra */
	private Board board;

	/**
	 * Konstruktor klasy
	 * @param players liczba graczy
	 * @throws IllegalArgumentException jezeli nie mozna rozpoczac gry dla danej liczby graczy
	 */
	public OOOCGame(int players) throws IllegalArgumentException {
		if (players != 2 && players != 3) {
			throw new IllegalArgumentException("invalid players number");
		}
		board = new OOOCBoard();
		board.fillBoard(players);
	}

	@Override
	public String draw() {
		return board.draw();
	}

	@Override
	public void move(int player, int xS, int yS, int xF, int yF) throws IllegalArgumentException {
		try {board.move(player, xS, yS, xF, yF);} catch(IllegalArgumentException e) {throw e;}
	}

	@Override
	public ArrayList<Integer> gameWon() {
		return board.getWinnerList();
	}

	@Override
	public boolean gameEnded(int players) {
		if (players == 0) return true;
		return board.getWinnerList().size() == players - 1 && board.getWinnerList().size() != 0;
	}

	@Override
	public Board getBoard() {
		return board;
	}
	
}
