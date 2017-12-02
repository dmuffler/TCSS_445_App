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

class InstructorListAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private final LayoutInflater inflater;

    public InstructorListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.allItems = new ArrayList<Instructor>();
        this.items = new ArrayList<Instructor>();
    }

    List<Instructor> allItems;
    List<Instructor> items;

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Instructor getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        InstructorViewHolder mViewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.layout_instructor_list_item, viewGroup, false);
            mViewHolder = new InstructorViewHolder(view);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (InstructorViewHolder) view.getTag();
        }

        Instructor instructor = getItem(i);

        mViewHolder.tvFullName.setText(instructor.getFullName());
        mViewHolder.tvPositionTitle.setText(instructor.getPositionTitle());
        mViewHolder.tvDepartmentName.setText(instructor.getDepartmentName());

        return view;
    }

    private class InstructorViewHolder {
        TextView tvFullName;
        TextView tvPositionTitle;
        TextView tvDepartmentName;


        public InstructorViewHolder(View item) {
            tvFullName = (TextView) item.findViewById(R.id.instructorName);
            tvPositionTitle = (TextView) item.findViewById(R.id.instructorPositionTitle);
            tvDepartmentName = (TextView) item.findViewById(R.id.instructorDepartmentName);
        }
    }

    @Override
    public Filter getFilter() {
        return new InstructorListFilter();
    }

    public void setInstructors(List<Instructor> instructors) {
        this.allItems = new ArrayList<Instructor>(instructors);
        this.items = new ArrayList<Instructor>(instructors);
        notifyDataSetChanged();
    }

    public class InstructorListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
            if(constraint == null || constraint.length() == 0){
                result.values = allItems;
                result.count = allItems.size();
            } else {
                final String query = constraint.toString().toLowerCase();
                ArrayList<Instructor> filteredList = new ArrayList<Instructor>();
                for(Instructor instructor: allItems){
                    boolean matches = instructor.getFullName().toLowerCase().contains(query);
                    int partialRatio = FuzzySearch.partialRatio(instructor.getFullName().toLowerCase(), query);
                    if(matches || partialRatio > 50) { filteredList.add(instructor); }
                }

                Collections.sort(filteredList, new Comparator<Instructor>(){
                    public int compare(Instructor obj1, Instructor obj2) {
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
            items = (ArrayList<Instructor>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
