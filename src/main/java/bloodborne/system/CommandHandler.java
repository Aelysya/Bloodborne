package bloodborne.system;

import bloodborne.exceptions.TooFewArgumentsException;

import static java.lang.Thread.sleep;

public class CommandHandler {

    private final Game GAME;

    public CommandHandler(Game game) {
        GAME = game;
    }

    public void startTextAnalyzer(String textLine) {
        String[] args = textLine.split("\\s+", 2);

        if (args.length == 0) {
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
            default -> GAME.writeInstantly(".............\nWake up ? [Y/N]\n");
        }
    }

    public void explorationTextAnalyzer(String textLine) throws TooFewArgumentsException {
        String[] args = textLine.split("\\s+", 2);
        if (args.length == 0) {
            return;
        }
        String command = args[0];
        String target = "";
        if (args.length == 2) {
            target = args[1];
        }
        String EXPLORATION_HELP_TEXT = """
                Currently available commands (use characters in brackets for shortcuts) :
                - (L) Look [target] : Get a description of the target, or of surroundings if none
                - (U) Use [object] : Use an object in your inventory
                - (SW) Switch : Switch the state of your trick weapon
                - (A) Activate [object] : Activate an object in your current location
                - (T) Take [object] : Pick up an object in your current location
                - (E) Equip [object] : Equip a weapon or a rune from your inventory
                - (F) Fight [target] : Initiate a fight with the target
                - (TK) Talk [NPC] : Engage conversation with target NPC
                - mute/unmute : Mutes or unmutes the music
                - (Q) Quit : Quit the game, all progress will be lost""";
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
            case "hl", "heal" -> GAME.healFunction();
            case "sw", "switch" -> GAME.switchFunction();
            case "mute" -> GAME.muteGame();
            case "unmute" -> GAME.unMuteGame();
            case "q", "quit" -> GAME.quitFunction();
            case "/kill" -> GAME.killFunction();
            default -> GAME.writeInstantly("Unknown command !");
        }
    }

    public void runeTextAnalyzer(String textLine) {
        String[] args = textLine.split("\\s+", 2);
        if (args.length == 0) {
            return;
        }
        String position = args[0];
        switch (position) {
            case "1", "2", "3" -> GAME.runeDecisionFunction(Integer.parseInt(position) - 1);
            case "c", "cancel" -> Game.setAnalyzer(TextAnalyzer.EXPLORATION);
            default -> GAME.writeInstantly("You can't memorize a rune at this position.");
        }
    }

    public void fightTextAnalyzer(String textLine) throws TooFewArgumentsException {
        String[] args = textLine.split("\\s+", 2);
        if (args.length == 0) {
            return;
        }
        String command = args[0];
        String target = "";
        if (args.length == 2) {
            target = args[1];
        }
        String FIGHT_HELP_TEXT = """
                Currently available commands (use characters in brackets for shortcuts) :
                - (CH) Combat-help : Show the exact stat changes for each combat actions
                - (A) Attack : Attack the enemy with your Trick weapon
                - (HA) Heavy-attack : Attacks the enemy with a heavy attack
                - (CHA) Charged-heavy-attack : Attacks the enemy with a charged heavy attack
                - (S) Shoot : Attack the enemy with your firearm
                - (SW) Switch : Switch the state of your trick weapon
                - (U) Use [object] : Use an object in your inventory
                - (F) Flee : Try to flee the fight
                - (HL) HeaL : Use a blood vial to heal you
                - mute/unmute : Mutes or unmutes the music
                """;
        String FIGHT_DETAILS_HELP_TEXT = """
                You are in a turn-based fight, here are the stat modifications for each action:
                - (A) Basic attack : Damage x 1, Dodge rate x 1
                - (HA) Heavy attack : Damage x 1.5, Dodge rate x 0.75
                - (CHA) Charged heavy attack : Damage x 1.9, Dodge rate x 0.6
                - (S) firearm shot : Damage x 1, + 0.2 to Dodge rate
                - (HL/U) Healing or using an item : Pass the turn, + 0.2 to Dodge rate
                - (F) Flee : Flee the fight but take some damage. You can't flee from a boss fight
                                
                Switching your trick weapon state or using help commands does not pass your turn
                """;
        switch (command) {
            case "u", "use" -> {
                if (target.equals("")) {
                    throw new TooFewArgumentsException();
                } else {
                    GAME.useFunction(target);
                }
            }
            case "h", "help" -> GAME.writeInstantly(FIGHT_HELP_TEXT);
            case "ch", "combat-help" -> GAME.writeInstantly(FIGHT_DETAILS_HELP_TEXT);
            case "a", "attack" -> GAME.resolveFightTurn("melee");
            case "ha", "heavy-attack" -> GAME.resolveFightTurn("heavy-melee");
            case "cha", "charged-heavy-attack" -> GAME.resolveFightTurn("charged-melee");
            case "s", "shoot" -> GAME.resolveFightTurn("range");
            case "sw", "switch" -> GAME.switchFunction();
            case "f", "flee" -> GAME.fleeFunction();
            case "hl", "heal" -> GAME.resolveFightTurn("heal");
            case "mute" -> GAME.muteGame();
            case "unmute" -> GAME.unMuteGame();
            default -> GAME.writeInstantly("Unknown command !");
        }
    }

    public void deathTextAnalyzer(String textLine) {
        String[] args = textLine.split("\\s+", 2);
        if (args.length == 0) {
            return;
        }
        String command = args[0];
        switch (command) {
            case "y", "yes" -> GAME.warpBackToLastLantern();
            case "n", "no" -> {
                GAME.writeInstantly("Do you want to quit the game ? [Y/N]");
                Game.setAnalyzer(TextAnalyzer.QUIT_FROM_DEATH);
            }
            default -> GAME.writeInstantly("Unknown command !\n\nYou died, do you want to reappear at the last lantern ? [Y/N]");
        }
    }

    public void dreamBackTextAnalyzer(String textLine) {
        String[] args = textLine.split("\\s+", 2);

        if (args.length == 0) {
            return;
        }

        String command = args[0];
        //String target = args[1];

        switch (command) {
            case "y", "yes" -> GAME.goFunction("hunter's-dream");
            case "n", "no" -> Game.setAnalyzer(TextAnalyzer.EXPLORATION);
            case "mute" -> GAME.muteGame();
            case "unmute" -> GAME.unMuteGame();
            default -> GAME.writeInstantly("Unknown command !");
        }
    }

    public void yharnamHeadstoneTextAnalyzer(String textLine) {
        String[] args = textLine.split("\\s+", 2);
        if (args.length == 0) {
            return;
        }
        String command = args[0];
        switch (command) {
            case "1" -> GAME.checkIfWarpFromDreamPossible("clinic");
            case "2" -> GAME.checkIfWarpFromDreamPossible("small-square");
            case "3" -> GAME.checkIfWarpFromDreamPossible("bridge-end");
            case "4" -> GAME.checkIfWarpFromDreamPossible("tomb-of-oedon");
            case "c", "cancel" -> Game.setAnalyzer(TextAnalyzer.EXPLORATION);
            default -> GAME.writeHeadstoneText("yharnam");
        }
    }

    public void quitFromDeathTextAnalyzer(String textLine) {
        String[] args = textLine.split("\\s+", 2);

        if (args.length == 0) {
            return;
        }

        String command = args[0];
        //String target = args[1];

        switch (command) {
            case "y", "yes" -> System.exit(0);
            case "n", "no" -> {
                GAME.writeInstantly("You died, do you want to reappear at the last lantern ? [Y/N]");
                Game.setAnalyzer(TextAnalyzer.DEATH);
            }
            default -> GAME.writeInstantly("Unknown command !\n\nDo you want to quit the game ? [Y/N]");
        }
    }

    public void quitTextAnalyzer(String textLine) {
        String[] args = textLine.split("\\s+", 2);

        if (args.length == 0) {
            return;
        }

        String command = args[0];
        //String target = args[1];

        switch (command) {
            case "y", "yes" -> System.exit(0);
            case "n", "no" -> Game.setAnalyzer(TextAnalyzer.EXPLORATION);
            default -> GAME.writeInstantly("Unknown command !");
        }
    }
}
