// my packages
package Classes;
//import model.Comment;
//import utilities.FilePath;

// java packages
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CommentData {

    public List<Comment> readAll() {
        List<Comment> list = new ArrayList<>();
        Path path = Paths.get(FilePath.COMMENT_FILE);

        if (!Files.exists(path)) {
            System.err.println("Comment file not found: " + path.toAbsolutePath());
            return list;
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.toLowerCase().startsWith("commentid,")) continue;

                // commentID, registeredClassID, comment
                String[] p = line.split(", ", 3); // comment may contain commas (safer)
                if (p.length < 3) {
                    System.err.println("Invalid comment row (skip): " + line);
                    continue;
                }

                String commentID = p[0].trim();
                String registeredClassID = p[1].trim();
                String comment = p[2].trim();

                list.add(new Comment(commentID, registeredClassID, comment));
            }
        } catch (IOException e) {
            System.err.println("Error reading comment file: " + e.getMessage());
        }

        return list;
    }

    public List<Comment> findByRegisteredClassId(String registeredClassId) {
        if (registeredClassId == null) return Collections.emptyList();

        List<Comment> out = new ArrayList<>();
        for (Comment c : readAll()) {
            if (registeredClassId.equalsIgnoreCase(c.getRegisteredClassID())) out.add(c);
        }
        return out;
    }

    public boolean append(Comment c) {
        if (c == null) return false;

        Path path = Paths.get(FilePath.COMMENT_FILE);
        try (BufferedWriter bw = Files.newBufferedWriter(path,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(toCsvRow(c));
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error appending comment: " + e.getMessage());
            return false;
        }
    }

    private String toCsvRow(Comment c) {
        return String.join(", ",
                safe(c.getCommentID()),
                safe(c.getRegisteredClassID()),
                safe(c.getComment())
        );
    }

    private String safe(String v) {
        return v == null ? "" : v.trim();
    }
}
