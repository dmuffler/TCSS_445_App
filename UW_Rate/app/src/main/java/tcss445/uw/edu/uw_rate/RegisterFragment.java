package tcss445.uw.edu.uw_rate;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tcss445.uw.edu.uw_rate.API.API;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.RegisterFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static final String FAILURE = "FAILURE";
    private RegisterFragmentInteractionListener mListener;
    private EditText mUsername;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPassword;
    private RadioButton mStudentRadio;
    private RadioButton mAdminRadio;
    private int mControl;

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
        mUsername = (EditText) v.findViewById(R.id.emailField);
        mPassword = (EditText) v.findViewById(R.id.passwordRegField);
        mStudentRadio = (RadioButton) v.findViewById(R.id.studentRadioRegisterButton);
        mAdminRadio = (RadioButton) v.findViewById(R.id.adminRadioRegisterButton);

        Button register = (Button) v.findViewById(R.id.registerButton);
        register.setOnClickListener(this);
        
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
        if (view.getId() == R.id.registerButton) {
            if (mStudentRadio.isChecked()) {
                mControl = 0;
            } else {
                mControl = 1;
            }

            if (validateInput()) {
                AsyncTask<String, Void, String> task = new LoginTask();
                task.execute(
                        String.valueOf(mControl),
                        mUsername.getText().toString(),
                        mPassword.getText().toString(),
                        mFirstName.getText().toString(),
                        mLastName.getText().toString());
            }
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

    /**
     * Asynch task that logs the user in if correct credentials are given.
     * Code provided by TCSS 450 and edited to fit needs.
     */
    private class LoginTask extends AsyncTask<String, Void, String> {

        private final String FILE = "register.php";

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 5) {
                throw new IllegalArgumentException("URL, email, password strings required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = API.PATH;
            String control = "?my_control=" + strings[0];
            String email = "&my_email=" + strings[1];
            String password = "&my_pass=" + strings[2];
            String first_name = "&my_first_name=" + strings[3];
            String last_name = "&my_last_name=" + strings[4];

            try {
                URL urlObject = new URL(url + FILE + control + email + password + first_name +
                        last_name);
                urlConnection = (HttpURLConnection) urlObject.openConnection();

                InputStream content = urlConnection.getInputStream();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }

            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            Log.d("RESPONSE:", response);
            if (response.equals("false")) {
                Toast.makeText(getActivity(), "A user already exists with that" +
                        " email.", Toast.LENGTH_LONG)
                        .show();
                return;
            } else if (response.equals("true")){

                mListener.registerFragmentInteraction("LoginFrag");
            }
        }
    }

    private boolean validateInput() {
        boolean validEmail = false;
        boolean validPass = false;
        boolean validFirst = false;
        boolean validLast = false;

        String email = mUsername.getText().toString();
        // Regex inspired by https://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
        // Expanded upon to fit our needs.
        if (checkString(email, "([a-zA-Z0-9!#$%&\'*+-/=?^_`{|}~])+(" +
                "\\.[a-zA-Z0-9!#$%&\'*+-/=?^_`{|}~]+)*@([a-zA-Z]{2,2})(\\.[a-zA-Z]{3,3})$")) {
            validEmail = true;
        } else {
            mUsername.setError("A valid uw email must be entered");
        }

        String password = mPassword.getText().toString();
        if (checkString(password, "[a-z]")
                && checkString(password, "[A-Z]")
                && checkLength(password, 8)
                && checkString(password, "[^a-zA-Z0-9]$")) {
            validPass = true;
        } else {
            mPassword.setError("Password must contain an uppercase, lowercase, special character, " +
                    "and must be at least 8 characters in length");
        }

        String firstName = mFirstName.getText().toString();
        if (checkString(firstName, "([a-zA-Z])$"))  {
            validFirst = true;
        } else {
            mFirstName.setError("Please enter a valid first name");
        }

        String lastName = mLastName.getText().toString();
        if (checkString(lastName, "([a-zA-Z])$")) {
            validLast = true;
        } else {
            mLastName.setError("Please enter a valid last name");
        }

        if (validEmail && validPass && validFirst && validLast) {
            return true;
        }else {
            return false;
        }
    }

    private boolean checkString(String theString, String thePattern) {
        Pattern casePattern = Pattern.compile(thePattern);
        Matcher matcher = casePattern.matcher(theString);
        return matcher.find();
    }

    private boolean checkLength(String theString, int theLength) {
        return theString.length() >= theLength;
    }
}
