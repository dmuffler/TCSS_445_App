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

class ProfessorListAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private final LayoutInflater inflater;

    public ProfessorListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.allItems = new ArrayList<Professor>();
        this.items = new ArrayList<Professor>();
    }

    List<Professor> allItems;
    List<Professor> items;

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Professor getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ProfessorViewHolder mViewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.layout_professor_list_item, viewGroup, false);
            mViewHolder = new ProfessorViewHolder(view);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ProfessorViewHolder) view.getTag();
        }

        Professor professor = getItem(i);

        mViewHolder.tvName.setText(professor.getFullName());

        return view;
    }

    private class ProfessorViewHolder {
        TextView tvName;

        public ProfessorViewHolder(View item) {
            tvName = (TextView) item.findViewById(R.id.professorName);
        }
    }

    @Override
    public Filter getFilter() {
        return new ProfessorListFilter();
    }

    public void setProfessors(List<Professor> professors) {
        this.allItems = new ArrayList<Professor>(professors);
        this.items = new ArrayList<Professor>(professors);
        notifyDataSetChanged();
    }

    public class ProfessorListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
            if(constraint == null || constraint.length() == 0){
                result.values = allItems;
                result.count = allItems.size();
            } else {
                final String query = constraint.toString().toLowerCase();
                ArrayList<Professor> filteredList = new ArrayList<Professor>();
                for(Professor prof: allItems){
                    boolean matches = prof.getFullName().toLowerCase().contains(query);
                    int partialRatio = FuzzySearch.ratio(prof.getFullName().toLowerCase(), query);
                    if(matches || partialRatio > 50) { filteredList.add(prof); }
                }

                Collections.sort(filteredList, new Comparator<Professor>(){
                    public int compare(Professor obj1, Professor obj2) {
                        int partialRatio1 = FuzzySearch.ratio(obj1.getFullName().toLowerCase(), query);
                        int partialRatio2 = FuzzySearch.ratio(obj2.getFullName().toLowerCase(), query);
                        return Integer.compare(partialRatio1, partialRatio2);
                    }
                });

                result.values = filteredList;
                result.count = filteredList.size();
            }

            return result;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            items = (ArrayList<Professor>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
