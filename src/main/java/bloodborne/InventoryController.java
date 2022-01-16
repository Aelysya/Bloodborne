package bloodborne;

import bloodborne.entities.Hunter;
import bloodborne.items.Item;
import bloodborne.system.Game;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class InventoryController {

    @FXML
    ImageView firstImage;

    @FXML
    TextField firstText;

    @FXML
    GridPane inventoryItems;



    private Game game;
    private Hunter hunter;

    public void init(Game game, Hunter hunter){
        this.game = game;
        this.hunter = hunter;
    }

    public void updateInventory(){
        inventoryItems.getChildren().clear();
        if (hunter.getINVENTORY().getNumberOfItems() == 0){
            ImageView emptyImage = new ImageView();
            emptyImage.setFitHeight(75);
            emptyImage.setFitWidth(75);
            emptyImage.setPreserveRatio(true);
            emptyImage.setImage(new Image(String.valueOf(getClass().getResource("images/empty.png"))));

            TextField emptyText = new TextField("Empty inventory");
            emptyText.setEditable(false);
            emptyText.setFocusTraversable(false);

            inventoryItems.add(emptyImage, 0, 0);
            inventoryItems.add(emptyText, 1, 0);
        } else {
            int cpt = 0;
            for (Item i : hunter.getINVENTORY().getItems().values()){
                ImageView itemImage = new ImageView();
                itemImage.setFitHeight(75);
                itemImage.setFitWidth(75);
                itemImage.setPreserveRatio(true);
                itemImage.setImage(new Image(String.valueOf(getClass().getResource("images/items/" + i.getImage()))));

                TextField itemText = new TextField(i.getNAME());
                itemText.setEditable(false);
                itemText.setFocusTraversable(false);

                inventoryItems.add(itemImage, 0, cpt);
                inventoryItems.add(itemText, 1, cpt);
                cpt++;
            }
        }
    }
}
