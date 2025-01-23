package server.trylma.game;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PieceTest {
	@Test
	public void testChangePosition() {
		Piece piece = new Piece(1, 2, 0, 0);
		piece.changePosition(3,4);

		int resultX = piece.getX();
		int resultY = piece.getY();

		assertEquals(3, resultX);
		assertEquals(4, resultY);
	}	
}