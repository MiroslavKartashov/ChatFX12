package client;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class Controller extends Application {

    @FXML
    public
    TextArea textArea;
    @FXML
    TextField textField;

    public void sendMassage() {
        textArea.appendText(textField.getText()+"\n");
        textField.clear();
        textField.requestFocus();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}






