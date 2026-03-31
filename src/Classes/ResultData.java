// data.ResultData

// my packages
package Classes;

// java packages
//import model.Result;
//import utilities.FilePath;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ResultData {

    public List<Result> readAll() {
        List<Result> results = new ArrayList<>();
        Path path = Paths.get(FilePath.RESULT_FILE);

        if (!Files.exists(path)) {
            System.err.println("Result file not found: " + path.toAbsolutePath());
            return results;
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // skip header
                if (line.toLowerCase().startsWith("resultid,")) continue;

                // resultID, studentID, assessID, markReceived, feedback
                String[] p = line.split(", ", -1); // keep empty columns
                if (p.length < 5) {
                    System.err.println("Invalid result row (skip): " + line);
                    continue;
                }

                String resultID = p[0].trim();
                String studentID = p[1].trim();
                String assessID = p[2].trim();
                Double markReceived = parseDoubleNullable(p[3].trim());
                String feedback = parseNullableString(p[4].trim());

                results.add(new Result(resultID, studentID, assessID, markReceived, feedback));
            }
        } catch (IOException e) {
            System.err.println("Error reading result file: " + e.getMessage());
        }

        return results;
    }

    public List<Result> findByStudentId(String studentId) {
        if (studentId == null) return Collections.emptyList();

        List<Result> out = new ArrayList<>();
        for (Result r : readAll()) {
            if (studentId.equalsIgnoreCase(r.getStudentID())) out.add(r);
        }
        return out;
    }

    public Result findByStudentAndAssess(String studentId, String assessId) {
        if (studentId == null || assessId == null) return null;

        for (Result r : readAll()) {
            if (studentId.equalsIgnoreCase(r.getStudentID())
                    && assessId.equalsIgnoreCase(r.getAssessID())) {
                return r;
            }
        }
        return null;
    }

    public boolean update(Result updated) {
        if (updated == null || updated.getResultID() == null) return false;

        List<Result> list = readAll();
        boolean replaced = false;

        for (int i = 0; i < list.size(); i++) {
            if (updated.getResultID().equalsIgnoreCase(list.get(i).getResultID())) {
                list.set(i, updated);
                replaced = true;
                break;
            }
        }

        if (!replaced) return false;
        return writeAll(list);
    }

    public boolean writeAll(List<Result> list) {
        Path path = Paths.get(FilePath.RESULT_FILE);
        Path tmp = Paths.get(FilePath.RESULT_FILE + ".tmp");

        try (BufferedWriter bw = Files.newBufferedWriter(tmp)) {
            for (Result r : list) {
                bw.write(toCsvRow(r));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing temp result file: " + e.getMessage());
            return false;
        }

        try {
            Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Error replacing result file: " + e.getMessage());
            return false;
        }
    }

    private String toCsvRow(Result r) {
        // resultID, studentID, assessID, markReceived, feedback
        return String.join(", ",
                safe(r.getResultID()),
                safe(r.getStudentID()),
                safe(r.getAssessID()),
                toCsvNullable(r.getMarkReceived()),
                toCsvNullable(r.getFeedback())
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
}
