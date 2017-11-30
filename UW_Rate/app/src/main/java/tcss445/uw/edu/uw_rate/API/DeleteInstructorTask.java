package tcss445.uw.edu.uw_rate.API;

import android.content.Context;
import tcss445.uw.edu.uw_rate.InstructorResult;

public class DeleteInstructorTask extends API.APITask<InstructorResult[]> {

    private static final String RESOURCE_NAME = "instructor";
    private static final Class RESOURCE_CLASS = InstructorResult[].class;


    public DeleteInstructorTask(Context context, API.Listener listener) {
        super(context, listener, RESOURCE_NAME, RESOURCE_CLASS, "verb", "email");
    }

    public void delete(String instructorEmail) {
        super.execute("DELETE", instructorEmail);
    }
}
