package com.example.assignment1_part3;

import com.example.assignment1_part3.control.StudentRecordController;
import com.example.assignment1_part3.control.Validation;
import com.example.assignment1_part3.model.Module;
import com.example.assignment1_part3.model.Student;
import com.example.assignment1_part3.model.StudentContainer;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentRecordTest {
    private final Validation validator;
    private final StudentContainer model;
    private final StudentRecordController control;


    StudentRecordTest() throws SQLException {
        this.validator = new Validation();
         this.control = new StudentRecordController();
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/OOP_Assign2", "root", "Arsenal1306");
        this.model = new StudentContainer(connection);
    }


    /**
     * This method test is the validateStudentNumber method in the validator class
     * successfully discerns between valid and invalid student numbers.
     */
    @Test
    void validateStudentNumber(){
        String input = "R00228904";
        int myResult = validator.validateStudentNumber(input);
        assertEquals(2, myResult);


        String input2 = "100228904";
        int myResult2 = validator.validateStudentNumber(input2);
        assertEquals(1, myResult2);

        String input3 = "R0022890454";
        int myResult3 = validator.validateStudentNumber(input3);
        assertEquals(0, myResult3);
    }

    /**
     * This method test is the validateDate method in the validator class
     * successfully discerns between valid and invalid dates.
     */
    @Test
    void validateDate(){
        String input1 = "8765432";
        boolean myResult1 = validator.dateValidator(input1);
        assertFalse(myResult1);

        String input2 = "33/33/2000";
        boolean myResult2 = validator.dateValidator(input2);
        assertFalse(myResult2);

        String input3 = "13/03/2002";
        boolean myResult3 = validator.dateValidator(input3);
        assertTrue(myResult3);
    }

    /**
     * This method tests the getCode() method in the Module class. When a module is created you should be able
     * to access its code.
     */
    @Test
    void testGetCode(){
        String name = "French";
        String code = "COMP8019";
        int semester = 2;
        Module myModule = new Module(name, code, semester);

        String myResult = myModule.getCode();
        assertEquals(myResult, code);
    }

    /**
     * This method tests whether the getName method in the Student class works.
     * When a Student object is created you should me able to access their name.
     */
    @Test
    void testGetName() throws ParseException {
        String name = "Klara";
        String sNum = "R00227894";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date dob = dateFormat.parse("01/01/2000");
        int semester = 3;

        Student myStudent = new Student(name, sNum, dob, semester);

        String result = myStudent.getName();
        assertEquals(result, name);

    }
    /**
     * This method tests whether the organizeModule method in the controller successfully organizes
     * modules by grade
     */
    @Test
    public void testOrganizeModulesByGrade(){
        List<String> input = Arrays.asList("CS101 : 85", "MATH102 : 90", "PHY103 : 78");
        String expectedOutput = "PHY103 : 78\nCS101 : 85\nMATH102 : 90\n";

        String actualOutput = control.organizeModules(true, input);
        assertEquals(expectedOutput, actualOutput);
    }
    /**
     * This method tests whether the organizeModule method in the controller successfully organizes
     * modules alphabetically
     */
    @Test
    void testOrganizeModulesByName() {
        List<String> inputModules = Arrays.asList("CS101 : 85", "MATH102 : 90", "PHY103 : 78");
        String expectedOutput = "CS101 : 85\nMATH102 : 90\nPHY103 : 78\n";

        String actualOutput = control.organizeModules(false, inputModules);

        assertEquals(expectedOutput, actualOutput);
    }
    /**
     * This tests the method in the model which checks if a student exists in the table based
     * on their student id It returns true if the student exists, false if they don't.
     * This will only work when connected to the database.
     */
    @Test
    void testGetStudentsQuery() throws SQLException {
        String id = "R00221673";
        boolean result = model.studentInTable(id);
        assertTrue(result);

    }
}