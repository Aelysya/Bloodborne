package bloodborne;

import bloodborne.system.Game;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadingScreenController {

    @FXML
    BorderPane rootPane;

    @FXML
    ImageView loadingScreenImage;

    public void initialize() {
        loadingScreenImage.fitWidthProperty().bind(rootPane.widthProperty());
        loadingScreenImage.fitHeightProperty().bind(rootPane.heightProperty());

        new Loader().start();
    }

    class Loader extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(1500);
                Platform.runLater(() -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load(), 1280, 720);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Stage stage = new Stage();
                    GameController Controller = fxmlLoader.getController();
                    Game game = new Game(Controller);
                    Controller.setGame(game);
                    stage.setTitle("Bloodborne");
                    stage.setScene(scene);
                    rootPane.getScene().getWindow().hide();
                    stage.setFullScreen(true);
                    stage.show();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
