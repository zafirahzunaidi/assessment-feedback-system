/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Student;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FilePath {
    // base directory
    public static final String DATA_DIR = "";
    
    // authentication
    public static final String USER_FILE = DATA_DIR + "user.txt";
    
    // student
    public static final String STUDENT_FILE = DATA_DIR + "student.txt";
    
    // class
    public static final String CLASS_FILE = DATA_DIR + "class.txt";
    public static final String REGISTERED_CLASS_FILE = DATA_DIR + "registeredClass.txt";
    
    // assessment + results
    public static final String ASSESSMENT_FILE = DATA_DIR + "assessment.txt";
    public static final String RESULT_FILE = DATA_DIR + "result.txt";
    public static final String MODULE_RESULT_FILE = DATA_DIR + "moduleResult.txt";
    public static final String GRADING_SYSTEM_FILE = DATA_DIR + "gradingSystem.txt";
    
    // feedback (comment)
    public static final String COMMENT_FILE = DATA_DIR + "comment.txt";
    
    // academic info
    public static final String FACULTY_FILE = DATA_DIR + "faculty.txt";
    public static final String COURSE_FILE = DATA_DIR + "course.txt";
    public static final String MODULE_FILE = DATA_DIR + "module.txt";
    public static final String REQUIRED_MODULE_FILE = DATA_DIR + "requiredModule.txt";
    
    // others
    public static final String LECTURER_FILE = DATA_DIR + "lecturer.txt";
    public static final String ACADEMIC_LEADER_FILE = DATA_DIR + "academicLeader.txt";
    public static final String MODULE_LECTURER_FILE = DATA_DIR + "moduleLecturer.txt";
    
    private FilePath() {}
    
    // read txt (simple comma split), skip the first header row
    public static List<String[]> readAll(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            throw new IllegalStateException(
                "File not found: " + f.getAbsolutePath()
                + "\nFix utilities.FilePath OR ensure txt files are in the correct run directory."
            );
        }

        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(", ", -1);
                for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();

                rows.add(parts);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed reading: " + filePath, e);
        }
        return rows;
    }
}
