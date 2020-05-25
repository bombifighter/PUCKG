package game.data;

import com.google.inject.persist.Transactional;
import game.util.jpa.GenericJpaDao;

import java.util.List;

public class GameDataDao extends GenericJpaDao<GameData> {

    public GameDataDao() {
        super(GameData.class);
    }

    @Transactional
    public List<GameData> findBest(int n) {
        return entityManager.createQuery("SELECT r FROM GameData r ORDER BY r.duration ASC, r.created DESC", GameData.class)
                .setMaxResults(n)
                .getResultList();
    }
}
