package game.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;
import game.data.GameData;
import game.data.GameDataDao;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Slf4j
public class HighScoreController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameDataDao gameDataDao;

    @FXML
    private TableView<GameData> highScoreTable;

    @FXML
    private TableColumn<GameData, String> winner;

    @FXML
    private TableColumn<GameData, Integer> winnerPoints;

    @FXML
    private TableColumn<GameData, String> second;

    @FXML
    private TableColumn<GameData, Integer> secondPoints;

    @FXML
    private TableColumn<GameData, Duration> duration;

    @FXML
    private TableColumn<GameData, ZonedDateTime> created;

    @FXML
    private Button exitButton;

    @FXML
    private void initialize() {
        log.debug("Loading high scores...");
        List<GameData> highScoreList = gameDataDao.findBestByTime(30);

        winner.setCellValueFactory(new PropertyValueFactory<>("winner"));
        winnerPoints.setCellValueFactory(new PropertyValueFactory<>("winnerPoints"));
        second.setCellValueFactory(new PropertyValueFactory<>("second"));
        secondPoints.setCellValueFactory(new PropertyValueFactory<>("secondPoints"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        created.setCellValueFactory(new PropertyValueFactory<>("created"));

        duration.setCellFactory(column -> {
            TableCell<GameData, Duration> cell = new TableCell<GameData, Duration>() {
                @Override
                protected void updateItem(Duration item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty) {
                        setText(null);
                    } else {
                        setText(DurationFormatUtils.formatDuration(item.toMillis(),"H:mm:ss"));
                    }
                }
            };
            return cell;
        });

        created.setCellFactory(column -> {
            TableCell<GameData, ZonedDateTime> cell = new TableCell<>() {
                private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

                @Override
                protected void updateItem(ZonedDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.format(formatter));
                    }
                }
            };
            return cell;
        });

        ObservableList<GameData> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(highScoreList);

        highScoreTable.setItems(observableResult);
    }

    public void handleRestartButton(ActionEvent actionEvent) throws IOException {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Loading launch scene...");
        fxmlLoader.setLocation(getClass().getResource("/fxml/start.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleByPointButton (ActionEvent actionEvent) {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Loading results ordered by winning player's score...");
        List<GameData> highScoreList = gameDataDao.findBestByPoint(30);

        ObservableList<GameData> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(highScoreList);

        highScoreTable.setItems(observableResult);
    }

    public void handleByTimeButton (ActionEvent actionEvent) {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Loading results ordered by game duration...");
        List<GameData> highScoreList = gameDataDao.findBestByTime(30);

        ObservableList<GameData> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(highScoreList);

        highScoreTable.setItems(observableResult);
    }

    public void handleExitButton (ActionEvent actionEvent) {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Exiting game...");
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
