package co.sidhant.smarterplaylists.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.sidhant.smarterplaylists.PlayerManager;
import co.sidhant.smarterplaylists.R;
import co.sidhant.smarterplaylists.spotify.SpotifyEntity;
import co.sidhant.smarterplaylists.views.PreviewButton;
import co.sidhant.smarterplaylists.fragments.PlaylistFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SpotifyEntity} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyPlaylistRecyclerViewAdapter extends RecyclerView.Adapter<MyPlaylistRecyclerViewAdapter.ViewHolder> {

    private final List<SpotifyEntity> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyPlaylistRecyclerViewAdapter(List<SpotifyEntity> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
        holder.mContentView.setText(mValues.get(position).getName());

        holder.button.entity = holder.mItem;
        if(holder.mItem.getPlaying())
        {
            int curPosition = PlayerManager.INSTANCE.getPlayPosition() / 10;
            holder.button.setProgress(curPosition);
        }
        else
        {
            holder.button.setProgressMax();
        }

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onPlaylistInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mContentView;
        SpotifyEntity mItem;
        PreviewButton button;

        ViewHolder(View view) {
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
