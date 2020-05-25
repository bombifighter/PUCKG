package game.javafx.controller;

import game.data.GameData;
import game.data.GameDataDao;
import game.state.TableState;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.inject.Inject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
public class GameController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameDataDao gameDataDao;

    private String player1Name;
    private String player2Name;
    private int player = 1;
    private TableState tableState;
    private Instant startTime;
    private List<Image> cellImages;

    @FXML
    private Label infoLabel;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Label stopWatchLabel;

    @FXML
    private Timeline stopWatchTimeLine;

    private BooleanProperty gameOver = new SimpleBooleanProperty();

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    @FXML
    public void initialize() {
        cellImages = List.of(
                new Image(getClass().getResource("/images/cell0.png").toExternalForm()),
                new Image(getClass().getResource("/images/cell1.png").toExternalForm()),
                new Image(getClass().getResource("/images/cell2.png").toExternalForm()),
                new Image(getClass().getResource("/images/cell3.png").toExternalForm())
        );
        gameOver.addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                gameDataDao.persist(createGameData());
                stopWatchTimeLine.stop();
            }
        });
        tableState = new TableState();
        startTime = Instant.now();
        gameOver.setValue(false);
        createStopWatch();
        Platform.runLater(() -> infoLabel.setText(player1Name + " vs " + player2Name));
        displayGameState();
    }

    private void displayGameState() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                ImageView view = (ImageView) gameGrid.getChildren().get(i * 6 + j);
                if(view.getImage() != null) {
                    //log.trace("Image({}, {}) = {}", i, j, view.getImage().getUrl());
                }
                view.setImage(cellImages.get(tableState.getTable()[i][j].getValue()));
            }
        }
    }

    public void handleClickOnCell(MouseEvent mouseEvent) {
        int row = GridPane.getRowIndex((Node) mouseEvent.getSource());
        int col = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        if(!tableState.isFinished() && tableState.isEmptyCell(row, col)) {
            tableState.newPuck(player,row,col);
            if(player == 1) {
                player = 2;
            } else player = 1;
            if (tableState.isFinished()) {
                gameOver.setValue(true);
                infoLabel.setText("Congratulations, " + (player + 1) % 2 + 1 + "!");
            }
        } else if(!tableState.isFinished() && tableState.isPuckOfPlayer(player, row, col)) {
            int newRow = GridPane.getRowIndex((Node) mouseEvent.getSource());
            int newCol = GridPane.getColumnIndex((Node) mouseEvent.getSource());
            tableState.movePuck(player, row, col, newRow, newCol);
            if(player == 1) {
                player = 2;
            } else player = 1;
            if (tableState.isFinished()) {
                gameOver.setValue(true);
                infoLabel.setText("Congratulations, " + (player + 1) % 2 + 1 + "!");

            }
        }
        displayGameState();
    }

    private void createStopWatch () {
        stopWatchTimeLine = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            long millisElapsed = startTime.until(Instant.now(), ChronoUnit.MILLIS);
            stopWatchLabel.setText(DurationFormatUtils.formatDuration(millisElapsed, "HH:mm:ss"));
        }), new KeyFrame(javafx.util.Duration.seconds(1)));
        stopWatchTimeLine.setCycleCount(Animation.INDEFINITE);
        stopWatchTimeLine.play();
    }

    private GameData createGameData () {
        GameData data = GameData.builder()
                .player1(player1Name)
                .player2(player2Name)
                .duration(java.time.Duration.between(startTime, Instant.now()))
                .build();
        return data;
    }
}
