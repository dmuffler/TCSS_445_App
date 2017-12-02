package tcss445.uw.edu.uw_rate;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tcss445.uw.edu.uw_rate.API.API;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstructorFragmentAdmin extends Fragment {

    private InstructorChangedListener mInstructorChangedListener;
    private InstructorFragmentAdminInteractionListener mListener;
    private Instructor mInstructor;
    private Rating rating;
    private List<Rating> mRatings;
    private RatingListAdapter mRatingListAdapter;
    //private RatingsChangedListener mRatingsChangedListener;


    public InstructorFragmentAdmin() {
        mRatings = new ArrayList<Rating>();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.instructor_admin_fragment, container, false);
        Instructor instructor = (Instructor) getArguments().getParcelable(Instructor.class.getName());

        mRatingListAdapter = new RatingListAdapter(getContext());

        final TextView instructorNameLabel = (TextView) view.findViewById(R.id.instructorName);
        final TextView positionTitleLabel = (TextView) view.findViewById(R.id.positionTitle);
        final TextView departmentNameLabel = (TextView) view.findViewById(R.id.departmentName);

        Button deleteInstructorButton = (Button) view.findViewById(R.id.deleteInstructorButton);
        deleteInstructorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onInstructorDelete(mInstructor.getEmail());
            }
        });

        final API.Listener<RatingResult[]> ratingChangedCallback = new API.Listener<RatingResult[]>() {
            @Override
            public void onComplete(RatingResult[] results) {
                Log.d("::onComplete", "ratingChanged");
                new FetchRatingsTask(getSession().getSessionId(), mInstructor).execute();
            }

            @Override
            public void onError() {
                Log.d("::onError", "rating changed");
            }
        };

        ListView ratingsListView = (ListView) view.findViewById(R.id.instructorReviewList);
        ratingsListView.setAdapter(mRatingListAdapter);

        mInstructorChangedListener = new InstructorChangedListener() {
            @Override
            public void onInstructorChanged(Instructor instructor) {
                if (instructor != null) {
                    mInstructor = instructor;
                }
                if (mInstructor != null && instructorNameLabel != null) {
                    Session session = getSession();
                    instructorNameLabel.setText(mInstructor.getFullName());
                    positionTitleLabel.setText(mInstructor.getPositionTitle());
                    departmentNameLabel.setText(mInstructor.getDepartmentName());
                    new FetchRatingsTask(session.getSessionId(), mInstructor).execute();
                }
            }
        };

        final ProgressBar ratingProgress1 = (ProgressBar) view.findViewById(R.id.ratingProgress1);
        final ProgressBar ratingProgress2 = (ProgressBar) view.findViewById(R.id.ratingProgress2);
        final ProgressBar ratingProgress3 = (ProgressBar) view.findViewById(R.id.ratingProgress3);
        final ProgressBar ratingProgress4 = (ProgressBar) view.findViewById(R.id.ratingProgress4);
        final ProgressBar ratingProgress5 = (ProgressBar) view.findViewById(R.id.ratingProgress5);

        final TextView numRating1 = (TextView) view.findViewById(R.id.numRating1);
        final TextView numRating2 = (TextView) view.findViewById(R.id.numRating2);
        final TextView numRating3 = (TextView) view.findViewById(R.id.numRating3);
        final TextView numRating4 = (TextView) view.findViewById(R.id.numRating4);
        final TextView numRating5 = (TextView) view.findViewById(R.id.numRating5);

        mInstructorChangedListener.onInstructorChanged(instructor);

        return view;
    }

    public Session getSession() {
        SharedPreferences preferences = getActivity().getSharedPreferences(MainActivity.SESSION_PREFERENCES, Context.MODE_PRIVATE);
        String json = preferences.getString(MainActivity.SESSION, "{}");
        Session session = new Gson().fromJson(json, Session.class);
        return session;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InstructorFragmentAdminInteractionListener) {
            mListener = (InstructorFragmentAdminInteractionListener) context;
            Instructor instructor = (Instructor) getArguments().getParcelable(Instructor.class.getName());
            if (mInstructorChangedListener != null) {
                mInstructorChangedListener.onInstructorChanged(instructor);
            } else if(instructor != null) {
                mInstructor = instructor;
            }
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

    public interface InstructorFragmentAdminInteractionListener {
        void onInstructorDelete(String instructorEmail);
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
            List<Rating> ratings = new ArrayList<Rating>();
            for (RatingResult ratingResult : ratingResults) {
                ratings.add(Rating.fromRatingResult(ratingResult));
            }
            //mRatingsChangedListener.onRatingsChanged(ratings);
        }
    }
}
