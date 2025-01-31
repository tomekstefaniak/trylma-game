package server.trylma;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import server.trylma.components.GameEngine;

public class GameEngineTest {
	@Test
	public void testState() {
		GameEngine game = new GameEngine();
		boolean result = game.state();
		assertEquals(false, result);
	}

	@Test
	public void testStartSuccessC() {
		new GameEngine().start('c', 2);
	}

	@Test
	public void testStartSuccessO() {
		new GameEngine().start('o', 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testStartFailureState() {
		GameEngine game = new GameEngine();
		game.start('c', 2);
		game.start('c', 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testStartFailureType() {
		new GameEngine().start('a', 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testStartFailurePlayers() {
		new GameEngine().start('c', 1);
	}

	@Test
	public void testMoveSuccess() {
		GameEngine game = new GameEngine();
		game.start('c', 2);

		int activePlayer = game.getActivePlayer();
		if(activePlayer == 1) game.nextPlayer(activePlayer);

		game.move(0, "3,4-4,4");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMoveFailureState() {
		new GameEngine().move(0, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMoveFailurePlayer() {
		GameEngine game = new GameEngine();
		game.start('c', 2);

		int activePlayer = game.getActivePlayer();
		if(activePlayer == 0) game.nextPlayer(activePlayer);

		game.move(0, "3,4-4,4");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMoveFailureField() {
		GameEngine game = new GameEngine();
		game.start('c', 2);

		int activePlayer = game.getActivePlayer();
		if(activePlayer == 1) game.nextPlayer(activePlayer);

		game.move(0, "a");
	}

	@Test
	public void testNextPlayerSuccess() {
		GameEngine game = new GameEngine();
		game.start('c', 2);
		
		int activePlayer = game.getActivePlayer();
		game.nextPlayer(activePlayer);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNextPlayerFailure() {
		GameEngine game = new GameEngine();
		game.start('c', 2);

		int player = (game.getActivePlayer() + 1) % 2;
		game.nextPlayer(player);
	}
}