module org.example.sn4ke {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.sn4ke to javafx.fxml;
    exports org.example.sn4ke;
}