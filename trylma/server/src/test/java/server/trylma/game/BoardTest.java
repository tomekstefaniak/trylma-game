package server.trylma.game;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

public class BoardTest {
	@Test
	public void testCreateBoard() {
		ClassicBoard board = new ClassicBoard();
		
		Field field1 = board.getField(0, 0); // type: -1
		Field field2 = board.getField(0, 6); // type: 1
		Field field3 = board.getField(4, 4); // type: 0

		int result1 = field1.getType();
		int result2 = field2.getType();
		int result3 = field3.getType();

		assertEquals(-1, result1);
		assertEquals(1, result2);
		assertEquals(0, result3);
	}

	@Test
	public void testFillBoard() {
		ClassicBoard board = new ClassicBoard();
		board.fillBoard(2);

		Piece piece1 = board.getField(0, 6).getPiece(); // player: 0
		Piece piece2 = board.getField(16, 6).getPiece(); // player: 1

		int result1 = piece1.getPlayer();
		int result2 = piece2.getPlayer();

		assertEquals(0, result1);
		assertEquals(1, result2);
	}

	@Test
	public void testMoveSuccessNeighbor() {
		ClassicBoard board = new ClassicBoard();
		board.fillBoard(2);
		Piece piece = board.getField(3, 4).getPiece();

		board.move(0, 3, 4, 4, 4);

		Piece result1 = board.getField(3, 4).getPiece();
		Piece result2 = board.getField(4, 4).getPiece();

		assertEquals(null, result1);
		assertEquals(piece, result2);
	}

	@Test
	public void testMoveSuccessJump() {
		ClassicBoard board = new ClassicBoard();
		board.fillBoard(2);
		Piece piece = board.getField(2, 5).getPiece();

		board.move(0, 2, 5, 4, 4);

		Piece result1 = board.getField(2, 5).getPiece();
		Piece result2 = board.getField(4, 4).getPiece();

		assertEquals(null, result1);
		assertEquals(piece, result2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMoveFailureInvalidField() {
		ClassicBoard board = new ClassicBoard();
		board.fillBoard(2);
		board.move(0, 0, 0, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMoveFailureNoPiece() {
		ClassicBoard board = new ClassicBoard();
		board.fillBoard(2);
		board.move(0, 4, 4, 3, 4);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMoveFailureWrongPiece() {
		ClassicBoard board = new ClassicBoard();
		board.fillBoard(2);
		board.move(1, 3, 4, 4, 4);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMoveFailureOtherPiece() {
		ClassicBoard board = new ClassicBoard();
		board.fillBoard(2);
		board.move(0, 3, 4, 3, 5);
	}

	@Test
	public void testGetWinnerList() {
		ClassicBoard board = new ClassicBoard();
		ArrayList<Integer> result = board.getWinnerList();

		assertEquals(new ArrayList<Integer>(), result);
	}
}