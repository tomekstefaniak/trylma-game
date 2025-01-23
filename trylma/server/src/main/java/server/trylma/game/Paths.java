package server.trylma.game;

public class Paths {
	private Board board;
	private boolean[][] visited;
	private int width;
	private int height;

	public Paths(Board board, int width, int height) {
		this.board = board;
		this.height = height;
		this.width = width;
		fillDistances();
	}

	private void fillDistances() {
		int[][] corners = {{0,6}, {4, 12}, {12, 12}, {16, 6}, {12, 0}, {4, 0}};
		for (int c = 0; c < 6; c++) {
			visited = new boolean[height][width];
			board.getField(corners[c][0], corners[c][1]).setDistance(c, 0);
			fillCornerDistance(corners[c][0], corners[c][1], c);
		}
	}

	private void fillCornerDistance(int x, int y, int corner) {
		Field field = board.getField(x, y);
		visited[x][y] = true;

		// przejdz po sasiadach i zmien ich odleglosci
		for (int i = 0; i < 6; i++) {
			Field neighbor = field.getNeighbor(i);
			if (neighbor != null && neighbor.getType() != -1) {
				if (field.getDistance(corner) + 1 < neighbor.getDistance(corner)) {
					neighbor.setDistance(corner, field.getDistance(corner) + 1);
				}
			}
		}

		// przejdz po sasiadach i wywolaj rekurencyjnie ta metode dla nich
		for (int i = 0; i < 6; i++) {
			Field neighbor = field.getNeighbor(i);
			if (neighbor != null && neighbor.getType() != -1) {
				if (!visited[neighbor.getX()][neighbor.getY()]) {
					fillCornerDistance(neighbor.getX(), neighbor.getY(), corner);
				} 
			}
		}
	}
}