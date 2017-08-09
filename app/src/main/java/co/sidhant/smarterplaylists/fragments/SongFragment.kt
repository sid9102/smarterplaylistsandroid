package co.sidhant.smarterplaylists.fragments

import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.sidhant.smarterplaylists.PlayerManager

import co.sidhant.smarterplaylists.R
import co.sidhant.smarterplaylists.spotify.SpotifySong

/**
 * A dialog that displays the songs produced by a program.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
class SongFragment : DialogFragment()
{
    companion object
    {

        const val ARG_AUTH_TOKEN = "auth-token"
        const val ARG_CLIENT_ID = "client-id"
        const val ARG_SONGS_JSON = "songs-json"

        fun newInstance(authToken: String, clientID: String, songsJson: String): SongFragment
        {
            val fragment = SongFragment()
            val args = Bundle()
            args.putString(ARG_AUTH_TOKEN, authToken)
            args.putString(ARG_CLIENT_ID, clientID)
            args.putString(ARG_SONGS_JSON, songsJson)
            fragment.arguments = args
            return fragment
        }
    }

    private var mAuthToken = ""
    private var mClientID = ""
    private var songs = ArrayList<SpotifySong>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        if (arguments != null)
        {
            mAuthToken = arguments.getString(ARG_AUTH_TOKEN)
            mClientID = arguments.getString(ARG_CLIENT_ID)
            val songsJson = arguments.getString(ARG_SONGS_JSON)
            songs = SpotifySong.importSongsFromJson(songsJson)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        dialog.setTitle("Program Output")
        val view = inflater!!.inflate(R.layout.fragment_song_list, container, false)
        // Set the adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.songsList)
        val context = view.getContext()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MySongRecyclerViewAdapter(songs)
        return view
    }


    override fun onAttach(context: Context?)
    {
        super.onAttach(context)
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

    override fun onDetach()
    {
        PlayerManager.stop()
        super.onDetach()
    }
}
