package co.sidhant.smarterplaylists

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button

import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.player.Config
import com.spotify.sdk.android.player.ConnectionStateCallback
import com.spotify.sdk.android.player.Error
import com.spotify.sdk.android.player.Player
import com.spotify.sdk.android.player.PlayerEvent
import com.spotify.sdk.android.player.Spotify


import co.sidhant.smarterplaylists.fragments.PlaylistFragment
import co.sidhant.smarterplaylists.fragments.SongFragment
import co.sidhant.smarterplaylists.fragments.TestFragment
import co.sidhant.smarterplaylists.program.Program
import co.sidhant.smarterplaylists.program.blocks.sources.PlaylistBlock
import co.sidhant.smarterplaylists.spotify.SpotifyEntity
import co.sidhant.smarterplaylists.spotify.SpotifyRequests
import co.sidhant.smarterplaylists.spotify.SpotifySong
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : Activity(),
        Player.NotificationCallback,
        ConnectionStateCallback,
        PlaylistFragment.OnListFragmentInteractionListener,
        TestFragment.OnTestFragmentCreatedListener
{
    companion object
    {

        private val CLIENT_ID = "ee7a464c2dc4410e972b78568ddde051"
        private val REDIRECT_URI = "sidhant://sidhant.co/spotify/"
        private var accessToken = ""

        // Request code that will be used to verify if the result comes from correct activity
        // Can be any integer
        private val REQUEST_CODE = 1337
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val builder = AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI)
        builder.setScopes(arrayOf("user-read-private", "streaming", "playlist-read-private", "user-top-read"))
        val request = builder.build()

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent)
    {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE)
        {
            val response = AuthenticationClient.getResponse(resultCode, intent)
            val fragmentTransaction = fragmentManager.beginTransaction()
            val prev = fragmentManager.findFragmentByTag("test")
            if (prev != null)
            {
                fragmentTransaction.remove(prev)
            }
            val newFragment = TestFragment.newInstance()
            fragmentTransaction.add(R.id.mainContainer, newFragment, "test")
            fragmentTransaction.commit()
            accessToken = response.accessToken
            if (response.type == AuthenticationResponse.Type.TOKEN)
            {
                val playerConfig = Config(this, response.accessToken, CLIENT_ID)
                PlayerManager.initializePlayer(playerConfig, this)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTestFragmentCreated(songsButton: Button, playlistsButton: Button)
    {
        songsButton.onClick()
        {
            doAsync()
            {
                val program = Program("what")
                program.addBlock(PlaylistBlock(SpotifyEntity("Twin Peaks", "spotify:user:johnnyjewelspotify:playlist:1CAvNa3OMDeoQX1vhl4jxZ")), 0)
                val songs = program.runProgram(SpotifyRequests(accessToken)) as ArrayList<SpotifySong>
                runOnUiThread()
                {
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val songFragment = SongFragment.newInstance(accessToken, CLIENT_ID, SpotifySong.dumpSongsToJson(songs))

                    val prevDialog = fragmentManager.findFragmentByTag("dialog")
                    if (prevDialog != null)
                    {
                        fragmentTransaction.remove(prevDialog)
                    }
                    songFragment.show(fragmentTransaction, "dialog")
                }
            }
        }
        playlistsButton.onClick()
        {
            val fragmentTransaction = fragmentManager.beginTransaction()
            val prevDialog = fragmentManager.findFragmentByTag("dialog")
            if (prevDialog != null)
            {
                fragmentTransaction.remove(prevDialog)
            }
            val newDialog = PlaylistFragment.newInstance(accessToken, CLIENT_ID)
            newDialog.show(fragmentTransaction, "dialog")
        }
    }
}