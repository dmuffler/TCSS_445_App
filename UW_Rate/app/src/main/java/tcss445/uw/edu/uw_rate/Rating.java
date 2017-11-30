package tcss445.uw.edu.uw_rate;

public class Rating {

    private String studentId;
    private String instructorId;
    private int score;
    private int hotness;
    private String comment;
    private String authorFirstName;
    private String authorLastName;

    public Rating(String instructorId, int score, int hotness, String comment) {
        this.instructorId = instructorId;
        this.score = score;
        this.hotness = hotness;
        this.comment = comment;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public String getStudentId() {
        return studentId;
    }

    public String getInstructorId() {
        return instructorId;
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

    public void setAuthorFirstName(String firstName) {
        this.authorFirstName = firstName;
    }

    public void setAuthorLastName(String lastName) {
        this.authorLastName = lastName;
    }

    public String getAuthorFullName() {
        return String.format("%s %s", authorFirstName, authorLastName);
    }

    public static Rating fromRatingResult(RatingResult ratingResult) {
        Rating rating = new Rating(ratingResult.instructor_email, Integer.valueOf(ratingResult.score), Integer.valueOf(ratingResult.hotness), ratingResult.comment);
        rating.setAuthorFirstName(ratingResult.author_first_name);
        rating.setAuthorLastName(ratingResult.author_last_name);
        return rating;
    }

    public int getHotness() {
        return hotness;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public void setHotness(int hotness) {
        this.hotness = hotness;
    }
}
