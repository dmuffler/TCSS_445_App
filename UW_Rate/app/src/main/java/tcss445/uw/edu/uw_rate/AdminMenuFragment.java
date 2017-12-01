package tcss445.uw.edu.uw_rate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class AdminMenuFragment extends Fragment {
    private AdminMenuFragmentInteractionListener mListener;

    public AdminMenuFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_menu, container, false);
        Button manageStudentsButton = (Button) view.findViewById(R.id.manageStudents);
        Button manageInstructorsButton = (Button) view.findViewById(R.id.manageInstructors);
        Button manageRatingsButton = (Button) view.findViewById(R.id.manageRatings);

        manageStudentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAdminMenuItemSelected(AdminStudentManagementFragment.class);
            }
        });
        manageInstructorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAdminMenuItemSelected(SearchFragmentAdmin.class);
            }
        });
        manageRatingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: mListener.onAdminMenuItemSelected(...);
                Toast.makeText(getContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AdminMenuFragmentInteractionListener) {
            mListener = (AdminMenuFragmentInteractionListener) context;
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
    public interface AdminMenuFragmentInteractionListener {
        void onAdminMenuItemSelected(Class<? extends Fragment> selectedFragment);
    }
}
