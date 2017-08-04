package co.sidhant.smarterplaylists.fragments

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import co.sidhant.smarterplaylists.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TestFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TestFragment : Fragment()
{
    companion object
    {

        fun newInstance(): TestFragment
        {
            val fragment = TestFragment()
            return fragment
        }
    }

    private var mListener: OnTestFragmentCreatedListener? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {

        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_test, container, false)
        mListener?.onTestFragmentCreated(view.findViewById<Button>(R.id.songsButton), view.findViewById<Button>(R.id.playlistsButton) as Button)
        return view
    }

    override fun onAttach(context: Context?)
    {
        super.onAttach(context)
        if (context is OnTestFragmentCreatedListener)
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
    }

    interface OnTestFragmentCreatedListener
    {
        fun onTestFragmentCreated(songsButton: Button, playlistsButton: Button)
    }
}
