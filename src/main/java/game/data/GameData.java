package game.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GameData {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String winner;

    @Column(nullable = false)
    private int winnerPoints;

    @Column(nullable = false)
    private String second;

    @Column(nullable = false)
    private int secondPoints;

    @Column(nullable = false)
    private Duration duration;

    @Column(nullable = false)
    private ZonedDateTime created;

    @PrePersist
    protected void onPersist() {
        created = ZonedDateTime.now();
    }
}
