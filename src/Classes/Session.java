/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

public final class Session {
    private static String username;
    private static String role;
    private static String password;
    
    private static String studentId;
    private static String selectedClassId;    
    
    private Session() {}
    
    // setters
    public static void setUsername(String username) {
        Session.username = username;
    }

    public static void setRole(String role) {
        Session.role = role;
    }
    
    public static void setPassword(String password) {
        Session.password = password;
    }

    public static void setStudentId(String studentId) {
        Session.studentId = studentId;
    }
    
    public static void setSelectedClassId(String selectedClassId) {
        Session.selectedClassId = selectedClassId;
    }

    // getters
    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static String getPassword() {
        return password;
    }
        
    public static String getStudentId() {
        return studentId;
    }
    
    public static String getSelectedClassId() {
        return selectedClassId;
    }
    
    // helper
    public static boolean isLoggedIn() {
        return username != null && !username.trim().isEmpty()
                && role != null && !role.trim().isEmpty();
    }
    
    public static void startSession(String user, String userRole, String userPassword) {
        username = user;
        role = userRole;
        password = userPassword;
    }
    
    public static void endSession() {
        username = null;
        role = null;
    }

    public static void clear() {
        username = null;
        role = null;
        studentId = null;
        selectedClassId = null;
    }

}
