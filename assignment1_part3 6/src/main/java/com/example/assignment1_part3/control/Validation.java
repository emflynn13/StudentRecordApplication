package com.example.assignment1_part3.control;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Validation {
    public Validation(){
    }

    /**
     * This method ensures the user enters a valid student number.
     * @param studentNum - the number to be validated
     * @return - 0 if the length is wrong, 1 if it doesn't begin with an "R" or a 2 if it passes validation
     */
    public int validateStudentNumber(String studentNum){
        if (studentNum.length() != 9){
            return 0;
        }
        else if (!studentNum.startsWith("R")){
            return 1;
        }
        return 2;
    }

    /**
     * This method ensures dates are entered in the correct format and not a future date
     * @param date - string version of the date entered by user
     * @return - boolean stating whether the date is valid or not.
     */
    public boolean dateValidator(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
        dateFormat.setLenient(false);

        try {
            Date myDate = dateFormat.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(myDate);
            Calendar now = Calendar.getInstance();
            return cal.before(now);
        }
        catch (ParseException exception){
            return false;
        }
    }
    /**
     * This method prevents duplicate module entries, empty textFields, non-numerical grades and ensures
     * grades fall within a certain interval.
     * @param grade - grade received in this module
     * @return - returns "valid" is passes validation checks or the string for the error message if it doesn't
     */
    public String validateModuleGrade(String grade){
        try { // input validation on grade entry
            if (!grade.equalsIgnoreCase("NP")) {
                int moduleGrade = Integer.parseInt(grade);
                if (moduleGrade < 0 || moduleGrade > 100) {
                    return "Grade must be between 0 and 100";
                }
            }
        }
            catch (Exception e){
                return "Grade must be an number or NP if not completed.";
            }
        return "valid";
    }

}
