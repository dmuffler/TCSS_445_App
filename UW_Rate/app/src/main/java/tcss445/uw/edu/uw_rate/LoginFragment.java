package tcss445.uw.edu.uw_rate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.LoginFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private LoginFragmentInteractionListener mListener;
    private EditText mUsername;
    private EditText mPassword;
    private RadioButton mStudentRadio;
    private RadioButton mAdminRadio;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mUsername = (EditText) v.findViewById(R.id.usernameField);
        mPassword = (EditText) v.findViewById(R.id.passwordLogField);
        mStudentRadio = (RadioButton) v.findViewById(R.id.studentRadioLoginButton);
        mAdminRadio = (RadioButton) v.findViewById(R.id.adminRadioLoginButton);

        mStudentRadio.setChecked(true);

        Button loginButton = (Button) v.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        Button registerButton = (Button) v.findViewById(R.id.createButton);
        registerButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragmentInteractionListener) {
            mListener = (LoginFragmentInteractionListener) context;
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
        int radioCheck;
        if (mStudentRadio.isChecked()) {
            radioCheck = 0;
        } else {
            radioCheck = 1;
        }
        switch (view.getId()) {
            case R.id.createButton:
                mListener.loginFragmentInteraction("RegisterFrag", radioCheck);
                break;
            case R.id.loginButton:
                mListener.loginFragmentInteraction("SearchFrag", radioCheck);
                break;
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
    public interface LoginFragmentInteractionListener {
        void loginFragmentInteraction(String theFragString, int theRadioCheck);
    }
}
