package server.trylma.game;

/**
 * Klasa reprezentujaca pole na planszy
 */
public class Field {
	/** Wspolrzedna x pola na planszy */
	private final int x;

	/** Wspolrzedna y pola na planszy */
	private final int y;

	/** Typ pola:
	 * -1: pole poza plasza
	 * 0: pole na srodku plaszy
	 * 1, ..., 6: pole nalezace do kolejnego naroznika liczone od srodkowego, gornego zgodnie ze wskazowkami zegara
	 */
	private final int type;

	/** Instancja pionka na polu, null jezeli na polu nie ma pionka */
	private Piece piece;

	/** Tablica przechowujaca instancje sasiednich pol
	 * indeksy sasiadow liczone sa zgodnie ze wskazowkami zegara zaczynajac od 0 - lewy gorny 
	 */
	private Field[] neighbors;

	private int[] distance;

	/**
	 * Konstruktor klasy
	 * @param x wspolrzedna x pola
	 * @param y wspolrzedna y pola
	 * @param type typ pola
	 */
	public Field(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.piece = null; // na poczatku brak pionka na polu
		
		this.distance = new int[6];
		for (int i = 0; i < 6; i++) {
			this.distance[i] = Integer.MAX_VALUE;
		}
	}

	/**
	 * Ustawia dany pionek na polu
	 * @param piece pionek, ktory ma sie znalezc na polu
	 * @throws IllegalArgumentException jezeli inny pionek jest juz na tym polu
	 */
	public void setPiece(Piece piece) throws IllegalArgumentException {
		if (this.piece != null) {
			throw new IllegalArgumentException("other piece on field already");
		}
		this.piece = piece;
		this.piece.changePosition(x, y);
	}

	/**
	 * Usuwa pionek z pola (ustawia piece na null, wiec aby zachowac pionka, nalezy go najpierw pobrac)
	 * @throws IllegalArgumentException jezeli na polu juz nie ma pionka
	 */
	public void removePiece() throws IllegalArgumentException {
		if(this.piece == null) {throw new IllegalArgumentException("no piece on field already");}
		this.piece = null;
	}

	/**
	 * Dodaje sasiednie pola do neighbors
	 * @param board tablica, na ktorej wystepuje pole i jego sasiedzi
	 */
	public void addNeighbors(Board board) {
		neighbors = new Field[6];
		if(x % 2 == 1) {
			try {neighbors[0] = board.getField(x - 1, y);} catch(IllegalArgumentException e) {}
			try {neighbors[1] = board.getField(x - 1, y + 1);} catch(IllegalArgumentException e) {}
			try {neighbors[2] = board.getField(x, y - 1);} catch(IllegalArgumentException e) {}
			try {neighbors[3] = board.getField(x, y + 1);} catch(IllegalArgumentException e) {}
			try {neighbors[4] = board.getField(x + 1, y);} catch(IllegalArgumentException e) {}
			try {neighbors[5] = board.getField(x + 1, y + 1);} catch(IllegalArgumentException e) {}
		} else {
			try {neighbors[0] = board.getField(x - 1, y - 1);} catch(IllegalArgumentException e) {}
			try {neighbors[1] = board.getField(x - 1, y);} catch(IllegalArgumentException e) {}
			try {neighbors[2] = board.getField(x, y - 1);} catch(IllegalArgumentException e) {}
			try {neighbors[3] = board.getField(x, y + 1);} catch(IllegalArgumentException e) {}
			try {neighbors[4] = board.getField(x + 1, y - 1);} catch(IllegalArgumentException e) {}
			try {neighbors[5] = board.getField(x + 1, y);} catch(IllegalArgumentException e) {}
		}
	}

	/**
	 * Sprawdza czy podane pole jest sasiadem tego pola
	 * @param field pole, ktore jest sprawdzane
	 * @return informacja czy dane pole jest sasiadem, tego pola
	 */
	public boolean isNeighbor(Field field) {
		for(int i = 0; i < 6; i++) {
			if(field.equals(neighbors[i])) return true;
		}
		return false;
	}

	public void setDistance(int corner, int distance) {
		this.distance[corner] = distance;
	}

	/**
	 * @param index indeks sasiada zgodny z zasadami przechowywania
	 * @return pole sasiada o danym indeksie
	 */
	public Field getNeighbor(int index) {
		return neighbors[index];
	}

	public int getDistance(int corner) {
		return distance[corner];
	}

	/**
	 * @return wspolrzedna x pola
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return wspolrzedna y pola
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return typ pola
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return pionek bedacy na danym polu, null jezeli na polu nie ma pionka
	 */
	public Piece getPiece() {return piece;}
}
