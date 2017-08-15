package co.sidhant.smarterplaylists

import android.util.Log
import co.sidhant.smarterplaylists.spotify.SpotifyEntity
import com.spotify.sdk.android.player.Config
import com.spotify.sdk.android.player.Player
import com.spotify.sdk.android.player.Spotify
import com.spotify.sdk.android.player.SpotifyPlayer

/**
 * Controls the SpotifyPlayer instance
 * Created by sid on 8/4/17.
 */
object PlayerManager
{
    private lateinit var player: Player

    fun initializePlayer(playerConfig : Config, mainActivity: MainActivity)
    {
        Spotify.getPlayer(playerConfig, this, object : SpotifyPlayer.InitializationObserver
        {
            override fun onInitialized(spotifyPlayer: SpotifyPlayer)
            {
                player = spotifyPlayer
                player.addConnectionStateCallback(mainActivity)
                player.addNotificationCallback(mainActivity)
            }

            override fun onError(throwable: Throwable)
            {
                Log.e("MainActivity", "Could not initialize player: " + throwable.message)
            }
        })
    }

    fun play(entity: SpotifyEntity)
    {
        player.playUri(null, entity.uri, 0, 0)
    }

    fun stop()
    {
        player.pause(null)
    }

    // Returns the current position of the playing track in ms
    fun getPlayPosition() : Int
    {
        return player.playbackState!!.positionMs.toInt()
    }
}