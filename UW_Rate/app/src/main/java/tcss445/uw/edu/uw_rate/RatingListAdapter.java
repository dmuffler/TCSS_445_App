package tcss445.uw.edu.uw_rate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

        mViewHolder.rbRating.setRating(rating.getScore());
        mViewHolder.rbEggplant.setRating(rating.getHotness());
        mViewHolder.tvComment.setText(rating.getComment());
        mViewHolder.tvAuthor.setText(rating.getAuthorFullName());

        return view;
    }

    private class RatingViewHolder {
        RatingBar rbRating;
        RatingBar rbEggplant;
        TextView tvComment;
        TextView tvAuthor;

        public RatingViewHolder(View item) {
            rbRating = (RatingBar) item.findViewById(R.id.listItemRatingBar);
            rbEggplant = (RatingBar) item.findViewById(R.id.listItemEggplantBar);
            tvComment = (TextView) item.findViewById(R.id.listItemRatingComment);
            tvAuthor = (TextView) item.findViewById(R.id.listItemRatingAuthor);
        }
    }

    public void setRatings(List<Rating> ratings) {
        this.allItems = new ArrayList<Rating>(ratings);
        this.items = new ArrayList<Rating>(ratings);
        notifyDataSetChanged();
    }
}
