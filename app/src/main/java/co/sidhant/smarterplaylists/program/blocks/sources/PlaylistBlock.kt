package co.sidhant.smarterplaylists.program.blocks.sources

import co.sidhant.smarterplaylists.SpotifyRequests
import co.sidhant.smarterplaylists.program.blocks.ProgramBlock

/**
 * Playlist Source Block
 * Created by sid on 7/26/17.
 */
class PlaylistBlock(var uri: String, override val requests: SpotifyRequests) : ProgramBlock
{
    override var name: String = requests.getName(uri)
        get() = field
        set(value)
        {
            field = value
        }
    override val hasInput: Boolean = false
        get() = field

    override fun output(): ArrayList<SpotifyRequests.SpotifyEntity>
    {
        return requests.getPlaylistTracks(uri)
    }

}