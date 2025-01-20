package server.trylma;

/**
 * Klasa tlumaczaca input klienta na instrukcje wykonywane na serwerze
 */
public class Interpreter {
	/**
	 * Metoda tlumaczaca tekst na instrukcje wykonywane na serwerze
	 * @param message teskt do przetlumaczenia
	 * @param server serwer, na ktorym wykonywane sa operacje
	 * @param player klient, ktory wyslal polecenie
	 * @return informacja zwrotna do wyslania do klienta/klientow
	 */
	public static String respond(String message, ServerApp server, int player) {
		String command = "", args = "";
		try {
			command = message.substring(0, message.indexOf(" "));
			args = message.substring(message.indexOf(' ') + 1);
		} catch(Exception e) {
			command = message;
		}

		switch(command) {
			case "state": return "game " + (server.game.state() ? "" : "not ") + "active";
			case "move":
				try {
					server.game.move(player, args);
					return "[ALL] moved " + args + "\nturn: " + server.game.getActivePlayer() + "\nboard: " + server.game.draw();
				} catch(Exception e) {return e.getMessage();}
			case "skip":
				try {
					server.game.nextPlayer(player);
					return "[ALL] skipped" + "\nturn: " + server.game.getActivePlayer() + "\nboard: " + server.game.draw(); 
				} catch(Exception e) {return e.getMessage();}
			case "board":
				try {return server.game.draw();}
				catch(Exception e) {return e.getMessage();}
			case "activeplayer":
				try {return Integer.toString(server.game.getActivePlayer());}
				catch(Exception e) {return e.getMessage();}
			case "ended":
				try {return "game " + (server.game.ended() ? "" : "not ") + "ended";}
				catch(Exception e) {return e.getMessage();}
			case "result":
				try {return server.game.getResult().toString();}
				catch(Exception e) {return e.getMessage();}
			case "exit":
				return "bye";
			default: return "unknown command";
		}
	}
}
