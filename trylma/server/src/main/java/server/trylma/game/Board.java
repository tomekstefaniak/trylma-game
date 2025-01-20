package server.trylma.game;

import java.util.ArrayList;

/**
 * Interfejs planszy
 */
public interface Board {
	/**
	 * Tworzy plansze dla danej wysokosci i szerokosci
	 * @param height wysokosc planszy
	 * @param width szerokosc planszy
	 * @return tablica dwuwymiarowa pol o danej wysokosci i szerokosci uzupelniona odpowiednimi polami
	 */
	public static Field[][] createBoard(int height, int width) {
		Field[][]board = new Field[height][width];
		
		for (int i = 0; i < width; i++) {
			if (i == 6) {
				board[0][i] = new Field(0, i, 1); 
				board[16][i] = new Field(16, i, 4);
			} else {
				board[0][i] = new Field(0, i, -1); 
				board[16][i] = new Field(16, i, -1);
			}
		}

		for (int i = 0; i < width; i++) {
			if (i == 5 || i == 6) {
				board[1][i] = new Field(1, i, 1); 
				board[15][i] = new Field(15, i, 4);
			} else {
				board[1][i] = new Field(1, i, -1); 
				board[15][i] = new Field(15, i, -1);
			}
		}

		for (int i = 0; i < width; i++) {
			if (i <= 4 || i >= 8) {
				board[2][i] = new Field(2, i, -1); 
				board[14][i] = new Field(14, i, -1);
			} else {
				board[2][i] = new Field(2, i, 1); 
				board[14][i] = new Field(14, i, 4);
			}
		}

		for (int i = 0; i < width; i++) {
			if (i <= 3 || i >= 8) {
				board[3][i] = new Field(3, i, -1); 
				board[13][i] = new Field(13, i, -1);
			} else {
				board[3][i] = new Field(3, i, 1); 
				board[13][i] = new Field(13, i, 4);
			}
		}

		for (int i = 0; i < width; i++) {
			if (i <= 3) {
				board[4][i] = new Field(4, i, 6); 
				board[12][i] = new Field(12, i, 5);
			} else if (i <= 8) {
				board[4][i] = new Field(4, i, 0); 
				board[12][i] = new Field(12, i, 0);
			} else {
				board[4][i] = new Field(4, i, 2); 
				board[12][i] = new Field(12, i, 3);
			}
		}

		for (int i = 0; i < width; i++) {
			if (i <= 2) {
				board[5][i] = new Field(5, i, 6); 
				board[11][i] = new Field(11, i, 5);
			} else if (i <= 8) {
				board[5][i] = new Field(5, i, 0); 
				board[11][i] = new Field(11, i, 0);
			} else if (i <= 11) {
				board[5][i] = new Field(5, i, 2); 
				board[11][i] = new Field(11, i, 3);
			} else {
				board[5][i] = new Field(5, i, -1); 
				board[11][i] = new Field(11, i, -1);
			}
		}

		for (int i = 0; i < width; i++) {
			if (i == 0 || i == 12) {
				board[6][i] = new Field(6, i, -1); 
				board[10][i] = new Field(10, i, -1);
			} else if (i <= 2) {
				board[6][i] = new Field(6, i, 6); 
				board[10][i] = new Field(10, i, 5);
			} else if (i <= 9) {
				board[6][i] = new Field(6, i, 0); 
				board[10][i] = new Field(10, i, 0);
			} else {
				board[6][i] = new Field(6, i, 2); 
				board[10][i] = new Field(10, i, 3);
			}
		}

		for (int i = 0; i < width; i++) {
			if (i == 1) {
				board[7][i] = new Field(7, i, 6); 
				board[9][i] = new Field(9, i, 5);
			} else if (i == 10) {
				board[7][i] = new Field(7, i, 2); 
				board[9][i] = new Field(9, i, 3);
			} else if (i >= 2 && i <= 9) {
				board[7][i] = new Field(7, i, 0); 
				board[9][i] = new Field(9, i, 0);
			} else {
				board[7][i] = new Field(7, i, -1); 
				board[9][i] = new Field(9, i, -1);
			}
		}

		for (int i = 0; i < width; i++) {
			if (i >= 2 && i <= 10) {
				board[8][i] = new Field(8, i, 0);
			}
			else {
				board[8][i] = new Field(8, i, -1);
			}
		}

		return board;
	}

	/**
	 * Wypelnia odpowiednio plasze pionkami
	 * @param players ilosc graczy, dla ktorej ma byc wypelniona plansza
	 */
	public void fillBoard(int players);

	/**
	 * Rysuje obecny stan planszy
	 * @return tekst z obecnym stanem planszy, w formacie 'pola w rzedzie'/'pola w kolejnym rzedzie' ..., pola bez pionkow oznaczone przez 'O', pionki oznaczone numerem gracza
	 */
	public String draw();

	/**
	 * Wykonuje ruch gracza 
	 * @param player gracz, ktory wykonuje ruch 
	 * @param xS wspolrzedna x poczatkowego pola
	 * @param yS wspolrzedna y poczatkowego pola
	 * @param xF wspolrzedna x koncowego pola
	 * @param yF wspolrzedna y koncowego pola
	 * @throws IllegalArgumentException jezeli nie mozna wykonac ruchu (zly gracz, zle pole, inne pionki, nielegalny ruch)
	 */
	public void move(int player, int xS, int yS, int xF, int yF) throws IllegalArgumentException;

	/**
	 * Zwraca pole z danymi wspolrzednymi
	 * @param x wsporzedna x
	 * @param y wspolrzedna y
	 * @return pole pod danymi wspolrzednymi
 	 * @throws IllegalArgumentException jezeli podane wspolrzedne nie naleza do planszy
	 */
	public Field getField(int x, int y) throws IllegalArgumentException;
	
	/**
	 * @return lista graczy, ktora ukonczyla gre, w kolejnosci ukonczenia
	 */
	public ArrayList<Integer> getWinnerList();
}
