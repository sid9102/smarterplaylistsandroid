package co.sidhant.smarterplaylists.program.blocks.sources

import co.sidhant.smarterplaylists.SpotifyRequests
import co.sidhant.smarterplaylists.program.blocks.ProgramBlock

/**
 * Playlist Source Block
 * Created by sid on 7/26/17.
 */
class PlaylistBlock(playlist: SpotifyRequests.SpotifyEntity) : ProgramBlock
{
    override var name: String = playlist.name
        get() = field
        set(value)
        {
            field = value
        }
    override val hasInput: Boolean = false
        get() = field

    var playlist: SpotifyRequests.SpotifyEntity = playlist
        get() = field
        set(value)
        {
            field = value
            this.name = value.name
        }

    override fun output(requests: SpotifyRequests): ArrayList<SpotifyRequests.SpotifyEntity>
    {
        return requests.getPlaylistTracks(playlist.uri)
    }
}