package server.trylma.database;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "moves")
@Getter
@Setter
@NoArgsConstructor
public class Move {
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "port")
	private Integer port;

	@Column(name = "game")
	private Integer game;

	@Column(name = "board")
	private String board;

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setGame(Integer game) {
		this.game = game;
	}

	public void setBoard(String board) {
		this.board = board;
	}
}