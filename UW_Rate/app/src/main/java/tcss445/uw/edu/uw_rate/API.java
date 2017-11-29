package tcss445.uw.edu.uw_rate;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asms on 11/29/17.
 */

public class API {
    public static final String PATH = "http://cssgate.insttech.washington.edu/~dmuffler/445/";
    //public static final String PATH = "http://10.0.2.2/php/";

    public interface Listener<T> {
        void onComplete(T results);
        void onError();
    }

    public static class APITask<T> extends AsyncTask<String, Void, String> {

        private final String resourcePath;
        private final String[] parameterKeys;
        private final Context context;
        private final Listener<T> listener;
        private final Class resourceClass;

        public APITask(Context context, API.Listener<T> listener, String resourceName, Class resourceClass, String... keys) {
            this.context = context;
            this.listener = listener;
            this.resourcePath = String.format("%s%s.php", API.PATH, resourceName);
            this.parameterKeys = keys;
            this.resourceClass = resourceClass;
        }

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
            } catch (Exception e) { } finally {
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
