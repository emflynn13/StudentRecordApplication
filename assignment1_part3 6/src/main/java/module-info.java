module com.example.assignment1_part3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.assignment1_part3 to javafx.fxml;
    exports com.example.assignment1_part3;
    exports com.example.assignment1_part3.control;
    opens com.example.assignment1_part3.control to javafx.fxml;
    opens com.example.assignment1_part3.model to javafx.fxml;
    exports com.example.assignment1_part3.model;
    exports com.example.assignment1_part3.view;
    opens com.example.assignment1_part3.view to javafx.fxml;
}