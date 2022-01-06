module bloodborne {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;

    opens bloodborne to javafx.fxml;
    exports bloodborne;
    exports bloodborne.conditions;
    exports bloodborne.entities;
    exports bloodborne.environment;
    exports bloodborne.exceptions;
    exports bloodborne.items;
    exports bloodborne.json;
    exports bloodborne.sounds;
    exports bloodborne.system;
    exports bloodborne.world;
}