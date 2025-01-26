package server.trylma.game;

import java.util.ArrayList;

/**
 * Interfejs gry
 */
public interface Game {
    /**
     * Rysuje plansze
     * @return String z plansza w formacie zgodnym z plansza
     */
    public String draw();

    /**
     * Rusza pionkiem
     * @param player gracz wysylajacy zadanie ruchu
     * @param xS wspolrzedna x pola poczatkowego
     * @param yS wspolrzedna y pola poczatkowego
     * @param xF wspolrzedna x pola koncowego
     * @param yF wspolrzedna y pola koncowego
     * @throws IllegalArgumentException jezeli wykonanie takiego ruchu jest niemozliwe
     */
    public void move(int player, int xS, int yS, int xF, int yF) throws IllegalArgumentException;

    /**
     * @return lista graczy, ktorzy wygrali gre w kolejnosci ukonczenia
     */
    public ArrayList<Integer> gameWon();

    /**
     * Sprawdza czy gra zostala zakonczona
     * @param players ilosc graczy bedacych nadal w grze
     * @return stan gry
     */
    public boolean gameEnded(int players);
    
    public Board getBoard();
}
