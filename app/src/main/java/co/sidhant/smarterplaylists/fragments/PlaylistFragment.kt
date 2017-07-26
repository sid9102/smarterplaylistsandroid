package co.sidhant.smarterplaylists.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.sidhant.smarterplaylists.R
import co.sidhant.smarterplaylists.SpotifyRequests


import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
class PlaylistFragment : Fragment()
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
    private var mListener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        if (arguments != null)
        {
            mAuthToken = arguments.getString(ARG_AUTH_TOKEN)
            mClientID = arguments.getString(ARG_CLIENT_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater!!.inflate(R.layout.fragment_playlist_list, container, false)
        val playlists = ArrayList<SpotifyRequests.SpotifyEntity>()
        // Set the adapter
        if (view is RecyclerView)
        {
            val context = view.getContext()
            val recyclerView = view
            recyclerView.layoutManager = LinearLayoutManager(context)
            val adapter = MyPlaylistRecyclerViewAdapter(playlists, mListener)
            recyclerView.adapter = adapter

            doAsync()
            {
                val requests = SpotifyRequests(mAuthToken, mClientID)
                requests.getPlaylists(playlists)
                uiThread()
                {
                    adapter.notifyDataSetChanged()
                }
            }
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
        fun onPlaylistInteraction(item: SpotifyRequests.SpotifyEntity)
    }
}
