package com.example.assignment1_part3.model;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is responsible for all logic related to the database
 */
public class StudentContainer {

    private final Connection connection;

    public StudentContainer(Connection connection){
        this.connection = connection;
    }

    /**
     * This method receives a student object and then uses a sql statement to add the student to the students table in
     * the database. It converts the data to be compatible with mySQL dates.
     * @param student - the student to be added to the database
     * @return - a boolean value, true if the student was added to the students table and false if not
     */
    public boolean addStudent(Student student) {
        try{
            String sql = "INSERT INTO students (studentNum, DOB, currentSem, sName) VALUE (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, student.getId());
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date parsed = format.parse(student.getDob());
            java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
            statement.setDate(2, sqlDate);
            statement.setInt(3, student.getSemester());
            statement.setString(4, student.getName());

            int rowsInserted = statement.executeUpdate();
            statement.close();
            // Return true if the statement was executed successfully and a student record was inserted
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Return false if an exception was thrown while executing the statement
            return false;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method uses a mySQL statement which looks for an entry with a matching studentNum and then deletes it.
     * @param studentNum - id of the student to be deleted
     * @return - a boolean value, true if the student has been successfully deleted from the database, false if not
     */
    public boolean removeStudent(String studentNum) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE studentNum = ?");
            statement.setString(1, studentNum);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets the information on all the students in the database and adds the details to a string. This is used
     * to fill the textArea in tab 1.
     * @return - the string value of all the student records in the database
     */
    public String getStudents() {
        try {
            StringBuilder string = new StringBuilder();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("Select * FROM students");
            while (result.next()) {
                String studentID = result.getString("studentNum");
                String name = result.getString("sName");
                Date dob = result.getDate("DOB");
                int semester = result.getInt("currentSem");
                string.append(studentID).append("\t").append(name).append("\t").append(dob).append("\t").append(semester).append("\n");
            }
            statement.close();
            result.close();
            return string.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method finds the details of a specified student in the database and creates an object of the student
     * which is returned.
     * @param studentNum - the id of the student whose details are wanted
     * @return - the student object
     */
    public Student getStudent(String studentNum) throws SQLException {
        Student myStudent = null;
        String sql = "SELECT * FROM students WHERE studentNum = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, studentNum);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String studentID = resultSet.getString("studentNum");
            String name = resultSet.getString("sName");
            Date dob = resultSet.getDate("DOB");
            int semester = resultSet.getInt("currentSem");
            myStudent = new Student(studentID, name, dob, semester);
        }
        statement.close();
        resultSet.close();
        return myStudent;
    }

    /**
     * This method gets all the students in the database and then gets selects their ids to add them to a string
     * @return - the string of all the student ids
     */
    public ArrayList<String> getStudentIDs(){
        ArrayList<String> studentIDs = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("Select * FROM students");
            while (result.next()) {
                String studentID = result.getString("studentNum");
                studentIDs.add(studentID);
            }
            statement.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return studentIDs;
    }

    /**
     * This method gets all the modules from the database and then selects their codes to add to a string
     * @return - a string of all the modules codes
     */
    public ArrayList<String> getModuleCodes(){
        ArrayList<String> moduleCodes = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("Select * FROM modules");
            while (result.next()) {
                String moduleCode = result.getString("moduleID");
                moduleCodes.add(moduleCode);
            }
            statement.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return moduleCodes;
    }

    /**
     * This method receives a student number and then creates a sql statement to see if the student number exists in
     * the database.
     * @param studentNum - the number of the student to be checked
     * @return - a boolean value, true if the student number exists in the table, false if it doesn't
     */
    public boolean studentInTable(String studentNum) throws SQLException {
        String sql = "SELECT * FROM students WHERE studentNum = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, studentNum);
        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }

    /**
     * This method checks if a specified module already exists in the database
     * @param moduleCode - the module code to be compared against the database
     * @return - a boolean, true if the value exists, false if it doesn't
     */
    public boolean moduleInTable(String moduleCode) throws SQLException{
        String sql = "SELECT * FROM modules WHERE moduleID = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, moduleCode);
        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }

    /**
     * This method adds a module to the module table in the database
     * @param myModule - the module to be added to the database
     * @return - true if the module is successfully added, false otherwise
     */
    public boolean addModule(Module myModule){
        try{
            String sql = "INSERT INTO modules (moduleID, mName, semester) VALUE (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, myModule.getCode());
            statement.setString(2, myModule.getName());
            statement.setInt(3, myModule.getSemester());

            int rowsInserted = statement.executeUpdate();
            // Return true if the statement was executed successfully and a student record was inserted
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Return false if an exception was thrown while executing the statement
            return false;
        }
    }

    /**
     * This module removes a record from the modules table in the database.
     * @param code - the id of the module to be deleted
     * @return - true if the record is deleted, false if otherwise
     */
    public boolean removeModule(String code){
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM modules WHERE moduleID = ?");
            statement.setString(1, code);
            int rowsDeleted = statement.executeUpdate();
            statement.close();
            return rowsDeleted > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method enters a record into the grades table in the database.
     * @param studentNum - the id of the student to be added to the studentNum field of the table
     * @param moduleNum - the id of the module to be added to the moduleID field of the table
     * @param grade - the grade to be entered to the grade field in the table
     * @return - true if the record was successfully added to the database, false if not
     */
    public boolean submitGrade(String studentNum, String moduleNum, String grade){
        try{
            String sql = "INSERT INTO grades (moduleID, studentNum, grade) VALUE (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, moduleNum);
            statement.setString(2, studentNum);
            grade = grade.toUpperCase();
            statement.setString(3, grade);

            int rowsInserted = statement.executeUpdate();
            // Return true if the statement was executed successfully
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Return false if an exception was thrown while executing the statement
            return false;
        }
    }

    /**
     * This method gets the grade information for a specific student from the database. If the grade is above 40 (a pass)
     * then it is appended to a string
     * @param studentNum - the id of the student whose grades are requested
     * @return - a string containing the student's grade information
     */
    public String getGradeInfo(String studentNum){
        StringBuilder sb = new StringBuilder();
        try {
            // Query the grades table based on the studentNum
            String query = "SELECT moduleID, grade FROM grades WHERE studentNum = ? AND (grades.grade >= 40 OR grades.grade = 'NP')";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, studentNum);
            ResultSet rs = statement.executeQuery();

            // Append the grade information to a StringBuilder
            while (rs.next()) {
                String moduleCode = rs.getString("moduleID");
                String grade = rs.getString("grade");
                sb.append(moduleCode).append(": ").append(grade.toUpperCase()).append("\n");
            }

            // Close the ResultSet and PreparedStatement objects
            rs.close();
            statement.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * This method updates the grade in the grades table to the grade passed into the method.
     * @param studentId - id of student whose grade is to be changed
     * @param moduleCode - id of the module with the grade to be changed
     * @param grade - the new grade
     * @return - true if the record has be updated successfully, false if not
     */
    public boolean updateStudentGrade(String studentId, String moduleCode, String grade) throws SQLException {
        try {
            String sql = "UPDATE grades SET grade = ? WHERE studentNum = ? AND moduleID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, grade);
            statement.setString(2, studentId);
            statement.setString(3, moduleCode);

            int rowsInserted = statement.executeUpdate();
            statement.close();
            // Return true if the statement was executed successfully
            return rowsInserted > 0;
        }
        catch (SQLException e){
            e.printStackTrace();
            // Return false if an exception was thrown while executing the statement
            return false;
        }
    }

    /**
     * This method finds out if a particular record of a grade exists in the grades table
     * @param studentNum - the id of the student whose grade we are checking for
     * @param moduleCode - the id of the module which has the grade we are looking for
     * @return - true if the record exists, false otherwise
     */
    public boolean gradeInTable(String studentNum, String moduleCode) throws SQLException {
        String sql = "SELECT * FROM grades WHERE moduleID = ? AND studentNum = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, moduleCode);
        statement.setString(2, studentNum);
        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }

    /**
     * This method joins two mySQL tables, grades and modules, to get the moduleID for a particular student and the
     * grade they received in it. It then adds this information to a string List.
     * @param studentNum - the id of the student whose module and grade info we are trying to find
     * @return - a List containing the moduleIDs and grades received
     */
    public List<String> getStudentModuleInfo(String studentNum) throws SQLException {
        String sql = "SELECT modules.moduleID, grades.grade FROM modules JOIN grades ON modules.moduleID = grades.moduleID WHERE grades.studentNum = ? AND (grades.grade >= 40 OR grades.grade = 'NP')";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, studentNum);
        ResultSet resultSet = statement.executeQuery();
        List<String> results = new ArrayList<>();
        while (resultSet.next()) {
            String moduleName = resultSet.getString("moduleID");
            String grade = resultSet.getString("grade"); // Changed grade to String type
            String result = moduleName + " : " + grade;
            results.add(result);
        }
        return results;
    }

}

