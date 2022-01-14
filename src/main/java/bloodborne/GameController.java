package bloodborne;

import bloodborne.entities.Hunter;
import bloodborne.items.Rune;
import bloodborne.system.Game;
import bloodborne.world.Place;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class GameController {
    @FXML
    AnchorPane entireWindow;

    @FXML
    AnchorPane HUDAnchorPane;

    @FXML
    Polygon northArrow, southArrow, eastArrow, westArrow;

    @FXML
    TextArea displayScreen;

    @FXML
    TextArea trickWeaponText, gunText;

    @FXML
    TextField writeLine;

    @FXML
    TextField currentHP, bloodEchoesText, vialAmountText, bulletAmountText;

    @FXML
    TextField dmgBoostText, boostLeftText, dodgeRateText, hitRateText, visceralRateText;

    @FXML
    ImageView imageID;

    @FXML
    ImageView trickWeaponImage, gunImage, rune1, rune2, rune3;

    @FXML
    BorderPane borderpane;

    private Game game;
    private final Lock LOCK = new ReentrantLock(true);
    private List<String> lastCommands;
    private int previousCommandIndex;

    public void setGame(Game game) {
        this.game = game;
    }

    public void updateDirectionalArrows(Place place){
        northArrow.setVisible(place.getEXITS().containsKey("north"));
        eastArrow.setVisible(place.getEXITS().containsKey("east"));
        westArrow.setVisible(place.getEXITS().containsKey("west"));
        southArrow.setVisible(place.getEXITS().containsKey("south"));
    }

    public void updateHUD(Hunter hunter){
        currentHP.setText("HP: " + hunter.getHealthPoints() + "/30");
        if(hunter.getHealthPoints() == 0){
            currentHP.setStyle("-fx-text-fill: red;");
        }
        bloodEchoesText.setText(Integer.toString(hunter.getBloodEchoes()));
        vialAmountText.setText(Integer.toString(hunter.getVialsNumber()));
        bulletAmountText.setText(Integer.toString(hunter.getBulletsNumber()));
        dmgBoostText.setText("Damage boost: " + hunter.getDamageBoost());
        boostLeftText.setText("Boost left: " + hunter.getBoostLeft());
        dodgeRateText.setText("Dodge rate: " + hunter.getDodgeRate());
        hitRateText.setText("Hit rate: " + hunter.getHitRate());
        visceralRateText.setText("Visceral rate: " + hunter.getVisceralRate());
    }

    public void updateWeapons(Hunter hunter){
        trickWeaponImage.setImage(new Image(String.valueOf(getClass().getResource(hunter.getTrickWeaponIcon()))));
        gunImage.setImage(new Image(String.valueOf(getClass().getResource(hunter.getFireArmIcon()))));
        if(hunter.getTrickWeapon() == null){
            trickWeaponText.setText("No weapon equipped\n1 damage");
        } else {
            trickWeaponText.setText(hunter.getTrickWeapon().getNAME() + "\n" + hunter.getTrickWeapon().getCurrentDamage() + " damage");
        }
        if(hunter.getFireArm() == null){
            gunText.setText("No weapon equipped");
        } else {
            gunText.setText(hunter.getFireArm().getNAME() + "\n" + hunter.getFireArm().getCurrentDamage() + " damage");
        }
        updateHUD(hunter);
    }

    public void updateRunes(Hunter hunter) {
        List<Rune> runes = hunter.getRUNE_LIST();
        if (runes.size() >= 1){
            rune1.setImage(new Image(String.valueOf(getClass().getResource("images/runes/" + runes.get(0).getIcon()))));
            if (runes.size() >= 2){
                rune2.setImage(new Image(String.valueOf(getClass().getResource("images/runes/" + runes.get(1).getIcon()))));
                if (runes.size() >= 3){
                    rune3.setImage(new Image(String.valueOf(getClass().getResource("images/runes/" + runes.get(2).getIcon()))));
                } else {
                    rune3.setImage(new Image(String.valueOf(getClass().getResource("images/empty.png"))));
                }
            } else {
                rune2.setImage(new Image(String.valueOf(getClass().getResource("images/empty.png"))));
            }
        } else {
            rune1.setImage(new Image(String.valueOf(getClass().getResource("images/empty.png"))));
        }
    }

    public void onKeyPressedWriteLine(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            displayScreen.appendText(">> " + writeLine.getText() + "\n");
            game.analyseText(writeLine.getText());
            lastCommands.add(writeLine.getText());
            previousCommandIndex = lastCommands.size()-1;
            writeLine.clear();
        } else if (keyEvent.getCode().equals(KeyCode.UP)){
            if (previousCommandIndex >= 0){
                writeLine.setText(lastCommands.get(previousCommandIndex));
                writeLine.positionCaret(writeLine.getText().length());
                previousCommandIndex--;
            }
        } else if (keyEvent.getCode().equals(KeyCode.DOWN)){
            if (lastCommands.size() !=0 && previousCommandIndex != lastCommands.size()-1){
                writeLine.setText(lastCommands.get(previousCommandIndex+1));
                writeLine.positionCaret(writeLine.getText().length());
                previousCommandIndex++;
            }
        }
    }

    public void writeInstantly(String txt) {
        displayScreen.appendText(txt + "\n");
    }

    public void writeLetterByLetter(String txt){
        writeLine.setDisable(true);
        Thread myThread = new Thread(() -> {
            LOCK.lock();
            char[] characters = txt.toCharArray();
            for(int j=0; j<txt.length(); j++){
                try{
                    sleep(15);
                    displayScreen.appendText("" + characters[j]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            /*for( char leChar : characters){
                try {
                    sleep(15);
                    displayScreen.appendText(leChar);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
            displayScreen.appendText("\n");
            LOCK.unlock();
            writeLine.setDisable(false);
        });
        myThread.start();
    }

    public void changeImage(String url){
        String path = "images/" + url;
        Image myImage = new Image(String.valueOf(getClass().getResource(path)));
        imageID.setImage(myImage);
    }

    public void transitionImage(String imageURL)  {
        final Rectangle rect1 = new Rectangle(0, 0, borderpane.getWidth(),  borderpane.getHeight());
        final Rectangle rect2 = new Rectangle(0, 0, borderpane.getWidth(),  borderpane.getHeight());
        rect1.setFill(Color.BLACK);
        rect2.setFill(Color.BLACK);
        borderpane.getChildren().add(rect1);
        FadeTransition ft = new FadeTransition(Duration.millis(2000), rect1);
        ft.setFromValue(0.1);
        ft.setToValue(1.0);
        ft.setCycleCount(1);

        FadeTransition ft2 = new FadeTransition(Duration.millis(2000), rect2);
        ft2.setFromValue(1.0);
        ft2.setToValue(0.1);
        ft2.setCycleCount(1);
        ft2.setAutoReverse(true);
        ft2.setOnFinished(actionEvent -> borderpane.getChildren().remove(rect2));
        ft.setOnFinished(e -> {
            changeImage(imageURL);
            borderpane.getChildren().add(rect2);
            ft2.play();
            borderpane.getChildren().remove(rect1);
        });
        ft.play();
    }

    public void deathTransition() {
        Rectangle rect1 = new Rectangle(0, 0, borderpane.getWidth(),  borderpane.getHeight());
        rect1.setFill(Color.BLACK);
        Rectangle rect2 = new Rectangle(0, 0, borderpane.getWidth(),  borderpane.getHeight());
        rect2.setFill(Color.BLACK);
        Rectangle rect3 = new Rectangle(0, 0, borderpane.getWidth(),  borderpane.getHeight());
        rect3.setFill(Color.BLACK);
        Rectangle rect4 = new Rectangle(0, 0, borderpane.getWidth(),  borderpane.getHeight());
        rect4.setFill(Color.BLACK);
        borderpane.getChildren().add(rect1);

        FadeTransition ft = new FadeTransition(Duration.millis(2000), rect1);
        ft.setFromValue(0.1);
        ft.setToValue(1.0);
        ft.setCycleCount(1);

        FadeTransition ft2 = new FadeTransition(Duration.millis(2000), rect2);
        ft2.setFromValue(1.0);
        ft2.setToValue(0.1);
        ft2.setCycleCount(1);
        ft2.setAutoReverse(true);

        FadeTransition ft3 = new FadeTransition(Duration.millis(2000), rect3);
        ft3.setFromValue(0.1);
        ft3.setToValue(1.0);
        ft3.setCycleCount(1);

        FadeTransition ft4 = new FadeTransition(Duration.millis(2000), rect4);
        ft4.setFromValue(1.0);
        ft4.setToValue(0.1);
        ft4.setCycleCount(1);
        ft4.setAutoReverse(true);

        ft4.setOnFinished(e -> borderpane.getChildren().remove(rect4));
        ft3.setOnFinished(e -> {
            changeImage("zones/hunter's-dream/hunter's-dream.jpg");
            borderpane.getChildren().remove(rect3);
            borderpane.getChildren().add(rect4);
            ft4.play();
        });
        ft2.setOnFinished(e -> {
            borderpane.getChildren().remove(rect2);
            borderpane.getChildren().add(rect3);
            ft3.play();
        });
        ft.setOnFinished(e -> {
            changeImage("you-died.jpg");
            borderpane.getChildren().remove(rect1);
            borderpane.getChildren().add(rect2);
            ft2.play();
        });
        ft.play();
    }

    public void initialize(){
        displayScreen.setFocusTraversable(false);
        displayScreen.setText("Wake up ? [Y/N]\n");
        lastCommands = new ArrayList<>();
        previousCommandIndex = 0;
        northArrow.setOnMouseEntered(event -> northArrow.setStyle("-fx-opacity: 1;"));
        northArrow.setOnMouseExited(event -> northArrow.setStyle("-fx-opacity: 0.6;"));
        northArrow.setOnMouseReleased(event -> game.goFunction("north"));

        southArrow.setOnMouseEntered(event -> southArrow.setStyle("-fx-opacity: 1;"));
        southArrow.setOnMouseExited(event -> southArrow.setStyle("-fx-opacity: 0.6;"));
        southArrow.setOnMouseReleased(event -> game.goFunction("south"));

        eastArrow.setOnMouseEntered(event -> eastArrow.setStyle("-fx-opacity: 1;"));
        eastArrow.setOnMouseExited(event -> eastArrow.setStyle("-fx-opacity: 0.6;"));
        eastArrow.setOnMouseReleased(event -> game.goFunction("east"));

        westArrow.setOnMouseEntered(event -> westArrow.setStyle("-fx-opacity: 1;"));
        westArrow.setOnMouseExited(event -> westArrow.setStyle("-fx-opacity: 0.6;"));
        westArrow.setOnMouseReleased(event -> game.goFunction("west"));
    }
}
