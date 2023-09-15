package com.example.assignment1_part3.control;

import com.example.assignment1_part3.model.Student;
import com.example.assignment1_part3.model.Module;
import com.example.assignment1_part3.model.StudentContainer;
import com.example.assignment1_part3.view.View;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


public class StudentRecordController {
    private final View view;
    private StudentContainer studentModel;
    private Alert alert;


    private final Validation validator;

    public StudentRecordController(View view){
        this.view = view;
        this.validator = new Validation();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/OOP_Assign2", "root", "Arsenal1306");
            this.studentModel = new StudentContainer(connection);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Constructor made for running tests
     */
    public StudentRecordController(){
        view = null;
        validator = null;
    }

    /**
     * This method is called when a user clicks on the addbtn in View. It creates a student
     * based on the information passed in and then adds this student to the list
     * of students in the model where it will be added to the database.
     * This method ensures that the textFields are all filled and checks for
     * duplicate entries in the list of students in the system.
     * @param name - the name of the student in the textField
     * @param id - the id of the student in the textField
     * @param dob - the date of birth of the student in the textField
     */
    public void addStudent(String name, String id, String dob, String semester) throws ParseException, SQLException {
        Student myStudent;
        boolean found = false;

        // if a section is not filled then produce an alert
        if (name.equals("") || id.equals("") || dob.equals("") || semester.equals("")){
            view.getError("Incomplete submission.");
        }
        else if (!validator.dateValidator(dob)){
            view.getError("Invalid date.");
        }
        else if (validator.validateStudentNumber(id) != 2){
            if (validator.validateStudentNumber(id) == 0)
                view.getError("Invalid student number length. 9 characters please.");
            else if (validator.validateStudentNumber(id) == 1)
                view.getError("Student number must starts with an \"R\" followed by 8 numbers.");
        }
        else {
            // check if a student is already in the system
            if (studentModel.studentInTable(id)){
                view.getError("Already in the system");
                    found = true;
            }

            // if not already in the system then add the student
            if (!found) {
                Date date = new SimpleDateFormat("dd/MM/yyy").parse(dob);
                int sem = Integer.parseInt(semester);
                myStudent = new Student(name, id, date, sem);
                System.out.println(studentModel.addStudent(myStudent));
                fillComboBoxes();
                clearText();
            }
        }
    }
    /**
     * This method gets the list of studentIDs from the model and then fills
     * the comboBoxes in the view with them.
     * It first calls the clearComboBox method to clear the comboBoxes to prevent entering a student more than once.
     */
    public void fillComboBoxes() {
        clearComboBoxes();

        ArrayList<String> idList = studentModel.getStudentIDs();
        for (String id : idList) {
            view.comboBox.getItems().add(id);
            view.comboBox2.getItems().add(id);
        }

        ArrayList<String> moduleCodeList = studentModel.getModuleCodes();
        for (String code: moduleCodeList){
            view.comboBox3.getItems().add(code);
        }
        view.comboBox2.setValue("Select Student");
    }

    /**
     * This method clears all comboBoxes in the view.
     */
    public void clearComboBoxes(){
        view.comboBox.getItems().clear();
        view.comboBox2.getItems().clear();
        view.comboBox3.getItems().clear();
    }

    /**
     * This method receives the id of a student. This id is then sent to the model to be compared against
     * database entries. If a match is found the student is removed. If not found an error message is created.
     * It then calls a method to clear the textFields for this entry.
     * @param id- the id of the student to be removed
     */
    public void removeStudent(String id) throws SQLException {
        boolean removed = studentModel.removeStudent(id);
        if (!removed)
            view.getError("No such student in system.");
        else {
            clearText();
            fillComboBoxes();
        }

    }

    /**
     * This method is used to clear all input in the textFields
     */
    public void clearText(){
        view.nameText.clear();
        view.dobText.clear();
        view.idText.clear();
        view.semesterText.clear();
        view.gradeText.clear();
        view.selectedNameText.clear();
        view.selectedModulesText.clear();
        view.selectedDOBText.clear();
        view.selectedIDText.clear();
        view.moduleNameText.clear();
        view.moduleCodeText.clear();
        view.moduleSemText.clear();
        view.selectedSemText.clear();
    }

    /**
     * This method iterates through the list of students that is returned from the model and
     * enters their information into the textArea in the view to be displayed.
     */
    public void listStudents(){
        try {
            String text = studentModel.getStudents();
            view.studentDetails.setText(text);
        }
        catch (Exception error){
            System.out.println("Error listing students");
        }
    }
    /**
     * This method is called when the user clicks on the "Exit" button.
     * It creates an alert which asks the user to confirm if they want to exit.
     */
    public void exit(ActionEvent e){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Program");
        alert.setTitle("Exit Program");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit the program?");

        Optional<ButtonType> result = alert.showAndWait();

        // if user clicks ok then exit
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            Platform.exit();
        } else
            e.consume(); // consume the event
    }
    /**
     * This method is called when the user clicks on the 'x' at the top of the program.
     * It creates an alert to ask the user if they are sure they'd like to exit.
     * If the user selects no then it consumes the event.
     */
    public void closeEvent(WindowEvent windowEvent){
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Program");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit the program?");

