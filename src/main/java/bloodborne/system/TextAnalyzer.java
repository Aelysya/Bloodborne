package bloodborne.system;

public enum TextAnalyzer {
    EXPLORATION("EXPLORATION >>"),
    FIGHT("FIGHT >>"),
    RUNE("EQUIP RUNE >>"),
    DEATH("DEATH >>"),
    QUIT("QUIT >>"),
    QUIT_FROM_DEATH("QUIT >>"),
    START("START >>"),
    DREAM_BACK("BACK TO DREAM >>"),
    LEVEL_UP("LEVEL UP >>"),
    MERCHANT("SHOP >>"),
    UPGRADE("UPGRADE >>"),
    YHARNAM_HEADSTONE("WARP >>"),
    FRONTIER_HEADSTONE("WARP >>"),
    UNSEEN_HEADSTONE("WARP >>");

    private final String TAG;

    TextAnalyzer(String tag) {
        TAG = tag;
    }

    String getTAG() {
        return TAG;
    }
}
