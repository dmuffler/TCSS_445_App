package tcss445.uw.edu.uw_rate;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.LoginFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/~dmuffler/445/";
    private AsyncTask<String, Integer, String> mTask;
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
                AsyncTask<String, Void, String> task = new LoginTask();
                task.execute(PARTIAL_URL,
                        "1",
                        mUsername.getText().toString(),
                        mPassword.getText().toString());
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


    /**
     * Asynch task that logs the user in if correct credentials are given.
     * Code provided by TCSS 450 and edited to fit needs.
     */
    private class LoginTask extends AsyncTask<String, Void, String> {

        private final String FILE = "login.php";

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 4) {
                throw new IllegalArgumentException("Two String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            String control = "?my_control=" + strings[1];
            String email = "&my_email=" + strings[2];
            String password = "&my_pass=" + strings[3];

            try {
                URL urlObject = new URL(url + FILE + control + email + password);
                Log.d("URL:", url + FILE + control + email + password);
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
            Log.d("Pass?:", response);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.equals("false")) {
                Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_LONG)
                        .show();
                return;
            } else if(result.equals("true")){
                mListener.loginFragmentInteraction("SearchFrag", 0);
            }
        }
    }
}
