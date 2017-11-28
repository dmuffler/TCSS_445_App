package tcss445.uw.edu.uw_rate;

public class Rating {

    private String studentId;
    private String professorId;
    private int score;
    private String comment;

    public Rating(String professorId, int score, String comment) {
        this.professorId = professorId;
        this.score = score;
        this.comment = comment;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public String getStudentId() {
        return studentId;
    }

    public String getProfessorId() {
        return professorId;
    }

    public int getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static Rating fromRatingResult(RatingResult ratingResult) {
        return new Rating(ratingResult.instructor_id, Integer.valueOf(ratingResult.score), ratingResult.comment);
    }
}
