package co.sidhant.smarterplaylists.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.sidhant.smarterplaylists.R;
import co.sidhant.smarterplaylists.SpotifyRequests;
import co.sidhant.smarterplaylists.fragments.PlaylistFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link co.sidhant.smarterplaylists.SpotifyRequests.SpotifyEntity} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyPlaylistRecyclerViewAdapter extends RecyclerView.Adapter<MyPlaylistRecyclerViewAdapter.ViewHolder> {

    private final List<SpotifyRequests.SpotifyEntity> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyPlaylistRecyclerViewAdapter(List<SpotifyRequests.SpotifyEntity> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPlaylistInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public SpotifyRequests.SpotifyEntity mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}