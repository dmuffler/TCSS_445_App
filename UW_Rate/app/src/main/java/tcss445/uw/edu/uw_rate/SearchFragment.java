package tcss445.uw.edu.uw_rate;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.SearchFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private List<Professor> mProfessors;
    private SearchFragmentInteractionListener mListener;
    private ProfessorListAdapter mListAdapter;

    public SearchFragment() {
        mProfessors = new ArrayList<Professor>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mListAdapter = new ProfessorListAdapter(getContext());
        final ListView professorListView = (ListView) view.findViewById(R.id.professorListView);
        professorListView.setAdapter(mListAdapter);
        professorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.onProfessorSelected(mProfessors.get(i), "ProfessorFragment");
            }
        });

        mListAdapter.setProfessors(mProfessors);

        SearchView mSearchView = (SearchView) view.findViewById(R.id.searchField);
        mSearchView.setOnQueryTextListener(this);

        new GetInstructorsTask().execute();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragmentInteractionListener) {
            mListener = (SearchFragmentInteractionListener) context;
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
    public boolean onQueryTextSubmit(String s) { return false; }

    @Override
    public boolean onQueryTextChange(String queryText) {
        mListAdapter.getFilter().filter(queryText);
        return false;
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
    public interface SearchFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String theFragString);
        void onProfessorSelected(Professor professor, String theFragString);
    }

    private class GetInstructorsTask extends AsyncTask<String, Void, String> {

        private static final String PATH
                = "http://cssgate.insttech.washington.edu/~dmuffler/445/instructor.php";

        @Override
        protected String doInBackground(String... strings) {
            String url = String.format("%s?verb=READ", PATH);
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
            Log.d("searchposexecute", result);
            InstructorResult[]  instructorResults = new Gson().fromJson(result, InstructorResult[].class);
            mProfessors = new ArrayList<Professor>();
            for (InstructorResult instructorResult : instructorResults) {
                mProfessors.add(Professor.fromInstructorResult(instructorResult));
            }
            mListAdapter.setProfessors(mProfessors);
        }
    }
}
