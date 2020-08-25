package client;



import client.Service.ClientService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import server.handler.ClientHandler;
import server.service.ServerImpl;

public class Main extends Application {

        Controller c;

        @Override
        public void start(Stage primaryStage) throws Exception{
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResourceAsStream("sample.fxml"));
            primaryStage.setTitle("ЧатFX");
            c = loader.getController();
            Scene scene = new Scene(root, 500, 500);
            primaryStage.setScene(scene);
            primaryStage.show();
            new ServerImpl();
            primaryStage.setOnCloseRequest(event -> {
                c.Dispose();
                Platform.exit();
                System.exit(0);
            });
        }

   public static void main(String[] args) {launch(args);}
    }