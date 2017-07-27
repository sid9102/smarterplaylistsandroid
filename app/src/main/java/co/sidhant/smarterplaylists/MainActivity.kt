package co.sidhant.smarterplaylists

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.player.Config
import com.spotify.sdk.android.player.ConnectionStateCallback
import com.spotify.sdk.android.player.Error
import com.spotify.sdk.android.player.Player
import com.spotify.sdk.android.player.PlayerEvent
import com.spotify.sdk.android.player.Spotify
import com.spotify.sdk.android.player.SpotifyPlayer


import co.sidhant.smarterplaylists.fragments.PlaylistFragment
import co.sidhant.smarterplaylists.fragments.SongFragment
import co.sidhant.smarterplaylists.program.Program
import co.sidhant.smarterplaylists.program.blocks.sources.PlaylistBlock
import co.sidhant.smarterplaylists.spotify.SpotifyEntity
import co.sidhant.smarterplaylists.spotify.SpotifyRequests
import co.sidhant.smarterplaylists.spotify.SpotifySong
import org.jetbrains.anko.doAsync

class MainActivity : AppCompatActivity(), Player.NotificationCallback, ConnectionStateCallback, PlaylistFragment.OnListFragmentInteractionListener, SongFragment.OnListFragmentInteractionListener
{

    private var mPlayer: Player? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val builder = AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI)
        builder.setScopes(arrayOf("user-read-private", "streaming"))
        val request = builder.build()

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent)
    {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE)
        {
            var songs = ArrayList<SpotifySong>()
            val response = AuthenticationClient.getResponse(resultCode, intent)
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val args = Bundle()
            args.putString(ARG_AUTH_TOKEN, response.accessToken)
            args.putString(ARG_CLIENT_ID, CLIENT_ID)
            doAsync()
            {
                val program = Program("what")
                program.addBlock(PlaylistBlock(SpotifyEntity("Twin Peaks", "spotify:user:johnnyjewelspotify:playlist:1CAvNa3OMDeoQX1vhl4jxZ")), 0)
                songs = program.runProgram(SpotifyRequests(response.accessToken)) as ArrayList<SpotifySong>
                args.putString(ARG_SONGS_JSON, SpotifySong.dumpSongsToJson(songs))
                runOnUiThread()
                {
                    val songFragment = SongFragment()
                    songFragment.arguments = args
                    Log.i("WHAT", args.getString(ARG_SONGS_JSON))
                    fragmentTransaction.add(R.id.mainContainer, songFragment)
                    fragmentTransaction.commit()
                }
            }
            if (response.type == AuthenticationResponse.Type.TOKEN)
            {
                val playerConfig = Config(this, response.accessToken, CLIENT_ID)
                Spotify.getPlayer(playerConfig, this, object : SpotifyPlayer.InitializationObserver
                {
                    override fun onInitialized(spotifyPlayer: SpotifyPlayer)
                    {
                        mPlayer = spotifyPlayer
                        mPlayer!!.addConnectionStateCallback(this@MainActivity)
                        mPlayer!!.addNotificationCallback(this@MainActivity)
                    }

                    override fun onError(throwable: Throwable)
                    {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.message)
                    }
                })
            }
        }
    }

    override fun onDestroy()
    {
        Spotify.destroyPlayer(this)
        super.onDestroy()
    }

    override fun onPlaybackEvent(playerEvent: PlayerEvent)
    {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name)
        when (playerEvent)
        {
        // Handle event type as necessary
            else ->
            {
            }
        }
    }

    override fun onPlaybackError(error: Error)
    {
        Log.d("MainActivity", "Playback error received: " + error.name)
        when (error)
        {
        // Handle error type as necessary
            else ->
            {
            }
        }
    }

    override fun onLoggedIn()
    {
        Log.d("MainActivity", "User logged in")
    }

    override fun onLoggedOut()
    {
        Log.d("MainActivity", "User logged out")
    }

    override fun onLoginFailed(error: Error)
    {

    }

    fun onLoginFailed(i: Int)
    {
        Log.d("MainActivity", "Login failed")
    }

    override fun onTemporaryError()
    {
        Log.d("MainActivity", "Temporary error occurred")
    }

    override fun onConnectionMessage(message: String)
    {
        Log.d("MainActivity", "Received connection message: " + message)
    }

    override fun onPlaylistInteraction(item: SpotifyEntity)
    {
        mPlayer!!.playUri(null, item.uri, 0, 0)
    }

    override fun onSongInteraction(item: SpotifySong)
    {
        mPlayer!!.playUri(null, item.uri, 0, 0)
    }

    companion object
    {

        private val CLIENT_ID = "ee7a464c2dc4410e972b78568ddde051"
        private val REDIRECT_URI = "sidhant://sidhant.co/spotify/"

        private val ARG_AUTH_TOKEN = "auth-token"
        private val ARG_CLIENT_ID = "client-id"
        private val ARG_SONGS_JSON = "songs-json"

        // Request code that will be used to verify if the result comes from correct activity
        // Can be any integer
        private val REQUEST_CODE = 1337
    }
}