        Optional<ButtonType> result = alert.showAndWait();

        // if user clicks ok then exit
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            Platform.exit();
        } else
            windowEvent.consume(); // consume the event
    }
    /**
     * This method receives the studentNum selected in the ComboBox in the view and sends this number to the model
     * which then returns a list of this particular student's modules and grades which it then sends to the organizeStudentModules
     * method to sort it and then fills the text area in the view with the results.
     * It then puts this sorted list in text form to fill the textArea.
     * @param studentNum - the id of the selected student in the student list in the model
     * @param byGrade - is true if the user wants the modules to be ordered by grade,
     *                false if they want them ordered alphabetically
     */

    public void getStudentModules(String studentNum, boolean byGrade) throws SQLException {
        List<String> results = studentModel.getStudentModuleInfo(studentNum);
        String modules = organizeModules(byGrade, results);
        view.selectedModulesText.setText(modules);
    }

    /**
     * This method receives a boolean value and a list of results and based on the boolean variable it organizes
     * modules based on either grade or alphabetically. It firsts splits the list items and then checks for
     * NP or an integer value. If it's a NP the value is ignored but if it's an integer it can be sorted
     * by number.
     * @param byGrade - boolean value which decides whether to organize the modules by grase or alphabetically
     * @param results - the list of the modules and the grade received
     * @return - the sorted string
     */
    public String organizeModules(boolean byGrade, List<String> results) {
        StringBuilder sortedModules = new StringBuilder();
        if (byGrade) {
            results.sort((s1, s2) -> {
                String[] parts1 = s1.split(" : ");
                String[] parts2 = s2.split(" : ");

                if (parts1[1].equalsIgnoreCase("NP")) {
                    return 1;
                } else if (parts2[1].equalsIgnoreCase("NP")) {
                    return -1;
                } else {
                    int grade1 = Integer.parseInt(parts1[1]);
                    int grade2 = Integer.parseInt(parts2[1]);
                    return Integer.compare(grade1, grade2);
                }
            });
        } else {
            Collections.sort(results);
        }
        for (String result : results) {
            sortedModules.append(result).append("\n");
        }
        return sortedModules.toString();
    }

    /**
     * This method receives the id of a student as a parameter and then iterates through the
     * model's list of students to find the index of the matching student.
     * It then gets the information of this student and fills the textFields with it.
     * This method fills the module textArea with the default ordering of modules in the arraylist.
     * @param id - the id of the student chosen in the comboBox
     */
    // get student chosen in comboBox to display their info. Get chosen id as a parameter to match against arrayList
    public void getChosenStudentInfo(String id) throws SQLException {
        Student myStudent = studentModel.getStudent(id);
        if (myStudent != null) {
            view.selectedNameText.setText(myStudent.getName());
            view.selectedIDText.setText(myStudent.getId());
            view.selectedDOBText.setText(myStudent.getDob());
            view.selectedSemText.setText(String.valueOf(myStudent.getSemester()));

            String text = studentModel.getGradeInfo(id);
            view.selectedModulesText.setText(text);
        }
    }

    /**
     * This method receives the user input and validates it, if the information is valid it
     * then creates a new module object which it passes to the model to be entered into the database.
     * @param name - The name of the module
     * @param code - the module code
     * @param semester - the semester in which the module takes place
     */
    public void addModule(String name, String code, String semester) throws SQLException {
        boolean found = false;

        if (name.equals("") || code.equals("") || semester.equals("")) {
            view.getError("Incomplete submission.");
        }
        int sem = Integer.parseInt(semester);
        if (sem > 2 || sem < 1) {
            view.getError("Invalid semester number");
        } else {
            // check if a student is already in the system
            if (studentModel.moduleInTable(code)) {
                view.getError("Already in the system");
                found = true;
            }
            if (!found) {
                Module myModule = new Module(name, code, sem);
                System.out.println(studentModel.addModule(myModule));
                fillComboBoxes();
                clearText();
            }
        }
    }

    /**
     * This method get the code of the module to be deleted and sends it to the model to be
     * removed from the database. It receives an error message if the model passes back a
     * false boolean result. The method is called when the user clicks the remove button
     * in tab 2.
     * @param code - the module code to be deleted
     */
    public void removeModule(String code) throws SQLException {
        if (code.isEmpty()){
            view.getError("Invalid entry");
        }
        else {
            boolean removed = studentModel.removeModule(code);
            if (!removed)
                view.getError("No such module in system.");

            fillComboBoxes();
            clearText();
        }
    }
    /**
     * This method is called when the user clicks on the "Submit" button in tab 3.
     * It gets the studentNum of the selected student and the module selected by the user from the comboBoxes.
     * It performs validation checks on the entries.
     * If it passed the validation checks then the studentNum and moduleCode are sent to the
     * model to see if the entry already exists in the system. If it does already exist
     * an alert appears asking the user to use the update button instead. If it doesn't already exist
     * then are sent to the model to be added to the database.
     * to the database.
     * @param studentNum - id of student selected by user
     * @param moduleCode - id of the module selected by the user
     * @param grade - the grade entered by the user
     */
    public void submitModuleGrade(String studentNum, String moduleCode, String grade) throws SQLException {
        String isValidGrade = validator.validateModuleGrade(grade);
        String gradeUpper = grade.toUpperCase();

        if (!studentModel.gradeInTable(studentNum, moduleCode)) {

            if (!isValidGrade.equals("valid")) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(isValidGrade);
                alert.showAndWait();
            } else {
                boolean gradeAdded = studentModel.submitGrade(studentNum, moduleCode, gradeUpper);
                System.out.println("grade added: " + gradeAdded);
                clearText();
                fillComboBoxes();
            }
        }
        else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Already in system.\nPress update to change grade.");
            alert.showAndWait();
        }
    }

    /**
     * This method checks if the grade entered is valid and then sends it to the model to be
     * updated in the database.
     * @param selectedStudentID - id of the student selected by user
     * @param selectedModuleCode - id of the module selected by user
     * @param grade - grade entered by user
     */
    public void updateGrade(String selectedStudentID, String selectedModuleCode, String grade) throws SQLException {
        String isValidGrade = validator.validateModuleGrade(grade);
        grade = grade.toUpperCase();
        if (!isValidGrade.equals("valid")) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(isValidGrade);
            alert.showAndWait();
        }
        boolean isUpdated = studentModel.updateStudentGrade(selectedStudentID, selectedModuleCode, grade);
        System.out.println("Updated grade: " + isUpdated);
        clearText();
        fillComboBoxes();
    }

    /**
     * This method creates a memory leak by adding student objects to an arraylist until
     * memory runs out. It tracks the time taken, the amount of student objects made and the memory values
     * before and after.
     */
    public void startMemLeak(){
        long startTime = System.currentTimeMillis();
        ArrayList<Student> studentList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Runtime runTime = Runtime.getRuntime();
        long usedMemoryBefore = runTime.totalMemory() - runTime.freeMemory();
        try {
            Date dob = dateFormat.parse("01/01/2000");
            try {
                while (true) {
                    studentList.add(new Student("name", "id", dob, 2));
                }
            } catch (OutOfMemoryError ex) {
                System.out.println("Memory limit exceeded");
                long usedMemoryAfter = runTime.totalMemory() - runTime.freeMemory();
                System.out.println("Memory before: " + usedMemoryBefore + " | Memory after: " + usedMemoryAfter);
                System.out.println("Total number of students created: " + studentList.size());
                long execTime = System.currentTimeMillis() - startTime;
                System.out.println("Time taken in ms: " + execTime);
            }
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

}
