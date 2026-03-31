// data.ModuleResultData

// my packages
package Classes;

//import model.ModuleResult;
//import utilities.FilePath;

// java packages
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ModuleResultData {

    public List<ModuleResult> readAll() {
        List<ModuleResult> list = new ArrayList<>();
        Path path = Paths.get(FilePath.MODULE_RESULT_FILE);

        if (!Files.exists(path)) {
            System.err.println("ModuleResult file not found: " + path.toAbsolutePath());
            return list;
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.toLowerCase().startsWith("moduleresultid,")) continue;

                // moduleResultID, studentID, moduleID, lectureMark, tutoMark, finalMark, overallGrade
                String[] p = line.split(", ", -1);
                if (p.length < 7) {
                    System.err.println("Invalid moduleResult row (skip): " + line);
                    continue;
                }

                String moduleResultID = p[0].trim();
                String studentID = p[1].trim();
                String moduleID = p[2].trim();
                Double lectureMark = parseDoubleNullable(p[3].trim());
                Double tutoMark = parseDoubleNullable(p[4].trim());
                Double finalMark = parseDoubleNullable(p[5].trim());
                String overallGrade = parseNullableString(p[6].trim());

                list.add(new ModuleResult(moduleResultID, studentID, moduleID,
                        lectureMark, tutoMark, finalMark, overallGrade));
            }
        } catch (IOException e) {
            System.err.println("Error reading moduleResult file: " + e.getMessage());
        }

        return list;
    }

    public List<ModuleResult> findByStudentId(String studentId) {
        if (studentId == null) return Collections.emptyList();

        List<ModuleResult> out = new ArrayList<>();
        for (ModuleResult mr : readAll()) {
            if (studentId.equalsIgnoreCase(mr.getStudentID())) out.add(mr);
        }
        return out;
    }

    public ModuleResult findByStudentAndModule(String studentId, String moduleId) {
        if (studentId == null || moduleId == null) return null;

        for (ModuleResult mr : readAll()) {
            if (studentId.equalsIgnoreCase(mr.getStudentID())
                    && moduleId.equalsIgnoreCase(mr.getModuleID())) {
                return mr;
            }
        }
        return null;
    }

    public boolean update(ModuleResult updated) {
        if (updated == null || updated.getModuleResultID() == null) return false;

        List<ModuleResult> list = readAll();
        boolean replaced = false;

        for (int i = 0; i < list.size(); i++) {
            if (updated.getModuleResultID().equalsIgnoreCase(list.get(i).getModuleResultID())) {
                list.set(i, updated);
                replaced = true;
                break;
            }
        }

        if (!replaced) return false;
        return writeAll(list);
    }

    public boolean writeAll(List<ModuleResult> list) {
        Path path = Paths.get(FilePath.MODULE_RESULT_FILE);
        Path tmp = Paths.get(FilePath.MODULE_RESULT_FILE + ".tmp");

        try (BufferedWriter bw = Files.newBufferedWriter(tmp)) {
            for (ModuleResult mr : list) {
                bw.write(toCsvRow(mr));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing temp moduleResult file: " + e.getMessage());
            return false;
        }

        try {
            Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Error replacing moduleResult file: " + e.getMessage());
            return false;
        }
    }

    private String toCsvRow(ModuleResult mr) {
        return String.join(", ",
                safe(mr.getModuleResultID()),
                safe(mr.getStudentID()),
                safe(mr.getModuleID()),
                toCsvNullable(mr.getLectureMark()),
                toCsvNullable(mr.getTutorialMark()),
                toCsvNullable(mr.getFinalMark()),
                toCsvNullable(mr.getOverallGrade())
        );
    }

    private String safe(String v) {
        return v == null ? "" : v.trim();
    }

    private Double parseDoubleNullable(String v) {
        if (v == null) return null;
        v = v.trim();
        if (v.isEmpty() || v.equalsIgnoreCase("NULL")) return null;

        try {
            return Double.parseDouble(v);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    private String parseNullableString(String v) {
        if (v == null) return null;
        v = v.trim();
        if (v.isEmpty() || v.equalsIgnoreCase("NULL")) return null;
        return v;
    }

    private String toCsvNullable(Object v) {
        return (v == null) ? "NULL" : String.valueOf(v).trim();
    }
    
    public boolean existsStudentModule(String studentId, String moduleId) {
        if (studentId == null || moduleId == null) return false;
        for (ModuleResult mr : readAll()) {
            if (studentId.equalsIgnoreCase(mr.getStudentID())
                    && moduleId.equalsIgnoreCase(mr.getModuleID())) {
                return true;
            }
        }
        return false;
    }

    public String generateNextModuleResultId() {
        int max = 0;
        for (ModuleResult mr : readAll()) {
            String id = mr.getModuleResultID();
            if (id == null) continue;
            id = id.trim().toUpperCase();
            if (!id.startsWith("MR")) continue;

            String numStr = id.substring(2).replaceAll("[^0-9]", "");
            if (numStr.isEmpty()) continue;

            try {
                int n = Integer.parseInt(numStr);
                if (n > max) max = n;
            } catch (Exception ignored) {}
        }
        return String.format("MR%05d", max + 1);
    }

    public boolean append(ModuleResult mr) {
        if (mr == null) return false;
        Path path = Paths.get(FilePath.MODULE_RESULT_FILE);

        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(toCsvRow(mr));
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error appending moduleResult: " + e.getMessage());
            return false;
        }
    }

}
