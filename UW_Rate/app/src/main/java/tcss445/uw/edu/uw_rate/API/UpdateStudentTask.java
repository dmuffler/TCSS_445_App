package tcss445.uw.edu.uw_rate.API;

import android.content.Context;
import tcss445.uw.edu.uw_rate.InstructorResult;
import tcss445.uw.edu.uw_rate.StudentResult;

public class UpdateStudentTask extends API.APITask<StudentResult[]> {

    private static final String RESOURCE_NAME = "student";
    private static final Class RESOURCE_CLASS = StudentResult[].class;


    public UpdateStudentTask(Context context, API.Listener listener) {
        super(context, listener, RESOURCE_NAME, RESOURCE_CLASS,
                "verb", "email", "password", "first_name", "last_name");
    }

    public void update(String email, String password, String firstName, String lastName) {
        super.execute("UPDATE", email, password, firstName, lastName);
    }
}
