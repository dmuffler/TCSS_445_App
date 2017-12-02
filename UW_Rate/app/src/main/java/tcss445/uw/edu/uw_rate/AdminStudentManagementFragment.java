package tcss445.uw.edu.uw_rate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tcss445.uw.edu.uw_rate.API.API;
import tcss445.uw.edu.uw_rate.API.DeleteStudentTask;
import tcss445.uw.edu.uw_rate.API.SearchStudentTask;

public class AdminStudentManagementFragment extends Fragment implements SearchView.OnQueryTextListener {

    private AdminStudentManagementFragmentInteractionListener mListener;
    private API.Listener<StudentResult[]> searchStudentsResultListener;
    private StudentListAdapter studentListAdapter;

    public AdminStudentManagementFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_admin_student_management, container, false);
        studentListAdapter = new StudentListAdapter(getContext());
        final ListView studentList = (ListView) view.findViewById(R.id.studentListView);
        studentList.setAdapter(studentListAdapter);
        registerForContextMenu(studentList);

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
    public boolean onQueryTextSubmit(String query) {
        new SearchStudentTask(getContext(), searchStudentsResultListener).search(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        studentListAdapter.getFilter().filter(query);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.studentListView) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Student student = (Student) lv.getItemAtPosition(acmi.position);
            menu.add(0, 0, 0, "Delete student");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case 0:
                new DeleteStudentTask(getContext(), new API.Listener<StudentResult[]>() {
                    @Override
                    public void onComplete(StudentResult[] results) {
                        Toast.makeText(getContext(), "Student deleted.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {

                    }
                }).delete(studentListAdapter.getItem(info.position).getEmail());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public interface AdminStudentManagementFragmentInteractionListener {}
}
