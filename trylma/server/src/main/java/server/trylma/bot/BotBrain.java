package server.trylma.bot;

import java.util.ArrayList;
import java.util.Collections;

import server.trylma.*;
import server.trylma.game.*;

public class BotBrain {
	private ServerApp server;
	private GameEngine engine;
	private int player;

	public BotBrain(ServerApp server, GameEngine engine, int player) {
		this.server = server;
		this.engine = engine;
		this.player = player;
	}

	public void move() {
		tryMove(new ArrayList<Field>());
	}

	public void tryMove(ArrayList<Field> excludedFields) {
		Board board = engine.getBoard();

		if(board.getWinnerList().contains(player)) {
			try {
				engine.nextPlayer(player);
				server.printForAll("[ALL] skipped" + "\nturn: " + server.game.getActivePlayer() + "\nboard: " + server.game.draw());
			} catch(Exception e) {}
		}

		ArrayList<Piece> pieces = board.getPieces(player);
		Collections.shuffle(pieces);

		int maxGain = Integer.MIN_VALUE;
		Field startField = null;
		Field finishField = null;
		for(Piece piece : pieces) {	
			int x = piece.getX(), y = piece.getY();
			Field currentField = board.getField(x, y);

			int destination = piece.getDestination();
			int currentDistance = currentField.getDistance(destination - 1);

			ArrayList<Field> possibleMoves = board.possibleMove(currentField);
			for(Field field : possibleMoves) {
				if(!excludedFields.contains(field)) {
					int distance = field.getDistance(destination - 1);
					int delta = currentDistance - distance;
	
					if(delta > maxGain) {
						maxGain = delta;
						startField = currentField;
						finishField = field;
					}
				}
			}
		}

		if(finishField == null) {
			try {
				engine.nextPlayer(player);
				server.printForAll("[ALL] skipped" + "\nturn: " + server.game.getActivePlayer() + "\nboard: " + server.game.draw());
			} catch(Exception e) {}
		} else {
			int xS = startField.getX(), yS = startField.getY();
			int xF = finishField.getX(), yF = finishField.getY();

			try {
				String args = xS + "," + yS + "-" + xF + "," + yF;
				engine.move(player, args);
				System.out.println("Bot " + player + " > move " + xS + "," + yS + "-" + xF + "," + yF);
				server.printForAll("[ALL] moved " + args + "\nturn: " + server.game.getActivePlayer() + "\nboard: " + server.game.draw());
			} catch (Exception e) {
				excludedFields.add(finishField);
				tryMove(excludedFields);
			}
		}
	}
}