
package server.trylma;

import java.util.ArrayList;

/**
 * Klasa odpowiadajaca za zarzadzanie gra w poziomu serwera
 */
class GameThread extends Thread {
	/** Instancja serwera, na ktorym odbywa sie gra */
	private ServerApp serverApp;

	/**
	 * Konstruktor klasy
	 * @param serverApp instancja serwera, na ktorym odbywa sie rozgrywka
	 */
	public GameThread(ServerApp serverApp) {
		this.serverApp = serverApp;
	}

	/**
	 * Glowna metoda watku, sprawdzajaca obecny stan gry
	 */
	@Override
	public void run() {
		ArrayList<Integer> prevWinners = new ArrayList<Integer>();
		while (serverApp.game.state()) {
			try {
				ArrayList<Integer> currentWinners = serverApp.game.getResult();
				// jezeli zmienila sie lista zwyciezcow - wypisz ja
				if (!prevWinners.equals(currentWinners))
					serverApp.printForAll("standings: " + serverApp.game.getResult().toString());
				
				// jezeli wszyscy poza jednym graczem wygrali to zakoncz gre
				if(currentWinners.size() == serverApp.players.size() - 1) 
					serverApp.game.end();

				prevWinners = currentWinners;
			} catch (Exception e) {}

			// jezeli na serwerze zostgal jeden gracz to zakoncz gre
			if (serverApp.players.size() == 1) 
				serverApp.game.end();

			try {Thread.sleep(10);} catch (Exception e ) {}
		}

		System.out.println("[SERVER] game ended");
		serverApp.printForAll("ended");
	}
}
