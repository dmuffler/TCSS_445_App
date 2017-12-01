package tcss445.uw.edu.uw_rate;

import android.content.SharedPreferences;
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
import java.net.HttpURLConnection;
import java.net.URL;

import tcss445.uw.edu.uw_rate.API.API;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.LoginFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String FAILURE = "FAILURE";

    private AsyncTask<String, Integer, String> mTask;
    private LoginFragmentInteractionListener mListener;
    private EditText mUsername;
    private EditText mPassword;
    private RadioButton mStudentRadio;
    private RadioButton mAdminRadio;
    private String isAdminResult;

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
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = getActivity().getSharedPreferences(MainActivity.SESSION_PREFERENCES, Context.MODE_PRIVATE);
        String username = preferences.getString(getString(R.string.username), null);
        String password = preferences.getString(getString(R.string.password), null);

        if (username != null && password != null) {
            mUsername.setText(username);
            mPassword.setText(password);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(getString(R.string.username));
            editor.remove(getString(R.string.password));
            editor.commit();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createButton:
                mListener.loginFragmentInteraction("RegisterFrag");
                break;
            case R.id.loginButton:
                AsyncTask<String, Void, String> task = new LoginTask();
                task.execute(
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
        void loginFragmentInteraction(String theFragString);
        void storeSession(Session session);
    }


    /**
     * Asynch task that logs the user in if correct credentials are given.
     * Code provided by TCSS 450 and edited to fit needs.
     */
    private class LoginTask extends AsyncTask<String, Void, String> {

        private final String FILE = "login.php";

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 2) {
                throw new IllegalArgumentException("URL, email, password strings required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = API.PATH;
            String email = "?email=" + strings[0];
            String password = "&password=" + strings[1];
            String result;

            try {
                URL urlObject = new URL(url + FILE + email + password);
                Log.d("URL:", url + FILE + email + password);
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
            try {
                if (new JSONObject(response).has("error")) {
                    result = FAILURE;
                } else {
                    result = response;
                }
            } catch(JSONException e) {
                result = FAILURE;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
            if (result.equals(FAILURE)) {
                Toast.makeText(getActivity(), "Incorrect email or password", Toast.LENGTH_LONG)
                        .show();
                return;
            } else {
                LoginResult loginResult = new Gson().fromJson(result, LoginResult.class);
                isAdminResult = loginResult.is_admin;

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("is_admin", isAdminResult).commit();
                //Log.e("inLoginFrag", isAdminResult);

                Session session = new Session(loginResult.sid, loginResult.email,
                        loginResult.first_name, loginResult.last_name);
                mListener.storeSession(session);

                mListener.loginFragmentInteraction("SearchFrag");

            }
        }
    }
}
