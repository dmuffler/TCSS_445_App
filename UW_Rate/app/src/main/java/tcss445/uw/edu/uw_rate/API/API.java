/*
 * TCSS445: Group Project: UW Rate
 */
package tcss445.uw.edu.uw_rate.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import tcss445.uw.edu.uw_rate.MainActivity;
import tcss445.uw.edu.uw_rate.Session;

/**
 * Module for communicating with the API server.
 *
 * @author Steven Smith
 * @version 1.0
 */
public class API {
    /** The API endpoint. */
    public static final String PATH = "http://cssgate.insttech.washington.edu/~dmuffler/445/";
    //public static final String PATH = "http://10.0.2.2/php/";
    //public static final String PATH = "http://10.0.0.12/php/";

    /**
     * Asynchronous callbacks that execute after the API call returns.
     * @param <T> the specicified return type that matches the APITask
     */
    public interface Listener<T> {

        /**
         * Executes on successful completion of the API task.
         * @param results the result of the api call
         */
        void onComplete(T results);

        /**
         * Executes on failure of the API task.
         */
        void onError();
    }

    /**
     * A generic asynchronous network call to the API given a set of parameters.
     * @param <T> the type of API resource being accessed
     */
    public static class APITask<T> extends AsyncTask<String, Void, String> {
        /** The API endpoint plus the API resource name. */
        private final String resourcePath;
        /** The list of GET parameter keys. */
        private final String[] parameterKeys;
        /** The android application context. */
        private final Context context;
        /** The API callback listener. */
        private final Listener<T> listener;
        /** The type of API resource. */
        private final Class resourceClass;

        /**
         * Creates an API task.
         * @param context the application context
         * @param listener the callback
         * @param resourceName the name of the API resource
         * @param resourceClass the class of the API resource
         * @param keys the kist of GET parameter keys
         */
        public APITask(Context context, API.Listener<T> listener, String resourceName, Class resourceClass, String... keys) {
            this.context = context;
            this.listener = listener;
            this.resourcePath = String.format("%s%s.php", API.PATH, resourceName);
            this.parameterKeys = keys;
            this.resourceClass = resourceClass;
        }

        /**
         * Gets the session id of the active session.
         * @return the session id
         */
        private String getSessionId() {
            SharedPreferences preferences = context.getSharedPreferences(MainActivity.SESSION_PREFERENCES, Context.MODE_PRIVATE);
            String json = preferences.getString(MainActivity.SESSION, "{}");
            Session session = new Gson().fromJson(json, Session.class);
            return session.getSessionId();
        }

        @Override
        protected String doInBackground(String... arguments) {
            StringBuilder args = new StringBuilder();
            for (int i = 0; i < parameterKeys.length; i++) {
                args.append('&');
                args.append(parameterKeys[i]);
                args.append('=');
                args.append(arguments[i]);
            }
            String url = String.format("%s?sid=%s%s", resourcePath, getSessionId(), args.toString());
            Log.d("url", url);
            String response = "";

            HttpURLConnection urlConnection = null;

            try {
                URL urlObject = new URL(url);
                urlConnection = (HttpURLConnection) urlObject.openConnection();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String s;
                StringBuilder sb = new StringBuilder();
                while ((s = buffer.readLine()) != null) {
                    sb.append(s);
                }
                response = sb.toString();
            } catch (Exception e) {
                response = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("APIPostExecute", result);
            try {
                T results = (T) new Gson().fromJson(result, resourceClass);
                listener.onComplete(results);
            } catch (JsonSyntaxException e) {
                listener.onError();
            }
        }
    }
}
