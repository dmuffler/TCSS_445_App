package tcss445.uw.edu.uw_rate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.RegisterFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private RegisterFragmentInteractionListener mListener;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mGender;
    private EditText mEmail;
    private EditText mPassword;
    private RadioButton mStudentRadio;
    private RadioButton mAdminRadio;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        mFirstName = (EditText) v.findViewById(R.id.firstNameField);
        mLastName = (EditText) v.findViewById(R.id.lastNameField);
        mGender = (EditText) v.findViewById(R.id.genderField);
        mEmail = (EditText) v.findViewById(R.id.emailField);
        mPassword = (EditText) v.findViewById(R.id.passwordRegField);
        mStudentRadio = (RadioButton) v.findViewById(R.id.studentRadioRegisterButton);
        mAdminRadio = (RadioButton) v.findViewById(R.id.adminRadioRegisterButton);

        int booKey = getArguments().getInt(getString(R.string.boo_key));

        if (booKey == 0) {
            mStudentRadio.setChecked(true);
        } else {
            mAdminRadio.setChecked(true);
        }
        
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterFragmentInteractionListener) {
            mListener = (RegisterFragmentInteractionListener) context;
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.createButton) {

        }
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
    public interface RegisterFragmentInteractionListener {
        // TODO: Update argument type and name
        void registerFragmentInteraction(String theFragString);
    }
}
