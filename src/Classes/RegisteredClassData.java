/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

// my packages
//import model.RegisteredClass;
//import utilities.FilePath;

// java packages
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.util.*;

public class RegisteredClassData {
    public List<RegisteredClass> readAll() {
        List<RegisteredClass> list = new ArrayList<>();
        Path path = Paths.get(FilePath.REGISTERED_CLASS_FILE);

        if (!Files.exists(path)) {
            System.err.println("Registered class file not found: " + path.toAbsolutePath());
            return list;
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                // skip header
                if (line.toLowerCase().startsWith("registeredclassid,")) continue;

                // registeredClassID, classID, studentID, registrationDate
                String[] p = line.split(", ");
                if (p.length < 4) {
                    System.err.println("Invalid registeredClass row (skip): " + line);
                    continue;
                }

                RegisteredClass rc = new RegisteredClass(
                        p[0].trim(),
                        p[1].trim(),
                        p[2].trim(),
                        p[3].trim()
                );
                list.add(rc);
            }
        } catch (IOException e) {
            System.err.println("Error reading registeredClass file: " + e.getMessage());
        }

        return list;
    }

    public List<RegisteredClass> findByStudentId(String studentId) {
        if (studentId == null) return Collections.emptyList();

        List<RegisteredClass> out = new ArrayList<>();
        for (RegisteredClass rc : readAll()) {
            if (studentId.equalsIgnoreCase(rc.getStudentID())) {
                out.add(rc);
            }
        }
        return out;
    }

    public boolean exists(String studentId, String classId) {
        if (studentId == null || classId == null) return false;

        for (RegisteredClass rc : readAll()) {
            if (studentId.equalsIgnoreCase(rc.getStudentID())
                    && classId.equalsIgnoreCase(rc.getClassID())) {
                return true;
            }
        }
        return false;
    }

    public int countByClassId(String classId) {
        if (classId == null) return 0;

        int count = 0;
        for (RegisteredClass rc : readAll()) {
            if (classId.equalsIgnoreCase(rc.getClassID())) {
                count++;
            }
        }
        return count;
    }

    public boolean append(RegisteredClass rc) {
        if (rc == null) return false;

        Path path = Paths.get(FilePath.REGISTERED_CLASS_FILE);

        try {
            ensureEndsWithNewline(path);

            try (BufferedWriter bw = Files.newBufferedWriter(
                    path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

                bw.write(toCsvRow(rc));
                bw.newLine();
            }
            return true;

        } catch (IOException e) {
            System.err.println("Error appending registeredClass: " + e.getMessage());
            return false;
        }
    }
    
    private void ensureEndsWithNewline(Path path) throws IOException {
        if (!Files.exists(path)) return;

        long size = Files.size(path);
        if (size == 0) return;

        byte last;
        try (SeekableByteChannel ch = Files.newByteChannel(path, StandardOpenOption.READ)) {
            ch.position(size - 1);
            ByteBuffer bb = ByteBuffer.allocate(1);
            ch.read(bb);
            bb.flip();
            last = bb.get();
        }

        if (last != '\n' && last != '\r') {
            Files.write(path, System.lineSeparator().getBytes(),
                    StandardOpenOption.APPEND);
        }
    }

    public boolean writeAll(List<RegisteredClass> list) {
        Path path = Paths.get(FilePath.REGISTERED_CLASS_FILE);
        Path tmp = Paths.get(FilePath.REGISTERED_CLASS_FILE + ".tmp");

        try (BufferedWriter bw = Files.newBufferedWriter(tmp)) {
            for (RegisteredClass rc : list) {
                bw.write(toCsvRow(rc));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing temp registeredClass file: " + e.getMessage());
            return false;
        }

        try {
            Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Error replacing registeredClass file: " + e.getMessage());
            return false;
        }
    }

    private String toCsvRow(RegisteredClass rc) {
        return String.join(", ",
                safe(rc.getRegisteredClassID()),
                safe(rc.getClassID()),
                safe(rc.getStudentID()),
                safe(rc.getRegistrationDate())
        );
    }

    private String safe(String v) {
        return v == null ? "" : v.trim();
    }
}
