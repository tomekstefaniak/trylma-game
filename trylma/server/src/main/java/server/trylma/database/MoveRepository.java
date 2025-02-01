package server.trylma.database;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MoveRepository extends JpaRepository<Move, Integer> {
	@Query("SELECT m.* FROM Move m WHERE m.port =: port AND m.game =: game")
	ArrayList<Move> findGame(@Param("port") Integer port, @Param("game") Integer game);
}