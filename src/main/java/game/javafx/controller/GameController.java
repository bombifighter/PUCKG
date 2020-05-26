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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
public class GameController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameDataDao gameDataDao;

    private String[] players = new String[2];
    private int[] points = new int[2];
    private int player;
    private TableState tableState;
    private Instant startTime;
    private List<Image> cellImages;
    private int prevRow = -1;
    private int prevCol = -1;

    @FXML
    private Label infoLabel;

    @FXML
    private Label player1Label;

    @FXML
    private Label player2Label;

    @FXML
    private Label player1PointsLabel;

    @FXML
    private Label player2PointsLabel;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Label stopWatchLabel;

    @FXML
    private Timeline stopWatchTimeLine;

    @FXML
    private Button resetButton;

    @FXML
    private Button giveUpButton;

    private BooleanProperty gameOver = new SimpleBooleanProperty();

    public void setPlayer1Name(String player1Name) {
        this.players[0] = player1Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.players[1] = player2Name;
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
                calculatePoints();
                gameDataDao.persist(createGameData());
                stopWatchTimeLine.stop();
                player1Label.setVisible(false);
                player2Label.setVisible(false);
                player1PointsLabel.setVisible(false);
                player2PointsLabel.setVisible(false);
                infoLabel.setText("The winner is\n" + players[oppositePlayer(player)-1] + "!");
                infoLabel.setPrefWidth(500);
            }
        });
        resetGame();
    }

    private void resetGame () {
        tableState = new TableState();
        startTime = Instant.now();
        gameOver.setValue(false);
        createStopWatch();
        Platform.runLater(() -> infoLabel.setText("VS"));
        infoLabel.setPrefWidth(100);
        Platform.runLater(() -> player1Label.setText(">" + players[0] + "<"));
        Platform.runLater(() -> player2Label.setText(players[1]));
        player1Label.setVisible(true);
        player2Label.setVisible(true);
        player1PointsLabel.setVisible(true);
        player2PointsLabel.setVisible(true);
        giveUpButton.setText("Give Up");
        player = 1;
        displayGameState();
    }

    public void handleReset (ActionEvent actionEvent) {
        stopWatchTimeLine.stop();
        resetGame();
    }

    public void handleGiveUp (ActionEvent actionEvent) throws IOException {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        if (buttonText.equals("Give Up")) {

        }
        gameOver.setValue(true);
        fxmlLoader.setLocation(getClass().getResource("/fxml/highscores.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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
        calculatePoints();
        player1PointsLabel.setText(Integer.toString(points[0]));
        player2PointsLabel.setText(Integer.toString(points[1]));
        if(player == 1) {
            player1Label.setText(">" + players[0] + "<");
            player2Label.setText(players[1]);
        } else {
            player1Label.setText(players[0]);
            player2Label.setText(">" + players[1] + "<");
        }
    }

    public void handleClickOnCell (MouseEvent mouseEvent) {
        int row = GridPane.getRowIndex((Node) mouseEvent.getSource());
        int col = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        if(prevRow < 0 && prevCol < 0) {
            if(!tableState.isFinished(player) && tableState.isEmptyCell(row, col)) {
                tableState.newPuck(player, row, col);
                player = oppositePlayer(player);
                if (tableState.isFinished(player)) {
                    gameOver.setValue(true);
                    //infoLabel.setText("Congratulations, " + (player + 1) % 2 + 1 + "!");
                    giveUpButton.setText("Finish");

                }
            }
            if(!tableState.isFinished(player) && tableState.isPuckOfPlayer(player, row, col)) {
                prevRow = row;
                prevCol = col;
            }
        } else {
            if(!tableState.isFinished(player) && tableState.isMovableTo(player, prevRow, prevCol, row, col)) {
                tableState.movePuck(player, prevRow, prevCol, row, col);
                prevRow = -1;
                prevCol = -1;
                player = oppositePlayer(player);
                if (tableState.isFinished(player)) {
                    gameOver.setValue(true);
                    //infoLabel.setText("Congratulations, " + (player + 1) % 2 + 1 + "!");
                    giveUpButton.setText("Finish");
                }
            }
            prevRow = -1;
            prevCol = -1;
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
                .winner(players[oppositePlayer(player) - 1])
                .winnerPoints(points[oppositePlayer(player) - 1])
                .second(players[player - 1])
                .secondPoints(points[player - 1])
                .duration(java.time.Duration.between(startTime, Instant.now()))
                .build();
        return data;
    }

    private void calculatePoints () {
        this.points[0] = tableState.pointsOfPlayer(1);
        this.points[1] = tableState.pointsOfPlayer(2);
    }

    private int oppositePlayer (int player) {
        if(player == 1) {
            return 2;
        } else return 1;
    }

    public void giveUpAction() {

    }
}
