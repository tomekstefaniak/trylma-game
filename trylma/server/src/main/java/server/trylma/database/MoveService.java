package server.trylma.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoveService {
	private final MoveRepository moveRepository;

	@Autowired
	public MoveService(MoveRepository moveRepository) {
		this.moveRepository = moveRepository;
	}

	public Move saveMove(Integer port, Integer game, String board) {
		Move move = new Move();
		move.setPort(port);
		move.setGame(game);
		move.setBoard(board);
		return moveRepository.save(move);
	}
}
