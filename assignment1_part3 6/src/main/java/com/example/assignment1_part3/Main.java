package com.example.assignment1_part3;


import com.example.assignment1_part3.control.StudentRecordController;
import com.example.assignment1_part3.view.View;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;


public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override

    public void start(Stage primaryStage) throws IOException, SQLException {

        View view = new View();
        StudentRecordController controller = new StudentRecordController(view);


        controller.fillComboBoxes();

        primaryStage.setScene(view.getScene());
        primaryStage.setTitle("MTU Student Record System");

        primaryStage.show();
        primaryStage.setOnCloseRequest(controller::closeEvent);
    }

}
