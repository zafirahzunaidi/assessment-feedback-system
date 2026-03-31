/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

// my packages
//import model.User;
//import utilities.FilePath;
 // java packages
import java.io.*;
import java.nio.file.*;
import java.util.*;


public class UserData {
    public List<User> readAll() {
        List<User> users = new ArrayList<>();
        Path path = Paths.get(FilePath.USER_FILE);

        if (!Files.exists(path)) {
            System.err.println("User file not found: " + path.toAbsolutePath());
            return users;
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // username, password, role
                String[] p = line.split(", ");
                if (p.length < 3) {
                    System.err.println("Invalid user row (skip): " + line);
                    continue;
                }

                String username = p[0].trim();
                String password = p[1].trim();
                String role = p[2].trim();

                users.add(new User(username, password, role));
            }
        } catch (IOException e) {
            System.err.println("Error reading user file: " + e.getMessage());
        }

        return users;
    }

    public User findByUsername(String username) {
        if (username == null) return null;

        for (User u : readAll()) {
            if (username.equalsIgnoreCase(u.getUsername())) {
                return u;
            }
        }
        return null;
    }

    // update password for a username in user.txt
    public boolean updatePassword(String username, String newPassword) {
        if (username == null || username.trim().isEmpty()) return false;
        if (newPassword == null || newPassword.trim().isEmpty()) return false;

        List<User> users = readAll();
        boolean updated = false;

        for (User u : users) {
            if (username.equalsIgnoreCase(u.getUsername())) {
                u.setPassword(newPassword);
                updated = true;
                break;
            }
        }

        if (!updated) return false;
        return writeAll(users);
    }

    public boolean writeAll(List<User> users) {
        Path path = Paths.get(FilePath.USER_FILE);
        Path tmp = Paths.get(FilePath.USER_FILE + ".tmp");

        try (BufferedWriter bw = Files.newBufferedWriter(tmp)) {
            for (User u : users) {
                bw.write(toCsvRow(u));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing temp user file: " + e.getMessage());
            return false;
        }

        try {
            Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Error replacing user file: " + e.getMessage());
            return false;
        }
    }

    private String toCsvRow(User u) {
        return String.join(", ",
                safe(u.getUsername()),
                safe(u.getPassword()),
                safe(u.getRole())
        );
    }

    private String safe(String v) {
        return v == null ? "" : v.trim();
    }
}
