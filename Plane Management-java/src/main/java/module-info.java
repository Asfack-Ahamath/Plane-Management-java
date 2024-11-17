module org.example.w2052789_PlaneManagement {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.PlaneManagement to javafx.fxml;
    exports org.example.PlaneManagement;
}