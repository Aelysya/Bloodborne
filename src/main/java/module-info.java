module bloodborne {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;

    opens bloodborne to javafx.fxml;
    exports bloodborne;
}