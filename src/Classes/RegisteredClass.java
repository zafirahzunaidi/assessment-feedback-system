/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.Date;

/**
 *
 * @author aqilahazis
 */
public class RegisteredClass {
    
    private String registeredClassID; // RCxxxxx
    private String classID;
    private String studentID;
    private String registrationDate;  // yyyy-mm-dd
    
    public RegisteredClass() {}

    public RegisteredClass(String registeredClassID, String classID,
                           String studentID, String registrationDate) {
        this.registeredClassID = registeredClassID;
        this.classID = classID;
        this.studentID = studentID;
        this.registrationDate = registrationDate;
    }

    public String getRegisteredClassID() { return registeredClassID; }
    public void setRegisteredClassID(String registeredClassID) { this.registeredClassID = registeredClassID; }

    public String getClassID() { return classID; }
    public void setClassID(String classID) { this.classID = classID; }

    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }

    public String getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }

    @Override
    public String toString() {
        return registeredClassID + " (" + classID + ")";
    }
    
    
}
