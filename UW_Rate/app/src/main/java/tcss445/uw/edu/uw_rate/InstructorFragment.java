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

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstructorFragment extends Fragment {

    private InstructorChangedListener mInstructorChangedListener;
    private InstructorFragmentInteractionListener mListener;
    private Instructor mInstructor;
    private Rating rating;
    private List<Rating> mRatings;
    private RatingListAdapter mRatingListAdapter;


    public InstructorFragment() {
        mRatings = new ArrayList<Rating>();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.instructor_layout, container, false);
        Instructor instructor = (Instructor) getArguments().getParcelable(Instructor.class.getName());

        mRatingListAdapter = new RatingListAdapter(getContext());

        final TextView instructorNameLabel = (TextView) view.findViewById(R.id.instructorName);
        final TextView positionTitleLabel = (TextView) view.findViewById(R.id.positionTitle);
        final TextView departmentNameLabel = (TextView) view.findViewById(R.id.departmentName);

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

        mInstructorChangedListener = new InstructorChangedListener() {
            @Override
            public void onInstructorChanged(Instructor instructor) {
                if (instructor == null) {
                    mInstructor = instructor;
                }
                if (mInstructor != null && instructorNameLabel != null) {
                    instructorNameLabel.setText(mInstructor.getFullName());
                    positionTitleLabel.setText(mInstructor.getPositionTitle());
                    departmentNameLabel.setText(mInstructor.getDepartmentName());
                    new FetchRatingsTask(mListener.getSessionId(), mInstructor).execute();
                }
            }
        };

        mInstructorChangedListener.onInstructorChanged(instructor);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InstructorFragmentInteractionListener) {
            mListener = (InstructorFragmentInteractionListener) context;
            Instructor instructor = (Instructor) getArguments().getParcelable(Instructor.class.getName());
            if (mInstructorChangedListener != null) {
                mInstructorChangedListener.onInstructorChanged(instructor);
            } else if(instructor != null) {
                mInstructor = instructor;
            }
            rating = new Rating(mInstructor.getEmail(), 0, null);
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

    public interface InstructorFragmentInteractionListener {
        void instructorFragmentInteraction(String theFragString);
        void onRatingChanged(Rating rating);
        String getSessionId();
    }

    public interface InstructorChangedListener {
        void onInstructorChanged(Instructor instructor);
    }

    public class FetchRatingsTask extends AsyncTask<String, Void, String> {
        private String sessionId;
        private Instructor instructor;

        public FetchRatingsTask(String sessionId, Instructor instructor) {
            this.sessionId = sessionId;
            this.instructor = instructor;
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = String.format("%srating.php?verb=READ_ALL&sid=%s&instructor_email=%s", API.PATH, sessionId, instructor.getEmail());
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
            Log.d("rating results", result);
            RatingResult[] ratingResults = new Gson().fromJson(result, RatingResult[].class);
            mRatings = new ArrayList<Rating>();
            for (RatingResult ratingResult : ratingResults) {
                mRatings.add(Rating.fromRatingResult(ratingResult));
            }
            mRatingListAdapter.setRatings(mRatings);
        }
    }
}
