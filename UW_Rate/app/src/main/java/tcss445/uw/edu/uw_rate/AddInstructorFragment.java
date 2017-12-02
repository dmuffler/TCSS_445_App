package tcss445.uw.edu.uw_rate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddInstructorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddInstructorFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public AddInstructorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_instructor, container, false);
        final EditText etFirstName = (EditText) view.findViewById(R.id.instructorFirstName);
        final EditText etLastName = (EditText) view.findViewById(R.id.instructorLastName);
        final EditText etEmail = (EditText) view.findViewById(R.id.instructorEmail);
        final EditText etPhoneNumber = (EditText) view.findViewById(R.id.instructorPhoneNumber);
        final EditText etPositionTitle = (EditText) view.findViewById(R.id.instructorPositionTitle);
        final EditText etDepartmentName = (EditText) view.findViewById(R.id.instructorDepartmentName);
        Button bAddInstructor = (Button) view.findViewById(R.id.addInstructorButton);

        bAddInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String phoneNumber = etPhoneNumber.getText().toString();
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String positionTitle = etPositionTitle.getText().toString();
                String departmentName = etDepartmentName.getText().toString();
                Instructor instructor = new Instructor(email, phoneNumber,
                        firstName, lastName,
                        positionTitle, departmentName);
                mListener.onCreateInstructor(instructor);
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onCreateInstructor(Instructor instructor);
    }
}
