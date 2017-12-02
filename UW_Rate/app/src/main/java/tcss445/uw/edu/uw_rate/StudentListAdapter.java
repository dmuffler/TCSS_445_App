package tcss445.uw.edu.uw_rate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.xdrop.fuzzywuzzy.FuzzySearch;

class StudentListAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private final LayoutInflater inflater;

    public StudentListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.allItems = new ArrayList<Student>();
        this.items = new ArrayList<Student>();
    }

    List<Student> allItems;
    List<Student> items;

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Student getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        StudentViewHolder mViewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.layout_student_list_item, viewGroup, false);
            mViewHolder = new StudentViewHolder(view);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (StudentViewHolder) view.getTag();
        }

        Student student = getItem(i);

        mViewHolder.tvFullName.setText(student.getFullName());
        mViewHolder.tvEmail.setText(student.getEmail());

        return view;
    }

    private class StudentViewHolder {
        TextView tvFullName;
        TextView tvEmail;

        public StudentViewHolder(View item) {
            tvFullName = (TextView) item.findViewById(R.id.studentFullName);
            tvEmail = (TextView) item.findViewById(R.id.studentEmail);
        }
    }

    @Override
    public Filter getFilter() {
        return new StudentListFilter();
    }

    public void setStudents(List<Student> students) {
        this.allItems = new ArrayList<Student>(students);
        this.items = new ArrayList<Student>(students);
        notifyDataSetChanged();
    }

    public class StudentListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
            if(constraint == null || constraint.length() == 0){
                result.values = allItems;
                result.count = allItems.size();
            } else {
                final String query = constraint.toString().toLowerCase();
                ArrayList<Student> filteredList = new ArrayList<Student>();
                for(Student student: allItems){
                    boolean matches = student.getFullName().toLowerCase().contains(query);
                    int partialRatio = FuzzySearch.partialRatio(student.getFullName().toLowerCase(), query);
                    if(matches || partialRatio > 50) { filteredList.add(student); }
                }

                Collections.sort(filteredList, new Comparator<Student>(){
                    public int compare(Student obj1, Student obj2) {
                        int partialRatio1 = FuzzySearch.partialRatio(obj1.getFullName().toLowerCase(), query);
                        int partialRatio2 = FuzzySearch.partialRatio(obj2.getFullName().toLowerCase(), query);
                        return Integer.compare(partialRatio2, partialRatio1);
                    }
                });

                result.values = filteredList;
                result.count = filteredList.size();
            }

            return result;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            items = (ArrayList<Student>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
