package tcss445.uw.edu.uw_rate.API;

import android.content.Context;

import tcss445.uw.edu.uw_rate.Instructor;
import tcss445.uw.edu.uw_rate.InstructorResult;

public class CreateInstructorTask extends API.APITask<InstructorResult[]> {

    private static final String RESOURCE_NAME = "instructor";
    private static final Class RESOURCE_CLASS = InstructorResult[].class;


    public CreateInstructorTask(Context context, API.Listener listener) {
        super(context, listener, RESOURCE_NAME, RESOURCE_CLASS,
                "verb",
                "first_name", "last_name",
                "position_title", "department_name",
                "phone_number", "email");
    }

    public void create(Instructor instructor) {
        super.execute("CREATE",
                instructor.getFirstName(), instructor.getLastName(),
                instructor.getPositionTitle(), instructor.getDepartmentName(),
                instructor.getPhoneNumber(), instructor.getEmail());
    }
}
