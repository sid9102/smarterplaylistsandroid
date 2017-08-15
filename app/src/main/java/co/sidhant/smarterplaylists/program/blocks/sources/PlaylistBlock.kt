package co.sidhant.smarterplaylists.program.blocks.sources

import co.sidhant.smarterplaylists.spotify.SpotifyRequest
import co.sidhant.smarterplaylists.program.blocks.ProgramBlock
import co.sidhant.smarterplaylists.spotify.SpotifyEntity
import co.sidhant.smarterplaylists.spotify.SpotifySong

/**
 * Playlist Source Block
 * Created by sid on 7/26/17.
 */
class PlaylistBlock(playlist: SpotifyEntity) : ProgramBlock
{
    override var name: String = playlist.name
        get() = field
        set(value)
        {
            field = value
        }
    override val hasInput: Boolean = false
        get() = field

    var playlist: SpotifyEntity = playlist
        get() = field
        set(value)
        {
            field = value
            this.name = value.name
        }

    override fun output(): ArrayList<SpotifySong>
    {
        return SpotifyRequest.getPlaylistTracks(playlist.uri)
    }
}