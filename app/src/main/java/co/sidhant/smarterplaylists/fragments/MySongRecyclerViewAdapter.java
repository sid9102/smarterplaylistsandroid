package co.sidhant.smarterplaylists.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.sidhant.smarterplaylists.R;
import co.sidhant.smarterplaylists.fragments.SongFragment.OnListFragmentInteractionListener;
import co.sidhant.smarterplaylists.spotify.SpotifySong;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SpotifySong} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MySongRecyclerViewAdapter extends RecyclerView.Adapter<MySongRecyclerViewAdapter.ViewHolder> {

    private final List<SpotifySong> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MySongRecyclerViewAdapter(List<SpotifySong> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mSong = mValues.get(position);

        holder.mSongNameView.setText(mValues.get(position).getName());
        holder.mArtistView.setText(mValues.get(position).getArtist());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onSongInteraction(holder.mSong);
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
        public final TextView mSongNameView;
        public final TextView mArtistView;
        public SpotifySong mSong;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSongNameView = (TextView) view.findViewById(R.id.name);
            mArtistView = (TextView) view.findViewById(R.id.artist);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mArtistView.getText() + "'";
        }
    }
}
