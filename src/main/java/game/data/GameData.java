package game.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Class representing the result of a game played by two specific players.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GameData {

    @Id
    @GeneratedValue
    private long id;

    /**
     * The name of the winner of the game.
     */
    @Column(nullable = false)
    private String winner;

    /**
     * The points of the winner of the game.
     */
    @Column(nullable = false)
    private int winnerPoints;

    /**
     * The name of the second placed player.
     */
    @Column(nullable = false)
    private String second;

    /**
     * The points of the second placed player.
     */
    @Column(nullable = false)
    private int secondPoints;

    /**
     * The duration of the game.
     */
    @Column(nullable = false)
    private Duration duration;

    /**
     * The timestamp when the result was saved.
     */
    @Column(nullable = false)
    private ZonedDateTime created;

    @PrePersist
    protected void onPersist() {
        created = ZonedDateTime.now();
    }
}
