package co.sidhant.smarterplaylists.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import co.sidhant.smarterplaylists.R
import co.sidhant.smarterplaylists.spotify.SpotifySong

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class SongFragment : Fragment()
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

    // TODO: Customize parameters
    private var mAuthToken = ""
    private var mClientID = ""
    private var songs = ArrayList<SpotifySong>()
    private var mListener: OnListFragmentInteractionListener? = null

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
        val view = inflater!!.inflate(R.layout.fragment_song_list, container, false)

        // Set the adapter
        if (view is RecyclerView)
        {
            val context = view.getContext()
            val recyclerView = view
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            recyclerView.adapter = MySongRecyclerViewAdapter(songs, mListener)
        }
        return view
    }


    override fun onAttach(context: Context?)
    {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener)
        {
            mListener = context
        } else
        {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach()
    {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnListFragmentInteractionListener
    {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: SpotifySong)
    }
}
