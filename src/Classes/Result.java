/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

/**
 *
 * @author aqilahazis
 */
public class Result {
    private String resultID;  // Rxxxxx
    private String studentID;
    private String assessID;
    private Double markReceived; // can be null
    private String feedback;      // can be null / "NULL"

    public Result() {}
    
    public Result(String studentID, String assessID,
                  double markReceived, String feedback) {
        this.studentID = studentID;
        this.assessID = assessID;
        this.markReceived = markReceived;
        this.feedback = feedback;
    }

    public Result(String resultID, String studentID, String assessID,
                  Double markReceived, String feedback) {
        this.resultID = resultID;
        this.studentID = studentID;
        this.assessID = assessID;
        this.markReceived = markReceived;
        this.feedback = feedback;
    }

    public String getResultID() { return resultID; }
    public void setResultID(String resultID) { this.resultID = resultID; }

    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }

    public String getAssessID() { return assessID; }
    public void setAssessID(String assessID) { this.assessID = assessID; }

    public Double getMarkReceived() { return markReceived; }
    public void setMarkReceived(Double markReceived) { this.markReceived = markReceived; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    @Override
    public String toString() {
        return resultID + " - " + studentID + " - " + assessID + " : " + markReceived;
    }
}
