package tcss445.uw.edu.uw_rate;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfessorFragment extends Fragment implements View.OnClickListener {

    private ProfessorFragmentInteractionListener mListener;
    //private Professor myProfessor;
    private Professor professor;
    private TextView professorNameLabel;


    public ProfessorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_professor, container, false);

        professorNameLabel = (TextView) view.findViewById(R.id.professorName);
        professor = getArguments().getParcelable(Professor.class.getName());
        if (professorNameLabel != null) {
            professorNameLabel.setText(professor.getFullName());
        }

        //return inflater.inflate(R.layout.fragment_professor, container, false);
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfessorFragmentInteractionListener) {
            mListener = (ProfessorFragmentInteractionListener) context;
            professor = getArguments().getParcelable(Professor.class.getName());
            if (professorNameLabel != null) {
                professorNameLabel.setText(professor.getFullName());
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

    public interface ProfessorFragmentInteractionListener {
        void professorFragmentInteraction(String theFragString);
    }
}
