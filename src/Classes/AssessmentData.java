package Classes;

//import model.Assessment;
//import utilities.FilePath;
import java.util.*;

public class AssessmentData {

    public List<Assessment> findByClassId(String classId) {
        List<Assessment> list = new ArrayList<>();

        for (String[] r : FilePath.readAll(FilePath.ASSESSMENT_FILE)) {
            if (r.length < 5) continue;
            if (!r[1].trim().equalsIgnoreCase(classId)) continue;

            list.add(new Assessment(
                r[0].trim(),          // assessmentId
                r[1].trim(),          // classId
                r[2].trim(),          // type
                Double.parseDouble(r[3].trim()),
                Double.parseDouble(r[4].trim())
            ));
        }
        return list;
    }
}