package server.trylma.game;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FieldTest {
	@Test
	public void testSetPieceSuccess() {
		Field field = new Field(0, 0, 0);
		Piece piece = new Piece(0, 0, 0, 0);
		field.setPiece(piece);
		Piece result = field.getPiece();

		assertEquals(piece, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetPieceFailure() {
		Field field = new Field(0, 0, 0);
		Piece piece1 = new Piece(0, 0, 0, 0);
		field.setPiece(piece1);

		Piece piece2 = new Piece(0, 0, 1, 1);
		field.setPiece(piece2);
	}

	@Test
	public void testRemovePieceSuccess() {
		Field field = new Field(0, 0, 0);
		Piece piece = new Piece(0, 0, 0, 0);
		field.setPiece(piece);

		field.removePiece();
		assertEquals(null, field.getPiece());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemovePieceFailure() {
		Field field = new Field(0, 0, 0);
		field.removePiece();
	}

	@Test
	public void testIsNeighborTrue() {
		ClassicBoard board = new ClassicBoard();
		boolean result = board.getField(3, 4).isNeighbor(board.getField(4, 4));
		assertEquals(true, result);
	}

	@Test
	public void testIsNeighborFalse() {
		ClassicBoard board = new ClassicBoard();
		boolean result = board.getField(3, 4).isNeighbor(board.getField(4, 3));
		assertEquals(false, result);
	}
}