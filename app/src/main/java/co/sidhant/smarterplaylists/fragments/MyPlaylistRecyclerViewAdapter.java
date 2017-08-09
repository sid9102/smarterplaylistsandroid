package co.sidhant.smarterplaylists.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.sidhant.smarterplaylists.R;
import co.sidhant.smarterplaylists.spotify.SpotifyEntity;
import co.sidhant.smarterplaylists.views.PreviewButton;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SpotifyEntity}.
 */
public class MyPlaylistRecyclerViewAdapter extends RecyclerView.Adapter<MyPlaylistRecyclerViewAdapter.ViewHolder> {

    private final List<            SpotifyEntity> mValues;

    public MyPlaylistRecyclerViewAdapter(List<SpotifyEntity> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_playlist, parent, false);

        PreviewButton button = new PreviewButton(parent.getContext());
        button.initView(new SpotifyEntity("PLACEHOLDER", "PLACEHOLDER"));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        button.setLayoutParams(params);
        layout.addView(button);

        ViewHolder holder = new ViewHolder(layout);
        holder.button = button;
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.button.entity = holder.mItem;
        holder.mContentView.setText(mValues.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public SpotifyEntity mItem;
        public PreviewButton button;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.playlistName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
