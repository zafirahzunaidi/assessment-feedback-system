package Classes;

public class Comment {
    private String commentID;         // Cxxxx
    private String registeredClassID; // RCxxxxx
    private String comment;           // text

    public Comment() {}

    public Comment(String commentID, String registeredClassID, String comment) {
        this.commentID = commentID;
        this.registeredClassID = registeredClassID;
        this.comment = comment;
    }

    public String getCommentID() { return commentID; }
    public void setCommentID(String commentID) { this.commentID = commentID; }

    public String getRegisteredClassID() { return registeredClassID; }
    public void setRegisteredClassID(String registeredClassID) { this.registeredClassID = registeredClassID; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    @Override
    public String toString() {
        return commentID + " (" + registeredClassID + ")";
    }
}
