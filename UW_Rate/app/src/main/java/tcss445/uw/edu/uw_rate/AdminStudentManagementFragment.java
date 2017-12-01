package tcss445.uw.edu.uw_rate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tcss445.uw.edu.uw_rate.API.API;
import tcss445.uw.edu.uw_rate.API.SearchStudentTask;

public class AdminStudentManagementFragment extends Fragment implements SearchView.OnQueryTextListener {

    private AdminStudentManagementFragmentInteractionListener mListener;
    private API.Listener<StudentResult[]> searchStudentsResultListener;

    public AdminStudentManagementFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_admin_student_management, container, false);
        final StudentListAdapter studentListAdapter = new StudentListAdapter(getContext());
        final ListView studentList = (ListView) view.findViewById(R.id.studentListView);
        studentList.setAdapter(studentListAdapter);

        studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Student selectedStudent = studentListAdapter.getItem(i);
            }
        });

        searchStudentsResultListener = new API.Listener<StudentResult[]>() {
            @Override
            public void onComplete(StudentResult[] results) {
                Student[] students = Student.fromResultArray(results);
                studentListAdapter.setStudents(Arrays.asList(students));
            }

            @Override
            public void onError() {}
        };

        SearchView mSearchView = (SearchView) view.findViewById(R.id.searchField);
        mSearchView.setOnQueryTextListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AdminStudentManagementFragmentInteractionListener) {
            mListener = (AdminStudentManagementFragmentInteractionListener) context;
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
    public boolean onQueryTextSubmit(String s) {
        new SearchStudentTask(getContext(), searchStudentsResultListener).search(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public interface AdminStudentManagementFragmentInteractionListener {}
}
