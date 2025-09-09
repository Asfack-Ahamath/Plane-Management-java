module org.example.w2052789_PlaneManagement {
    requires javafx.controls;
    requires javafx.fxml;

    // Export packages for external access
    exports org.example.PlaneManagement;
    exports org.example.PlaneManagement.gui;
    exports org.example.PlaneManagement.common;
    
    // Open packages for JavaFX reflection access
    opens org.example.PlaneManagement.gui to javafx.fxml;
    opens org.example.PlaneManagement.common to javafx.fxml;
}