package tcss445.uw.edu.uw_rate;

import android.content.Context;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.SearchFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SearchFragmentInteractionListener mListener;
    private ProfessorListAdapter mListAdapter;

    public SearchFragment() {
        // Required empty public constructor
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
                mListener.onProfessorSelected(new Professor("Nametest"), "ProfessorFragment");
            }
        });

        List<Professor> professors = new ArrayList<Professor>();
        Professor testProfessor = new Professor("Test Professor");
        Professor professor = new Professor("Prof. Smith");
        professors.add(testProfessor);
        professors.add(professor);
        mListAdapter.setProfessors(professors);

        SearchView mSearchView = (SearchView) view.findViewById(R.id.searchField);
        mSearchView.setOnQueryTextListener(this);
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
}
