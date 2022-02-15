package bloodborne;

import bloodborne.entities.Hunter;
import bloodborne.items.Item;
import bloodborne.items.Rune;
import bloodborne.system.Game;
import bloodborne.world.Place;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameController {

    @FXML
    AnchorPane imageAnchor;

    @FXML
    Polygon northArrow, southArrow, eastArrow, westArrow;

    @FXML
    TextArea console, detailText;

    @FXML
    TextField writeLine, currentHP;

    @FXML
    GridPane inventoryItems;

    @FXML
    Button useButton, throwOneButton, throwAllButton;

    @FXML
    ImageView vialsImage, bulletsImage, placeImage, detailImage, trickWeaponImage, firearmImage, runeOath, rune3, rune2, rune1, headArmorImage, chestArmorImage, legsArmorImage, feetArmorImage, consumablesTab, materialsTab, keysTab, trickWeaponsTab, firearmsTab,  attiresTab, gemsTab, runesTab;

    @FXML
    Label levelText, echoesText, insightText, vitText, endText, arcText, btText, sklText, strText, vialAmountText, bulletAmountText, dmgBoostText, boostLeftText, visceralRateText, hitRateText, dodgeRateText, bulletConsumptionText, trickWeaponNameText, trickWeaponDmgText, firearmNameText, firearmDmgText;

    private Game game;
    private final Lock LOCK = new ReentrantLock(true);
    private List<String> lastCommands;
    private int previousCommandIndex;
    private Hunter hunter;
    private String currentCategory, draggedTrickWeaponName, draggedFirearmName, draggedRuneName;

    public void setGame(Game game) {
        this.game = game;
    }

    public void setHunter(Hunter hunter) {
        this.hunter = hunter;
    }

    public void initialize() {
        console.setFocusTraversable(false);
        console.setText("Wake up ? [Y/N]\n");
        lastCommands = new ArrayList<>();
        previousCommandIndex = 0;
        currentCategory = "consumable";
        draggedTrickWeaponName = null;
        draggedFirearmName = null;
        draggedRuneName = null;

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

        consumablesTab.setOnMouseReleased(event -> updateInventory("consumable"));
        materialsTab.setOnMouseReleased(event -> updateInventory("material"));
        keysTab.setOnMouseReleased(event -> updateInventory("key"));
        trickWeaponsTab.setOnMouseReleased(event -> updateInventory("trickWeapon"));
        firearmsTab.setOnMouseReleased(event -> updateInventory("firearm"));
        attiresTab.setOnMouseReleased(event -> updateInventory("attire"));
        gemsTab.setOnMouseReleased(event -> updateInventory("gem"));
        runesTab.setOnMouseReleased(event -> updateInventory("rune"));

        vialsImage.setOnMouseClicked(event -> {
            detailImage.setImage(new Image(String.valueOf(getClass().getResource("images/items/vial.png"))));
            detailText.setText("Special blood used in ministration. Restores HP.\n\nOnce a patient has had their blood ministered, a unique but common treatment in Yharnam, successive infusions recall the first, and are all the more invigorating for it.\n\nNo surprise that most Yharnamites are heavy users of blood.");
        });
        bulletsImage.setOnMouseClicked(event -> {
            detailImage.setImage(new Image(String.valueOf(getClass().getResource("images/items/bullet.png"))));
            detailText.setText("Special bullets used with hunter firearms.\n\nOrdinary bullets have no effect on beasts, and so Quicksilver Bullets, fused with the wielder's own blood, must be employed.\n\nThe strength of Quicksilver Bullets depends greatly upon the wielder's bloodtinge.");
        });

        trickWeaponImage.setOnMouseClicked(event -> {
            if (hunter.getTrickWeapon() == null) {
                detailImage.setImage(new Image(String.valueOf(getClass().getResource("images/items/empty.png"))));
                detailText.clear();
            } else {
                detailImage.setImage(new Image(String.valueOf(getClass().getResource(hunter.getTrickWeaponIcon()))));
                detailText.setText(hunter.getTrickWeapon().getDESCRIPTION());
            }
        });
        trickWeaponImage.setOnDragOver(event -> {
            if (event.getGestureSource() != trickWeaponImage && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        trickWeaponImage.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasImage()) {
                if (draggedTrickWeaponName != null) {
                    trickWeaponImage.setImage(db.getImage());
                    game.equipFunction(draggedTrickWeaponName);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        firearmImage.setOnMouseClicked(event -> {
            if (hunter.getFirearm() == null) {
                detailImage.setImage(new Image(String.valueOf(getClass().getResource("images/items/empty.png"))));
                detailText.clear();
            } else {
                detailImage.setImage(new Image(String.valueOf(getClass().getResource(hunter.getFirearmIcon()))));
                detailText.setText(hunter.getFirearm().getDESCRIPTION());
            }
        });
        firearmImage.setOnDragOver(event -> {
            if (event.getGestureSource() != firearmImage && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        firearmImage.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasImage()) {
                if (draggedFirearmName != null) {
                    firearmImage.setImage(db.getImage());
                    game.equipFunction(draggedFirearmName);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        List<ImageView> runeImages = new ArrayList<>();
        runeImages.add(rune1);
        runeImages.add(rune2);
        runeImages.add(rune3);
        runeImages.add(runeOath);
        int runeIndex = 0;
        for (ImageView image : runeImages) {
            if (runeIndex != 3) {
                int finalRuneIndex = runeIndex;
                image.setOnMouseClicked(event -> {
                    if (hunter.getRUNE_LIST().size() <= finalRuneIndex) {
                        detailImage.setImage(new Image(String.valueOf(getClass().getResource("images/items/empty.png"))));
                        detailText.clear();
                    } else {
                        detailImage.setImage(new Image(String.valueOf(getClass().getResource(hunter.getRUNE_LIST().get(finalRuneIndex).getImage()))));
                        detailText.setText(hunter.getRUNE_LIST().get(finalRuneIndex).getDESCRIPTION());
                    }
                });
            } else {
                runeOath.setOnMouseClicked(event -> {
                    if (hunter.getOathRune() == null) {
                        detailImage.setImage(new Image(String.valueOf(getClass().getResource("images/items/empty.png"))));
                        detailText.clear();
                    } else {
                        detailImage.setImage(new Image(String.valueOf(getClass().getResource(hunter.getOathRune().getImage()))));
                        detailText.setText(hunter.getOathRune().getDESCRIPTION());
                    }
                });
            }
            runeIndex++;
        }
    }

    public void updateDirectionalArrows(Place place) {
        northArrow.setVisible(place.getEXITS().containsKey("north"));
        eastArrow.setVisible(place.getEXITS().containsKey("east"));
        westArrow.setVisible(place.getEXITS().containsKey("west"));
        southArrow.setVisible(place.getEXITS().containsKey("south"));
    }

    public void updateHUD() {
        currentHP.setText(hunter.getCurrentHealthPoints() + "/" + hunter.getMaxHP() + " HP");
        if (hunter.getCurrentHealthPoints() == 0) {
            currentHP.setStyle("-fx-text-fill: red;");
        }
        echoesText.setText(Integer.toString(hunter.getBloodEchoes()));
        insightText.setText(Integer.toString(hunter.getInsight()));
        vialAmountText.setText(Integer.toString(hunter.getVialsNumber()));
        bulletAmountText.setText(Integer.toString(hunter.getBulletsNumber()));
        dmgBoostText.setText("" + hunter.getDamageBoost());
        boostLeftText.setText("" + hunter.getBoostLeft());
        dodgeRateText.setText("" + hunter.getDodgeRate());
        hitRateText.setText("" + hunter.getHitRate());
        visceralRateText.setText("" + hunter.getVisceralRate());
        bulletConsumptionText.setText("" + hunter.getBulletConsumption());
    }

    public void updateWeapons() {
        trickWeaponImage.setImage(new Image(String.valueOf(getClass().getResource(hunter.getTrickWeaponIcon()))));
        firearmImage.setImage(new Image(String.valueOf(getClass().getResource(hunter.getFirearmIcon()))));
        if (hunter.getTrickWeapon() == null) {
            trickWeaponNameText.setText("No weapon equipped");
            trickWeaponDmgText.setText("3 damage");
        } else {
            trickWeaponNameText.setText(hunter.getTrickWeapon().getNAME());
            trickWeaponDmgText.setText(hunter.getTrickWeapon().getCurrentDamage() + " damage");
        }
        if (hunter.getFirearm() == null) {
            firearmNameText.setText("No firearm equipped");
            firearmDmgText.setText("0 damage");
        } else {
            firearmNameText.setText(hunter.getFirearm().getNAME());
            firearmDmgText.setText(hunter.getFirearm().getCurrentDamage() + " damage");
        }
        updateHUD();
    }

    public void updateRunes() {
        List<Rune> runes = hunter.getRUNE_LIST();
        Rune oathRune = hunter.getOathRune();
        if (runes.size() >= 1) {
            rune1.setImage(new Image(String.valueOf(getClass().getResource(runes.get(0).getImage()))));
            if (runes.size() >= 2) {
                rune2.setImage(new Image(String.valueOf(getClass().getResource(runes.get(1).getImage()))));
                if (runes.size() >= 3) {
                    rune3.setImage(new Image(String.valueOf(getClass().getResource(runes.get(2).getImage()))));
                } else {
                    rune3.setImage(new Image(String.valueOf(getClass().getResource("images/items/runes/empty-rune.png"))));
                }
            } else {
                rune2.setImage(new Image(String.valueOf(getClass().getResource("images/items/runes/empty-rune.png"))));
            }
        } else {
            rune1.setImage(new Image(String.valueOf(getClass().getResource("images/items/runes/empty-rune.png"))));
        }
        if (oathRune == null) {
            runeOath.setImage(new Image(String.valueOf(getClass().getResource("images/items/runes/empty-oath.png"))));
        } else {
            runeOath.setImage(new Image(String.valueOf(getClass().getResource(oathRune.getImage()))));
        }
    }

    public void updateInventory(String category) {
        if (category.equals("default")) {
            category = currentCategory;
        } else {
            currentCategory = category;
        }
        inventoryItems.getChildren().clear();
        if (hunter.getINVENTORY().getNumberOfItems() == 0) {
            ImageView emptyImage = new ImageView();
            emptyImage.setFitHeight(75);
            emptyImage.setFitWidth(75);
            emptyImage.setPreserveRatio(true);
            emptyImage.setImage(new Image(String.valueOf(getClass().getResource("images/items/empty.png"))));

            Label emptyText = new Label("Empty inventory");
            emptyText.setPrefWidth(230);
            emptyText.setWrapText(true);
            emptyText.setFocusTraversable(false);

            inventoryItems.add(emptyImage, 0, 0);
            inventoryItems.add(emptyText, 1, 0);
        } else {
            int numberOfItemInCategory = 0;
            for (Item i : hunter.getINVENTORY().getItems().keySet()) {
                if (i.getCategory().equals(category)) {
                    ImageView itemImage = new ImageView();
                    itemImage.setFitHeight(75);
                    itemImage.setFitWidth(75);
                    itemImage.setPreserveRatio(true);
                    itemImage.setImage(new Image(String.valueOf(getClass().getResource(i.getImage()))));
                    itemImage.setOnMouseClicked(event -> {
                        detailImage.setImage(new Image(String.valueOf(getClass().getResource(i.getImage()))));
                        detailText.setText(i.getDESCRIPTION());
                    });
                    itemImage.setOnMouseEntered(event -> itemImage.getScene().setCursor(Cursor.HAND));
                    itemImage.setOnMouseExited(event -> itemImage.getScene().setCursor(Cursor.DEFAULT));

                    if (category.equals("trickWeapon") || category.equals("firearm")) {
                        String finalCategory = category; //To use in following lambdas

                        itemImage.setOnDragDetected((MouseEvent event) -> {
                            if (finalCategory.equals("trickWeapon")) {
                                draggedTrickWeaponName = i.getNAME();
                            } else {
                                draggedFirearmName = i.getNAME();
                            }
                            Dragboard db = itemImage.startDragAndDrop(TransferMode.ANY);
                            ClipboardContent content = new ClipboardContent();
                            content.putImage(itemImage.getImage());
                            db.setContent(content);
                            event.consume();
                        });
                        itemImage.setOnDragDone(event -> {
                            draggedTrickWeaponName = null;
                            draggedFirearmName = null;
                            if (event.getTransferMode() == TransferMode.MOVE) {
                                updateInventory(finalCategory);
                            }
                            event.consume();
                        });
                    }

                    int itemAmount = hunter.getINVENTORY().getItems().get(i);
                    Label itemText = new Label(i.getNAME() + (itemAmount == 1 ? "" : " x" + itemAmount));
                    itemText.setPrefWidth(230);
                    itemText.setWrapText(true);
                    itemText.setFocusTraversable(false);

                    inventoryItems.add(itemImage, 0, numberOfItemInCategory);
                    inventoryItems.add(itemText, 1, numberOfItemInCategory);
                    numberOfItemInCategory++;
                }
            }
            if (numberOfItemInCategory == 0) {
                ImageView emptyImage = new ImageView();
                emptyImage.setFitHeight(75);
                emptyImage.setFitWidth(75);
                emptyImage.setPreserveRatio(true);
                emptyImage.setImage(new Image(String.valueOf(getClass().getResource("images/items/empty.png"))));

                Label emptyText = new Label("Empty category");
                emptyText.setPrefWidth(230);
                emptyText.setWrapText(true);
                emptyText.setFocusTraversable(false);

                inventoryItems.add(emptyImage, 0, 0);
                inventoryItems.add(emptyText, 1, 0);
            }
        }
    }

    public void onKeyPressedWriteLine(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            console.appendText(">> " + writeLine.getText() + "\n");
            game.analyseText(writeLine.getText());
            lastCommands.add(writeLine.getText());
            previousCommandIndex = lastCommands.size() - 1;
            writeLine.clear();
        } else if (keyEvent.getCode().equals(KeyCode.UP)) {
            if (previousCommandIndex >= 0) {
                writeLine.setText(lastCommands.get(previousCommandIndex));
                writeLine.positionCaret(writeLine.getText().length());
                previousCommandIndex--;
            }
        } else if (keyEvent.getCode().equals(KeyCode.DOWN)) {
            if (lastCommands.size() != 0 && previousCommandIndex != lastCommands.size() - 1) {
                writeLine.setText(lastCommands.get(previousCommandIndex + 1));
                writeLine.positionCaret(writeLine.getText().length());
                previousCommandIndex++;
            }
        }
    }

    public void writeInstantly(String txt) {
        console.appendText(txt + "\n");
    }

    public void writeLetterByLetter(String txt) {
        writeInstantly(txt);
        /*writeLine.setDisable(true);
        Thread myThread = new Thread(() -> {
            LOCK.lock();
            char[] characters = txt.toCharArray();
            for( char c : characters){
                try {
                    sleep(15);
                    displayScreen.appendText(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            displayScreen.appendText("\n");
            LOCK.unlock();
            writeLine.setDisable(false);
        });
        myThread.start();*/
    }

    public void changeImage(String url) {
        String path = "images/" + url;
        Image myImage = new Image(String.valueOf(getClass().getResource(path)));
        placeImage.setImage(myImage);
    }

    public void transitionImage(String imageURL) {
        final Rectangle rect1 = new Rectangle(0, 0, imageAnchor.getWidth(), imageAnchor.getHeight());
        final Rectangle rect2 = new Rectangle(0, 0, imageAnchor.getWidth(), imageAnchor.getHeight());
        rect1.setFill(Color.BLACK);
        rect2.setFill(Color.BLACK);
        imageAnchor.getChildren().add(rect1);
        FadeTransition ft = new FadeTransition(Duration.millis(2000), rect1);
        ft.setFromValue(0.1);
        ft.setToValue(1.0);
        ft.setCycleCount(1);

        FadeTransition ft2 = new FadeTransition(Duration.millis(2000), rect2);
        ft2.setFromValue(1.0);
        ft2.setToValue(0.1);
        ft2.setCycleCount(1);
        ft2.setAutoReverse(true);
        ft2.setOnFinished(actionEvent -> imageAnchor.getChildren().remove(rect2));
        ft.setOnFinished(e -> {
            changeImage(imageURL);
            imageAnchor.getChildren().add(rect2);
            ft2.play();
            imageAnchor.getChildren().remove(rect1);
        });
        ft.play();
    }

    public void deathTransition() {
        Rectangle rect1 = new Rectangle(0, 0, imageAnchor.getWidth(), imageAnchor.getHeight());
        rect1.setFill(Color.BLACK);
        Rectangle rect2 = new Rectangle(0, 0, imageAnchor.getWidth(), imageAnchor.getHeight());
        rect2.setFill(Color.BLACK);
        Rectangle rect3 = new Rectangle(0, 0, imageAnchor.getWidth(), imageAnchor.getHeight());
        rect3.setFill(Color.BLACK);
        Rectangle rect4 = new Rectangle(0, 0, imageAnchor.getWidth(), imageAnchor.getHeight());
        rect4.setFill(Color.BLACK);
        imageAnchor.getChildren().add(rect1);

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

        ft4.setOnFinished(e -> imageAnchor.getChildren().remove(rect4));
        ft3.setOnFinished(e -> {
            changeImage("zones/hunter's-dream/hunter's-dream.jpg");
            imageAnchor.getChildren().remove(rect3);
            imageAnchor.getChildren().add(rect4);
            ft4.play();
        });
        ft2.setOnFinished(e -> {
            imageAnchor.getChildren().remove(rect2);
            imageAnchor.getChildren().add(rect3);
            ft3.play();
        });
        ft.setOnFinished(e -> {
            changeImage("you-died.jpg");
            imageAnchor.getChildren().remove(rect1);
            imageAnchor.getChildren().add(rect2);
            ft2.play();
        });
        ft.play();
    }
}
