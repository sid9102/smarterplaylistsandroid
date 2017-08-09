package co.sidhant.smarterplaylists.fragments

import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.sidhant.smarterplaylists.R
import co.sidhant.smarterplaylists.spotify.SpotifyEntity
import co.sidhant.smarterplaylists.spotify.SpotifyRequests


import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import android.widget.ProgressBar
import co.sidhant.smarterplaylists.PlayerManager


/**
 * A fragment representing a list of Items.
 *
 */
class PlaylistFragment : DialogFragment()
{
    companion object
    {

        const val ARG_AUTH_TOKEN = "auth-token"
        const val ARG_CLIENT_ID = "client-id"

        fun newInstance(authToken: String, clientID: String): PlaylistFragment
        {
            val fragment = PlaylistFragment()
            val args = Bundle()
            args.putString(ARG_AUTH_TOKEN, authToken)
            args.putString(ARG_CLIENT_ID, clientID)
            fragment.arguments = args
            return fragment
        }
    }

    private var mAuthToken = ""
    private var mClientID = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        if (arguments != null)
        {
            mAuthToken = arguments.getString(ARG_AUTH_TOKEN)
            mClientID = arguments.getString(ARG_CLIENT_ID)
        }
        setStyle(DialogFragment.STYLE_NORMAL, this.theme)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        dialog.setTitle("Select Playlist")
        val view = inflater!!.inflate(R.layout.fragment_playlist_list, container, false)
        val playlists = ArrayList<SpotifyEntity>()
        // Set the adapter
        val context = view.context
        val recyclerView = view.findViewById<RecyclerView>(R.id.playlists)
        val spinner = view.findViewById<ProgressBar>(R.id.playlistsLoading)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = MyPlaylistRecyclerViewAdapter(playlists)
        recyclerView.adapter = adapter

        doAsync()
        {
            val requests = SpotifyRequests(mAuthToken)
            requests.getPlaylistsForCurrentUser(playlists)
            uiThread()
            {
                recyclerView.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
                spinner.visibility = View.GONE
            }
        }
        return view
    }

    override fun onDetach()
    {
        PlayerManager.stop()
        super.onDetach()
    }

    override fun onResume()
    {
        // Make this dialog larger
        val params = dialog.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = params
        super.onResume()
    }
}
