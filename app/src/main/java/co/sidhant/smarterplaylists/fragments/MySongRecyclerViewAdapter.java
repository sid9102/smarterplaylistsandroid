package co.sidhant.smarterplaylists.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.sidhant.smarterplaylists.PlayerManager;
import co.sidhant.smarterplaylists.PrefManager;
import co.sidhant.smarterplaylists.R;
import co.sidhant.smarterplaylists.spotify.SpotifyEntity;
import co.sidhant.smarterplaylists.spotify.SpotifySong;
import co.sidhant.smarterplaylists.views.PreviewButton;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SpotifySong}.
 */
public class MySongRecyclerViewAdapter extends RecyclerView.Adapter<MySongRecyclerViewAdapter.ViewHolder> {

    private final List<SpotifySong> mValues;

    public MySongRecyclerViewAdapter(List<SpotifySong> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_song, parent, false);

        PreviewButton button = null;
        if(PrefManager.INSTANCE.isUserPremium())
        {
            button = new PreviewButton(parent.getContext());
            button.initView(new SpotifyEntity("PLACEHOLDER", "PLACEHOLDER"));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            button.setLayoutParams(params);
            layout.addView(button);
        }

        ViewHolder holder = new ViewHolder(layout);
        holder.button = button;
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mSong = mValues.get(position);
        holder.mSongNameView.setText(mValues.get(position).getName());
        holder.mArtistView.setText(mValues.get(position).getArtist());

        if(holder.button != null)
        {
            holder.button.entity = holder.mSong;
            if (holder.mSong.getPlaying())
            {
                int curPosition = PlayerManager.INSTANCE.getPlayPosition() / 10;
                holder.button.setProgress(curPosition);
            }
            else
            {
                holder.button.setProgressMax();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mSongNameView;
        final TextView mArtistView;
        SpotifySong mSong;
        PreviewButton button;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mSongNameView = view.findViewById(R.id.songName);
            mArtistView = view.findViewById(R.id.artist);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mArtistView.getText() + "'";
        }
    }
}
