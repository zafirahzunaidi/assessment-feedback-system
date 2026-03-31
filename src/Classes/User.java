package Classes;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class User {
    
    private String username;
    private String password;
    private String role;     // "Student", "Lecturer", "Admin", "Academic Leader"
    public User() {}
    
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    // Getters
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public String getRole(){
        return role;
    }
    
    
    // Setters
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password; 
    }
    
    public void setRole(String role){  
        this.role = role;
    }
    
    @Override
    public String toString() {
        return username + " (" + role + ")";
    }
    

    public static User login(String username, String password) {

        try (BufferedReader br = new BufferedReader(new FileReader("user.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\s*,\\s*");

                String fileUser = data[0];
                String filePass = data[1];
                String fileRole = data[2];

                if (fileUser.equals(username) && filePass.equals(password)) {
                    return new User(fileUser, filePass, fileRole);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // login failed
    }


    public void checkLogin(String username, String password, JFrame admindashboard, JFrame loginpage, 
            JFrame lectdashboard, JFrame studentdashboard, JFrame acadleaddashboard){
        boolean exist = false;
        List<String> data = AdminTask.readtxt("user.txt");
        System.out.print(data);
        
        OUTER:
        for (int i = 1; i < data.size(); i++) {
            String row = data.get(i);
            String[] eachline = row.split(", ");
            System.out.println(eachline[0]);
            if (eachline[0].equals(username)) {
                exist = true;
                if (!eachline[1].equals(password)) {
                    JOptionPane.showMessageDialog(null,
                            "Incorrect password. Try again",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    break;
                } else {
                    setUsername(username);
                    setPassword(password);
                    
                    admindashboard.addComponentListener(new ComponentAdapter() {
                                @Override
                                public void componentHidden(ComponentEvent e) {
                                    loginpage.setVisible(true);
                                }
                            });
                    
                    acadleaddashboard.addComponentListener(new ComponentAdapter() {
                                @Override
                                public void componentHidden(ComponentEvent e) {
                                    loginpage.setVisible(true);
                                }
                            });
                    
                    studentdashboard.addComponentListener(new ComponentAdapter() {
                                @Override
                                public void componentHidden(ComponentEvent e) {
                                    loginpage.setVisible(true);
                                }
                            });
                    
                    lectdashboard.addComponentListener(new ComponentAdapter() {
                                @Override
                                public void componentHidden(ComponentEvent e) {
                                    loginpage.setVisible(true);
                                }
                            });
                    
                    switch (eachline[2]) {
                        case "Admin":
                            setRole("Admin");
                            System.out.print("hycufevuber");
                            admindashboard.setVisible(true);
                            loginpage.setVisible(false);
                            
//                            AdminTask.initUsernameRole(getUsername(), getRole());
                            
                            break OUTER;
                        case "Academic Leader":
                            acadleaddashboard.setVisible(true);
                            loginpage.setVisible(false);
                            break OUTER;
                        case "Lecturer":
                            setRole("Lecturer");

                            Session.setUsername(username);
                            Session.setRole("Lecturer");

                            System.out.println("LOGIN SUCCESS → " + Session.getUsername());

                            lectdashboard.setVisible(true);
                            loginpage.setVisible(false);
                            break OUTER;


                        case "Student":
                            studentdashboard.setVisible(true);
                            loginpage.setVisible(false);
                            break OUTER;
                        default:
                            break;
                    }
                }
            } else {
                exist = false;
            }
        }
        
        if (exist = false){
            JOptionPane.showMessageDialog(null, 
                                      "Username is incorrect.", 
                                      "Error", 
                                      JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void createUser(String username, String password, String role){ //admin
        if (username.isEmpty() || password.isEmpty() || role.isEmpty()){
            JOptionPane.showMessageDialog(null, 
                                      "Please fill all field", 
                                      "Error", 
                                      JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //Object[] userrow = {username, password, "Student"};
        List<String> userrow = List.of(username, password, role);
        
        //add to text file
        AdminTask task = new AdminTask();
        task.writetxt(userrow, "user.txt", 2);
    }
    
    public void deleteuser(String rowUsername){ //hasnt been tested
        List<String> containLines = new ArrayList<>();
        AdminTask task = new AdminTask();
        List<String> data = task.readtxt("user.txt");
        
        for(int i = 0; i < data.size(); i++){
                String row = data.get(i);
                String[] eachline = row.split(", ");
                if(!eachline[0].equals(rowUsername)){
                   List<String> eachrow = Arrays.asList(row);
                   containLines.addAll(eachrow);
                }
            }
        
        task.writetxt(containLines, "user.txt", 1);
    }
    
    public void edituserInit(JTextField txtPw){
        String rowUsername = getUsername();
        System.out.println(rowUsername);
        
        AdminTask task = new AdminTask();
        List<String> data = task.readtxt("user.txt");
        
        for(int i = 1; i < data.size(); i++){
                String row = data.get(i);
                String[] eachline = row.split(", ");
                if(eachline[0].equals(rowUsername)){
                    txtPw.setText(eachline[1]);
                }
            }
    }
    
    public static boolean checkusernameExist(String oldusername, String username){
        boolean exist = false;
        List<String> data = AdminTask.readtxt("user.txt");
        
        for(int i = 1; i < data.size(); i++){
                String row = data.get(i);
                String[] eachline = row.split(", ");
                if(eachline[0].equals(oldusername)){
                    exist = true;
                    break;
                    
                } else {
                    exist = false;
                }
            }
        return exist;
    }
    
    public static void editAdmin(String oldusername, String username, String password){
        List<String> containLines = new ArrayList<>();
        
        List<String> userrow = List.of(username, password, "Admin");
        
        List<String> data = AdminTask.readtxt("user.txt");
        for(int i = 0; i < data.size(); i++){
            String row = data.get(i);
                String[] eachline = row.split(", ");
                if(!eachline[0].equals(oldusername)){
                    List<String> eachrow = Arrays.asList(row);
                       containLines.addAll(eachrow);
                }
                else{
                    String editedrow = String.join(", ", userrow);
                    containLines.addAll(Arrays.asList(editedrow));
                }
        }
        
        AdminTask.writetxt(containLines, "user.txt", 1);
    }
    
    public String toFileString() {
        return username + ", " + password + ", " + role;
    }
    
    public static User fromFileString(String line) {
        String[] parts = line.split(", ");
        return new User(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim()
        );
    }
    
        public static List<User> loadUsers(String filePath) {

        List<User> users = new ArrayList<>();
        List<String> lines = FileFunctions.readAllLines(filePath);

        for (String line : lines) {

            if (line.toLowerCase().startsWith("username")) {
                continue;
            }

            try {
                users.add(User.fromFileString(line));
            } catch (Exception e) {
                System.err.println("Skipping invalid user line: " + line);
            }
        }

        return users;
    }

        public static void saveUsers(String filePath, List<User> users) {

        List<String> lines = new ArrayList<>();
        lines.add("username,password,role");

        for (User u : users) {
            lines.add(u.toFileString());
        }

        FileFunctions.writeAllLines(filePath, lines);
    }

        public static void addUser(String filePath, User user) {
        FileFunctions.appendLine(filePath, user.toFileString());
    }
    
}
