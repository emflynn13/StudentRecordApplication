package com.example.assignment1_part3.view;

import com.example.assignment1_part3.control.StudentRecordController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.text.ParseException;


public class View {

    private final StudentRecordController control;
    public final TextArea studentDetails;
    public TextField nameText;
    public TextField idText;
    public TextField dobText;
    public TextField semesterText;
    public TextField gradeText;
    public TabPane tabPane;
    public BorderPane borderpane;
    public ComboBox<String> comboBox;
    public ComboBox<String> comboBox2;
    public ComboBox<String> comboBox3;
    public TextField selectedNameText;
    public TextField selectedIDText;
    public TextField selectedDOBText;
    public TextField selectedSemText;
    public TextArea selectedModulesText;

    public TextField moduleNameText;
    public TextField moduleCodeText;
    public TextField moduleSemText;

    private final Scene scene;

    public View(){
        control = new StudentRecordController(this);
        borderpane = new BorderPane();

        tabPane = new TabPane();
        Tab tab1 = new Tab("Edit Students");
        Tab tab2 = new Tab("Edit Modules");
        Tab tab3 = new Tab("Submit Student Grades");
        Tab tab4 = new Tab("View Student Details");
        tab1.setClosable(false);
        tab2.setClosable(false);
        tab3.setClosable(false);
        tab4.setClosable(false);

        // ------ Tab 1 --------//
        VBox root = new VBox(15);
        GridPane inputGrid = new GridPane();

        HBox centerButtonRow = new HBox(10);
        HBox btmButtonRow = new HBox(10);

        scene = new Scene(borderpane);

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            String name = nameText.getText();
        String id = idText.getText();
        String dob = dobText.getText();
        String semester = semesterText.getText();
            try {
                control.addStudent(name, id, dob, semester);
            } catch (ParseException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button removeBtn = new Button("Remove");
        removeBtn.setOnAction(e -> {
        String ID = idText.getText();
            try {
                control.removeStudent(ID);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        Button listBtn = new Button("List");
        listBtn.setOnAction(e -> control.listStudents());

        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(control::exit);

        Button memLeakBtn = new Button("Memory Leak");
        memLeakBtn.setOnAction(e -> control.startMemLeak());



        Label name1 = new Label("Enter Name ");
        Label id1 = new Label("Enter Student ID ");
        Label dob1 = new Label("Enter Date of Birth ");
        Label semester1 = new Label("Enter current semester");

        nameText = new TextField();
        idText = new TextField();
        dobText = new TextField();
        semesterText = new TextField();

        // create hints for user input
        Tooltip tool = new Tooltip();
        tool.setText("In the form RXXXXXXXX");
        idText.setTooltip(tool);
        dobText.setPromptText("In the form DD/MM/YYYY");

        studentDetails = new TextArea();
        studentDetails.setEditable(false);
        studentDetails.setPrefSize(100, 200);
        VBox.setMargin(studentDetails, new Insets(0, 10, 0, 10));

        // add these rows to a grid to align text fields
        inputGrid.addRow(0, name1, nameText);
        inputGrid.addRow(1, id1, idText);
        inputGrid.addRow(2, dob1, dobText);
        inputGrid.addRow(3, semester1, semesterText);
        inputGrid.setVgap(20);
        inputGrid.setHgap(20);

        root.getChildren().addAll(inputGrid, centerButtonRow, studentDetails, btmButtonRow);
        centerButtonRow.getChildren().addAll(addBtn, removeBtn, listBtn);
        btmButtonRow.getChildren().addAll(exitBtn, memLeakBtn);
        btmButtonRow.setAlignment(Pos.CENTER_RIGHT );
        root.setPadding(new Insets(15, 15, 15, 15));
        root.setAlignment(Pos.TOP_CENTER);
        tab1.setContent(root);


         // ------ Tab 2 --------//

        Label moduleNameLabel = new Label("Module Name");
        Label moduleCodeLabel = new Label("Module Code");
        Label moduleSemLabel = new Label("Semester");

        moduleNameText = new TextField();
        moduleCodeText = new TextField();
        moduleSemText = new TextField();

        GridPane tab2Grid = new GridPane();
        tab2Grid.setHgap(10);
        tab2Grid.setVgap(30);
        tab2Grid.setPadding(new Insets(20, 10, 0, 10));
        tab2Grid.setAlignment(Pos.TOP_CENTER);


        Button addModuleBtn = new Button("Add");
        addModuleBtn.setOnAction(e ->{
            String mName = moduleNameText.getText();
            String code = moduleCodeText.getText();
            String mSem = moduleSemText.getText();
            try {
                control.addModule(mName, code, mSem);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
                }

        });


        Button removeModuleBtn = new Button("Remove");
        removeModuleBtn.setOnAction(e -> {
            String code = moduleCodeText.getText();
            try {
                control.removeModule(code);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });


        tab2Grid.addRow(1, moduleNameLabel, moduleNameText);
        tab2Grid.addRow(2, moduleCodeLabel, moduleCodeText);
        tab2Grid.addRow(3, moduleSemLabel, moduleSemText);
        tab2Grid.addRow(4, addModuleBtn, removeModuleBtn);

        tab2.setContent(tab2Grid);


        // ------ Tab 3 --------//

        GridPane tab3Grid = new GridPane();
        tab3Grid.setHgap(20);
        tab3Grid.setVgap(10);

        Button submitModuleButton = new Button("Submit");
        submitModuleButton.setOnAction(e -> {
            try {
                String selectedStudentID = comboBox.getSelectionModel().getSelectedItem();
                String selectedModuleCode = comboBox3.getSelectionModel().getSelectedItem();
                String grade = gradeText.getText();
                control.submitModuleGrade(selectedStudentID, selectedModuleCode, grade);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button updateGradeBtn = new Button("Update");
        updateGradeBtn.setOnAction(e -> {
            String selectedStudentID = comboBox.getSelectionModel().getSelectedItem();
            String selectedModuleCode = comboBox3.getSelectionModel().getSelectedItem();
            String grade = gradeText.getText();
            try {
                control.updateGrade(selectedStudentID, selectedModuleCode, grade);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        comboBox = new ComboBox<>();
        comboBox2 = new ComboBox<>();
        comboBox3 = new ComboBox<>();
        Label selectStudentLabelTab3 = new Label("Select Student");
        Label selectModuleLabelTab3 = new Label("Select Module");

        gradeText = new TextField();
        Label gradeLabel = new Label("Enter grade received");
        gradeLabel.setPadding(new Insets(0, 0, 0, 20));
        Tooltip gradeTool = new Tooltip();
        gradeTool.setText("NP if not completed");
        gradeText.setPromptText("NP if not completed");
        gradeText.setTooltip(gradeTool);

        tab3Grid.add(selectStudentLabelTab3, 4, 3);
        tab3Grid.add(comboBox, 4,4);
        tab3Grid.add(selectModuleLabelTab3, 4, 6);
        tab3Grid.add(comboBox3,4,7);
        tab3Grid.add(gradeLabel, 3,12);
        tab3Grid.add(gradeText, 4,12);
        tab3Grid.add(submitModuleButton, 4, 14);
        tab3Grid.add(updateGradeBtn, 5, 14);
        //tab2Grid.setGridLinesVisible(true);

        BorderPane border2 = new BorderPane();
        border2.setCenter(tab3Grid);
        border2.setPadding(new Insets(20, 20, 0, 10));

        tab3.setContent(border2);


        // ------ Tab 4 --------//
        Button gradeBtn = new Button("Grade");
        gradeBtn.setMinWidth(100);
        gradeBtn.setOnAction(e -> {String studentNum = comboBox2.getSelectionModel().getSelectedItem();
            boolean byGrade = true;
            try {
                control.getStudentModules(studentNum, byGrade);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button moduleNameBtn = new Button("Alphabetical");
        moduleNameBtn.setMinWidth(100);
        moduleNameBtn.setOnAction(e -> {String studentNum = comboBox2.getSelectionModel().getSelectedItem();
            boolean byGrade = false;
            try {
                control.getStudentModules(studentNum, byGrade);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        comboBox2.setValue("Select Student");
        Label selectedNameLabel = new Label("Name");
        Label selectedIdLabel = new Label("ID");
        Label selectedDOBLabel = new Label("DOB");
        Label selectedSemLabel = new Label("Semester");
        Label selectedModules = new Label("Modules\nPassed");
        Label moduleOrderLabel = new Label("Select Module Order");

        selectedNameText = new TextField();
        selectedIDText = new TextField();
        selectedDOBText = new TextField();
        selectedSemText = new TextField();
        selectedModulesText = new TextArea();

        selectedNameText.setEditable(false);
        selectedIDText.setEditable(false);
        selectedDOBText.setEditable(false);
        selectedSemText.setEditable(false);
        selectedModulesText.setEditable(false);

        selectedModulesText.setPrefSize(100, 100);


        GridPane tab4Grid = new GridPane();
        tab4Grid.setHgap(10);
        tab4Grid.setVgap(30);
        tab4Grid.setPadding(new Insets(20, 10, 0, 10));
        tab4Grid.setAlignment(Pos.TOP_CENTER);

        tab4Grid.add(comboBox2, 1, 1);
        tab4Grid.add(selectedNameLabel, 5, 0);
        tab4Grid.add(selectedNameText, 6, 0);
        tab4Grid.add(selectedIdLabel, 5, 1);
        tab4Grid.add(selectedIDText, 6, 1);
        tab4Grid.add(selectedDOBLabel, 5, 2);
        tab4Grid.add(selectedDOBText, 6, 2);
        tab4Grid.add(selectedSemLabel, 5, 3);
        tab4Grid.add(selectedSemText, 6, 3);
        tab4Grid.add(selectedModules, 5, 4);
        tab4Grid.add(selectedModulesText, 6, 4, 8, 2);
        tab4Grid.add(moduleOrderLabel, 6, 6);
        tab4Grid.add(moduleNameBtn, 6, 7);
        tab4Grid.add(gradeBtn, 7, 7);
        //tab3Grid.setGridLinesVisible(true);

        tab4.setContent(tab4Grid);

        tabPane.getTabs().addAll(tab1,tab2, tab3, tab4);
        borderpane.setTop(tabPane);
        borderpane.setPrefSize(600, 700);

        comboBox2.setOnAction(e -> {String id = comboBox2.getSelectionModel().getSelectedItem();
            try {
                control.getChosenStudentInfo(id);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    public Scene getScene(){
        return scene;
    }

    public void getError(String errorMsg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(errorMsg);
        alert.showAndWait();
    }
}