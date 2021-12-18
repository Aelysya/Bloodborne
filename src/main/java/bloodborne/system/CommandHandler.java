package bloodborne.system;

import bloodborne.exceptions.TooFewArgumentsException;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class CommandHandler {

    private final Game GAME;

    public CommandHandler(Game game) {
        GAME = game;
    }

    public void startTextAnalyzer(String textLine) throws InterruptedException {
        String[] args = textLine.split("\\s+", 2);

        if(args.length == 0){
            return;
        }

        String command = args[0];
        //String target = args[1];

        switch (command) {
            case "y", "yes" -> GAME.startGame();
            case "n", "no" -> {
                Thread thread = new Thread(() -> {
                    GAME.writeInstantly(".............\nOk fine keep sleeping");
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                });
                thread.start();
            }
            default -> GAME.writeInstantly(".............\nWake up ? [yes/no]\n");
        } //TODO change to letter by letter printing
    }

    public void quitTextAnalyzer(String textLine) {
        String[] args = textLine.split("\\s+", 2);

        if(args.length == 0){
            return;
        }

        String command = args[0];
        //String target = args[1];

        switch (command) {
            case "y", "yes" -> System.exit(0);
            case "n", "no" -> GAME.setAnalyzer(TextAnalyzer.EXPLORATION);
            case "mute" -> GAME.muteGame();
            case "unmute" -> GAME.unMuteGame();
            default -> GAME.writeInstantly("Unknown command !");
        }
    }

    public void explorationTextAnalyzer(String textLine) throws TooFewArgumentsException{
        String[] args = textLine.split("\\s+", 2);

        if(args.length == 0){
            return;
        }

        String command = args[0];
        String target = "";

        if(args.length == 2){
            target = args[1];
        }

        String EXPLORATION_HELP_TEXT = """
                Currently available commands (use capitalized characters for shortcuts) :
                - Look [target] : Get a description of the target, or of surroundings if none
                - Go north|east|south|west : Move towards the direction given
                - Use [object] : Use an object in your inventory
                - HeaL : Use a blood vial to heal you
                - SWitch : Switch the state of your trick weapon
                - Activate [object] : Activate an object in your current location
                - Take [object] : Pick up an object in your current location
                - Equip [weapon] : Equip a weapon in your inventory
                - Fight [target] : Initiate a fight with the target
                - TalK [NPC] : Engage conversation with target NPC
                - Status : Display current status and inventory
                - mute/unmute : Mutes or unmutes the music
                - Quit : Quit the game, all progress will be lost""";

        switch (command) {
            case "g", "go" -> {
                if (target.equals("")) {
                    throw new TooFewArgumentsException();
                } else {
                    GAME.goFunction(target);
                }
            }
            case "tp", "teleport" -> {
                if (target.equals("")) {
                    throw new TooFewArgumentsException();
                } else {
                    GAME.teleportFunction(target);
                }
            }
            case "a", "activate" -> {
                if (target.equals("")) {
                    throw new TooFewArgumentsException();
                } else {
                    GAME.activateFunction(target);
                }
            }
            case "u", "use" -> {
                if (target.equals("")) {
                    throw new TooFewArgumentsException();
                } else {
                    GAME.useFunction(target);
                }
            }
            case "t", "take" -> {
                if (target.equals("")) {
                    throw new TooFewArgumentsException();
                } else {
                    GAME.takeFunction(target);
                }
            }
            case "e", "equip" -> {
                if (target.equals("")) {
                    throw new TooFewArgumentsException();
                } else {
                    GAME.equipFunction(target);
                }
            }
            case "f", "fight" -> {
                if (target.equals("")) {
                    throw new TooFewArgumentsException();
                } else {
                    GAME.initiateFightFunction(target);
                }
            }
            case "tk", "talk" -> {
                if (target.equals("")) {
                    throw new TooFewArgumentsException();
                } else {
                    GAME.talkFunction(target);
                }
            }
            case "h", "help" -> GAME.writeInstantly(EXPLORATION_HELP_TEXT);
            case "l", "look" -> GAME.lookFunction(target);
            case "s", "status" -> GAME.statusFunction();
            case "hl", "heal" -> GAME.healFunction();
            case "sw", "switch" -> GAME.switchFunction();
            case "mute" -> GAME.muteGame();
            case "unmute" -> GAME.unMuteGame();
            case "q", "quit" -> GAME.quitFunction();
            default -> GAME.writeInstantly("Unknown command !");
        }
    } //TODO change to letter by letter printing

    public void fightTextAnalyzer(String textLine) throws TooFewArgumentsException {
        String[] args = textLine.split("\\s+", 2);

        if(args.length == 0){
            return;
        }

        String command = args[0];
        //String target = args[1];

        String FIGHT_HELP_TEXT = """
                Currently available commands (capitalized characters for shortcut) :
                - Attack : Attack the enemy with your Trick weapon, you'll have a chance to dodge it's attack
                - SHoot : Attack the enemy with your gun, you'll have a chance to stagger it, canceling it's attack and performing a visceral attack
                - SWitch : Switch the state of your trick weapon
                - Flee : Try to flee the fight, you will take some damage if you do this
                - HeaL : Use a blood vial to heal you
                - Status : Display current status and inventory
                - mute/unmute : Mutes or unmutes the music""";

        switch (command) {
            case "h", "help" -> GAME.writeInstantly(FIGHT_HELP_TEXT);
            case "a", "attack" -> GAME.resolveFight("melee");
            case "sh", "shoot" -> GAME.resolveFight("range");
            case "sw", "switch" -> GAME.switchFunction();
            case "f", "flee" -> GAME.fleeFunction();
            case "hl", "heal" -> GAME.healFunction();
            case "s", "status" -> GAME.statusFunction();
            case "mute" -> GAME.muteGame();
            case "unmute" -> GAME.unMuteGame();
            default -> GAME.writeInstantly("Unknown command !"); //TODO Add a function to allow use of papers and only these items
        }
    }

    public void deathTextAnalyzer(String textLine) {
        String[] args = textLine.split("\\s+", 2);

        if(args.length == 0){
            return;
        }

        String command = args[0];

        switch (command) {
            case "y", "yes" -> System.exit(0);
            default -> GAME.writeInstantly("It may be hard to accept but... you're dead. You have to deal with it.\nEnter Y to quit");
        }//TODO change to letter by letter printing
    }

    public void winTextAnalyzer(String textLine) {
        String[] args = textLine.split("\\s+", 2);

        if(args.length == 0){
            return;
        }

        String command = args[0];

        switch (command) {
            case "y", "yes" -> System.exit(0);
            default -> GAME.writeInstantly("You don't need to stay here any longer.\nPress Y to quit");
        }
    }
}
