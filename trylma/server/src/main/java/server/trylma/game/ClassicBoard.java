package server.trylma.game;

import java.util.ArrayList;

/**
 * Klasa reprezentujaca plansze klasycznej gry
 */
public class ClassicBoard implements Board {
	/** Szerokosc planszy */
	private final int width = 13;

	/** Wysokosc planszy */
	private final int height = 17;

	/** Tablica dwuwymiarowa pol, odpowiadajaca planszy */
	private Field[][] board;

	/** Lista graczy, ktorzy wygrali, w kolejnosci ukonczenia */
	private ArrayList<Integer> winnerList;

	/**
	 * Konstruktor klasy
	 */
	public ClassicBoard() {
		board = Board.createBoard(height, width);

		// wypelnienie sasiadami
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				board[i][j].addNeighbors(this);
			}
		}

		winnerList = new ArrayList<Integer>();
	}

	@Override
	public void fillBoard(int players) {
		switch (players) {
			case 2:
				fillCorner(1, 0);
				fillCorner(4, 1);
				break;
			case 3:
				fillCorner(1, 0);
				fillCorner(3, 1);
				fillCorner(5, 2);
				break;
			case 4:
				fillCorner(2, 0);
				fillCorner(3, 1);
				fillCorner(5, 2);
				fillCorner(6, 3);
				break;
			case 6:
				fillCorner(1, 0);
				fillCorner(2, 1);
				fillCorner(3, 2);
				fillCorner(4, 3);
				fillCorner(5, 4);
				fillCorner(6, 5);
				break;
		}
	}

	private void fillCorner(int corner, int player) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (board[i][j].getType() == corner) {
					board[i][j].setPiece(new Piece(i, j, player, (corner > 3 ? (corner + 3) % 6 : corner + 3)));
				}
			}
		}
	}

	@Override
	public String draw() {
		String result = "";
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (board[i][j].getType() != -1) {
					Piece piece = board[i][j].getPiece();
					if(piece == null) {
						result += 'O';
					} else {
						result += piece.getPlayer();
					}
				}
			}
			result += '/';
		}
		return result;
	}

	@Override
	public void move(int player, int xS, int yS, int xF, int yF) throws IllegalArgumentException {
		Field field;
		Field destination;
		try {field = getField(xS, yS);} catch(IllegalArgumentException e) {throw e;} // index poza tablica
		try {destination = getField(xF, yF);} catch(IllegalArgumentException e) {throw e;} // index poza tablica

		if (field.getType() == -1) {throw new IllegalArgumentException("invalid starting field");} // pole poza plansza
		if (destination.getType() == -1) {throw new IllegalArgumentException("invalid end field");} // pole poza plansza

		Piece piece = field.getPiece();
		if (piece == null) {throw new IllegalArgumentException("no piece on starting field");} // brak pionka na polu startowym
		if (piece.getPlayer() != player) {throw new IllegalArgumentException("wrong piece on starting field");} // pionek innego gracza na polu startowym
		if (destination.getPiece() != null) {throw new IllegalArgumentException("other piece on end field");} // inny pionek na polu docelowym
		
		if (possibleMove(field, destination)) {
			try {field.removePiece();} catch(IllegalArgumentException e) {throw e;} // brak pionka na polu startowym
			try {destination.setPiece(piece);} catch(IllegalArgumentException e) {throw e;} // inny pionek na polu docelowym
			updateWinners();
		} else {
			throw new IllegalArgumentException("illegal move");
		}
	}

	@Override
	public Field getField(int x, int y) throws IllegalArgumentException {
		try {return board[x][y];} catch(IndexOutOfBoundsException e) {throw new IllegalArgumentException("invalid field");}
	}

	/**
	 * Sprawdza czy przejscie z jednego pola na drugie jest mozliwe
	 * @param from pole startowe
	 * @param to pole koncowe
	 * @return czy mozna przejsc z from do to
	 */
	public boolean possibleMove(Field from, Field to) {
		if (from.isNeighbor(to)) {
			return true;
		}

		ArrayList<Field> possibleJumps = new ArrayList<Field>();
		possibleJumps.add(from);

		boolean added = true;
		while (added) {
			added = false;
			int n = possibleJumps.size();
			for (int i = 0; i < n; i++) {
				Field field = possibleJumps.get(i);
				for (int j = 0; j < 6; j++) {
					Field neighbor = field.getNeighbor(j);
					if (neighbor != null && neighbor.getType() != -1 && neighbor.getPiece() != null) {
						Field potJump = neighbor.getNeighbor(j);
						if (potJump != null && potJump.getType() != -1 && potJump.getPiece() == null && !possibleJumps.contains(potJump)) {
							if (potJump == to) {
								return true;
							}
							possibleJumps.add(potJump);
							added = true;
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * Sprawdza i aktualizuje liste zwyciezcow
	 */
	private void updateWinners() {
		int[] playersPieces = new int[6];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Field field = board[i][j];
				Piece piece = field.getPiece();
				if (piece != null && piece.getDestination() == field.getType()) {
					playersPieces[piece.getPlayer()]++;
				}
			}
		}

		for (int i = 0; i < 6; i++) {
			if (playersPieces[i] == 10 && !winnerList.contains(i)) {
				winnerList.add(i);
			}
		}
	}

	/**
	 * @return lista zwyciezcow
	 */
	public ArrayList<Integer> getWinnerList() {
		return winnerList;
	}
}
