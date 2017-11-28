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

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.RegisterFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/~dmuffler/445/";
    private static final String FAILURE = "FAILURE";
    private RegisterFragmentInteractionListener mListener;
    private EditText mUsername;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mGender;
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
        mGender = (EditText) v.findViewById(R.id.genderField);
        mUsername = (EditText) v.findViewById(R.id.emailField);
        mPassword = (EditText) v.findViewById(R.id.passwordRegField);
        mStudentRadio = (RadioButton) v.findViewById(R.id.studentRadioRegisterButton);
        mAdminRadio = (RadioButton) v.findViewById(R.id.adminRadioRegisterButton);

/*        int booKey = getArguments().getInt(getString(R.string.boo_key));

        if (booKey == 0) {
            mStudentRadio.setChecked(true);
        } else {
            mAdminRadio.setChecked(true);
        }*/

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
            // TODO: implement password hashing client side. Most of it is done below.
            /*try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.update(mPassword.getText().toString().getBytes());
                String pass = new String(md.digest());*/
                AsyncTask<String, Void, String> task = new LoginTask();
                task.execute(PARTIAL_URL,
                        String.valueOf(mControl),
                        mUsername.getText().toString(),
                        mPassword.getText().toString(),
                        mFirstName.getText().toString(),
                        mLastName.getText().toString(),
                        mGender.getText().toString());
/*            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }*/
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
            if (strings.length != 7) {
                throw new IllegalArgumentException("URL, email, password strings required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            String control = "?my_control=" + strings[1];
            String email = "&my_email=" + strings[2];
            String password = "&my_pass=" + strings[3];
            String first_name = "&my_first_name=" + strings[4];
            String last_name = "&my_last_name=" + strings[5];
            String gender = "&my_gender=" + strings[6];

            try {
                URL urlObject = new URL(url + FILE + control + email + password + first_name +
                        last_name + gender);
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
            if (response.equals("fasle")) {
                Toast.makeText(getActivity(), "A user already exists with that" +
                        " email.", Toast.LENGTH_LONG)
                        .show();
                return;
            } else if (response.equals("true")){
                // TODO: apply sessions
                mListener.registerFragmentInteraction("SearchFrag");
            }
        }
    }
}
