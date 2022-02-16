package bloodborne.items;

import bloodborne.entities.Entity;
import bloodborne.entities.Hunter;
import bloodborne.sounds.SoundManager;

import java.util.Map;

public class PungentBloodCocktail extends Item{

    public PungentBloodCocktail(String id, String description, Map<String, String> attributes) {
        super(id, description, attributes);
    }

    @Override
    public String use(Hunter hunter, SoundManager soundManager, Entity target) { //TODO Make the system to make the item work properly
        soundManager.playSoundEffect("itemsUse/pungent-blood-cocktail.wav");
        hunter.removeOneItemFromStack(this);
        return "You throw the cocktail bottle on the ground, a rancid smell emanates from the puddle of blood. Some kind of enemies may be attracted and prevented from attacking you.";
    }
}
