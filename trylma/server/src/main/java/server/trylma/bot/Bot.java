package server.trylma.bot;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import server.trylma.*;

public class Bot {
	private static String[] botNicknames = {
        "Shinpiung", "Bożydar", "Uzghul", "Zutgrud", "Bocian", "Prycz"
    };

	public String nickname;
	private int id;
	private ServerApp server;
	private GameEngine engine;
	private BotBrain brain;

	public Bot(int botID, ServerApp server, GameEngine engine) {
		this.nickname = botNicknames[botID];
		this.server = server;
		this.engine = engine;
		this.id = -1;
	}

	public void inform(String msg) {
		List<String> responseParsed = Arrays.stream(msg.split("\\s+"))
                                            .filter(s -> !s.isEmpty())
                                            .collect(Collectors.toList());

		if (responseParsed.get(0).startsWith("0:")) {
			this.brain = new BotBrain(server, engine, id);
			if (engine.getActivePlayer() == id) {
				brain.move();
			}
		}
		
		if (responseParsed.size() > 1) {
			if (responseParsed.get(1).equals("moved")) {
				if (Integer.parseInt(responseParsed.get(4)) == id) {
					brain.move();
				}
			} else if(responseParsed.get(1).equals("skipped")) {
				if (Integer.parseInt(responseParsed.get(3)) == id) {
					brain.move();
				}
			}
		}

        // // Obsługa różnych typów wiadomości od serwera
        // switch (responseParsed.get(0)) {
        //     case "[ALL]":
        //         if (responseParsed.size() > 1) {
        //             if (responseParsed.get(1).equals("moved")) {
		// 				if (Integer.parseInt(responseParsed.get(4)) == id) {
		// 					new BotBrain(server, engine, id).move();
		// 				}
		// 			} else if(responseParsed.get(1).equals("skipped")) {
		// 				if (Integer.parseInt(responseParsed.get(3)) == id) {
		// 					new BotBrain(server, engine, id).move();
		// 				}
		// 			}
		// 		}
        //         break;
        // }
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}
}