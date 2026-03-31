/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

// my packages
//import model.Student;
//import utilities.FilePath;

// java packages
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class StudentData {
    public List<Student> readAll() {
        List<Student> students = new ArrayList<>();
        Path path = Paths.get(FilePath.STUDENT_FILE);

        if (!Files.exists(path)) {
            System.err.println("Student file not found: " + path.toAbsolutePath());
            return students;
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.toLowerCase().startsWith("studentid,")) continue;

                // studentID, username, name, email, phoneNumber, currentCourse, CGPA
                String[] parts = line.split(", ");
                if (parts.length < 7) {
                    System.err.println("Invalid student row (skip): " + line);
                    continue;
                }

                String studentID = parts[0].trim();
                String username = parts[1].trim();
                String name = parts[2].trim();
                String email = parts[3].trim();
                String phone = parts[4].trim();
                String currentCourse = parts[5].trim();
                double cgpa = parseDoubleSafe(parts[6].trim(), 0.0);

                Student s = new Student(studentID, username, name, email, phone, currentCourse, cgpa);
                students.add(s);
            }
        } catch (IOException e) {
            System.err.println("Error reading student file: " + e.getMessage());
        }

        return students;
    }

    public Student findByUsername(String username) {
        if (username == null) return null;

        for (Student s : readAll()) {
            if (username.equalsIgnoreCase(s.getUsername())) {
                return s;
            }
        }
        return null;
    }

    public Student findByStudentId(String studentId) {
        if (studentId == null) return null;
        
        String key = studentId.trim();

        for (Student s : readAll()) {
            if (s != null && s.getStudentID() != null) {
                if (key.equalsIgnoreCase(s.getStudentID().trim())) {
                    return s;
                }
            }
        }
        return null;
    }

    // update student row by matching studentID
    public boolean update(Student updated) {
        if (updated == null || updated.getStudentID() == null) return false;

        List<Student> students = readAll();
        boolean replaced = false;

        for (int i = 0; i < students.size(); i++) {
            if (updated.getStudentID().equalsIgnoreCase(students.get(i).getStudentID())) {
                students.set(i, updated);
                replaced = true;
                break;
            }
        }

        if (!replaced) return false;
        return writeAll(students);
    }

    public boolean writeAll(List<Student> students) {
        Path path = Paths.get(FilePath.STUDENT_FILE);
        Path tmp = Paths.get(FilePath.STUDENT_FILE + ".tmp");

        try (BufferedWriter bw = Files.newBufferedWriter(tmp)) {
            for (Student s : students) {
                bw.write(toCsvRow(s));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing temp student file: " + e.getMessage());
            return false;
        }

        try {
            Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Error replacing student file: " + e.getMessage());
            return false;
        }
    }

    private String toCsvRow(Student s) {
        // studentID, username, name, email, phoneNumber, currentCourse, CGPA
        return String.join(", ",
                safe(s.getStudentID()),
                safe(s.getUsername()),
                safe(s.getName()),
                safe(s.getEmail()),
                safe(s.getPhoneNumber()),
                safe(s.getCurrentCourse()),
                String.valueOf(s.getCGPA())
        );
    }

    private String safe(String v) {
        return v == null ? "" : v.trim();
    }

    private double parseDoubleSafe(String v, double fallback) {
        if (v == null) return fallback;
        v = v.trim();
        if (v.isEmpty() || v.equalsIgnoreCase("NULL")) return fallback;

        try {
            return Double.parseDouble(v);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public Student findById(String studentId) {
        if (studentId == null) return null;
        String target = studentId.trim();

        for (Student s : readAll()) {
            if (s == null) continue;
            if (s.getStudentID() != null && s.getStudentID().trim().equalsIgnoreCase(target)) {
                return s;
            }
        }
        return null;
    }
}
