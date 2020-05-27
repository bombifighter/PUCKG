package game.data;

import com.google.inject.persist.Transactional;
import game.util.jpa.GenericJpaDao;

import java.util.List;

/**
 * DAO class for the {@link GameData} entity.
 */
public class GameDataDao extends GenericJpaDao<GameData> {

    /**
     * Constructor for the DAO class.
     */
    public GameDataDao() {
        super(GameData.class);
    }

    /**
     * Returns the list of {@code n} best results with respect to the time
     * spent for solving the puzzle.
     *
     * @param n the maximum number of results to be returned
     * @return the list of {@code n} best results with respect to the time
     * spent for solving the puzzle
     */
    @Transactional
    public List<GameData> findBestByTime(int n) {
        return entityManager.createQuery("SELECT r FROM GameData r ORDER BY r.duration ASC, r.created DESC", GameData.class)
                .setMaxResults(n)
                .getResultList();
    }

    /**
     * Returns the list of {@code n} best results with respect to the winning
     * player's final score.
     *
     * @param n the maximum number of results to be returned
     * @return the list of {@code n} best results with respect to the winning
     * player's final score
     */
    @Transactional
    public List<GameData> findBestByPoint(int n) {
        return entityManager.createQuery("SELECT r FROM GameData r ORDER BY r.winnerPoints DESC, r.duration ASC", GameData.class)
                .setMaxResults(n)
                .getResultList();
    }
}
