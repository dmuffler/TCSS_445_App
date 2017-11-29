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

class RatingListAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater inflater;

    public RatingListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.allItems = new ArrayList<Rating>();
        this.items = new ArrayList<Rating>();
    }

    List<Rating> allItems;
    List<Rating> items;

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Rating getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RatingViewHolder mViewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.layout_rating_list_item, viewGroup, false);
            mViewHolder = new RatingViewHolder(view);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (RatingViewHolder) view.getTag();
        }

        Rating rating = getItem(i);

        mViewHolder.tvName.setText(rating.getComment());

        return view;
    }

    private class RatingViewHolder {
        TextView tvName;

        public RatingViewHolder(View item) {
            tvName = (TextView) item.findViewById(R.id.ratingComment);
        }
    }

    public void setRatings(List<Rating> ratings) {
        this.allItems = new ArrayList<Rating>(ratings);
        this.items = new ArrayList<Rating>(ratings);
        notifyDataSetChanged();
    }
}
