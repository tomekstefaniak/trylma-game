package server.trylma.game;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GameTest {
	@Test
	public void testGameEndedFalse() {
		ClassicGame game = new ClassicGame(2);
		boolean result = game.gameEnded(2);

		assertEquals(false, result);
	}
}