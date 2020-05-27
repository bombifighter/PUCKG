package game.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
public class StartController {

    @Inject
    private FXMLLoader fxmlLoader;

    @FXML
    private TextField player1NameTextField;

    @FXML
    private TextField player2NameTextField;

    @FXML
    private Label errorLabel1;

    @FXML
    private Label errorLabel2;

    @FXML
    private TextArea ruleArea;

    @FXML
    private Button rulesButton;

    @FXML
    public void initialize() {
        ruleArea.setVisible(false);
    }

    public void startAction(ActionEvent actionEvent) throws IOException {
        if(player1NameTextField.getText().isEmpty()) {
            errorLabel1.setText("Enter Player1's name!");
        } else if(player2NameTextField.getText().isEmpty()) {
            errorLabel2.setText("Enter Player2's name!");
        } else {
            fxmlLoader.setLocation(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().setPlayer1Name(player1NameTextField.getText());
            fxmlLoader.<GameController>getController().setPlayer2Name(player2NameTextField.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            log.info("The players' name are set to {} and {}, loading game scene...", player1NameTextField.getText(), player2NameTextField.getText());
        }
    }

    public void handleRules(ActionEvent actionEvent) {
        if(!ruleArea.visibleProperty().getValue()) {
            ruleArea.setVisible(true);
            rulesButton.setText("Hide rules");
            log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
            log.info("Showing rules...");
        } else {
            ruleArea.setVisible(false);
            rulesButton.setText("Show rules");
            log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
            log.info("Hiding rules...");
        }
    }
}
