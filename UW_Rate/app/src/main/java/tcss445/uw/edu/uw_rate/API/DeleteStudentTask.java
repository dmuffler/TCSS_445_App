package tcss445.uw.edu.uw_rate.API;

import android.content.Context;
import tcss445.uw.edu.uw_rate.InstructorResult;
import tcss445.uw.edu.uw_rate.StudentResult;

public class DeleteStudentTask extends API.APITask<StudentResult[]> {

    private static final String RESOURCE_NAME = "student";
    private static final Class RESOURCE_CLASS = StudentResult[].class;


    public DeleteStudentTask(Context context, API.Listener listener) {
        super(context, listener, RESOURCE_NAME, RESOURCE_CLASS, "verb", "email");
    }

    public void delete(String studentEmail) {
        super.execute("DELETE", studentEmail);
    }
}
