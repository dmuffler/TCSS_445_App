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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfessorFragment extends Fragment {

    private ProfessorFragmentInteractionListener mListener;
    private Professor professor;
    private Rating rating;
    private TextView professorNameLabel;
    private List<Rating> mRatings;
    private RatingListAdapter mRatingListAdapter;


    public ProfessorFragment() {
        mRatings = new ArrayList<Rating>();
    }

    private void setProfessor(Professor professor) {
        if (!professor.equals(this.professor)) {
            this.professor = professor;
            onProfessorChanged();
        }
    }

    private void onProfessorChanged() {
        if (professorNameLabel != null) {
            professorNameLabel.setText(professor.getFullName());
        }
        new FetchRatingsTask(mListener.getSessionId(), professor).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.instructor_layout, container, false);

        mRatingListAdapter = new RatingListAdapter(getContext());

        professorNameLabel = (TextView) view.findViewById(R.id.instructorName);
        setProfessor((Professor) getArguments().getParcelable(Professor.class.getName()));

        final EditText editTextReview = (EditText) view.findViewById(R.id.editTextReview);
        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.editRating);
        Button submitReview = (Button) view.findViewById(R.id.submitRatingButton);
        View.OnClickListener ratingChangedListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ratingScore = Math.round(ratingBar.getRating());
                String reviewText = editTextReview.getText().toString();
                rating.setScore(ratingScore);
                rating.setComment(reviewText);
                mListener.onRatingChanged(rating);
            }
        };
        ratingBar.setOnClickListener(ratingChangedListener);
        submitReview.setOnClickListener(ratingChangedListener);

        ListView ratingsListView = (ListView) view.findViewById(R.id.instructorReviewList);
        ratingsListView.setAdapter(mRatingListAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfessorFragmentInteractionListener) {
            mListener = (ProfessorFragmentInteractionListener) context;
            setProfessor((Professor) getArguments().getParcelable(Professor.class.getName()));
            rating = new Rating(professor.getId(), 0, null);
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

    public interface ProfessorFragmentInteractionListener {
        void professorFragmentInteraction(String theFragString);
        void onRatingChanged(Rating rating);
        String getSessionId();
    }

    public class FetchRatingsTask extends AsyncTask<String, Void, String> {

        private static final String PATH
                = "http://cssgate.insttech.washington.edu/~dmuffler/445/rating.php";

        private String sessionId;
        private Professor professor;

        public FetchRatingsTask(String sessionId, Professor professor) {
            this.sessionId = sessionId;
            this.professor = professor;
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = String.format("%s?verb=READ_ALL&sid=%s&instructor_id=%s", PATH, sessionId, professor.getId());
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
            RatingResult[] ratingResults = new Gson().fromJson(result, RatingResult[].class);
            mRatings = new ArrayList<Rating>();
            for (RatingResult ratingResult : ratingResults) {
                mRatings.add(Rating.fromRatingResult(ratingResult));
            }
            mRatingListAdapter.setRatings(mRatings);
        }
    }
}
