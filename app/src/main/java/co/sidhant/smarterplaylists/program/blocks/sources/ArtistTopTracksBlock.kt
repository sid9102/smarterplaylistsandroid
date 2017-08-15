package co.sidhant.smarterplaylists.program.blocks.sources

import co.sidhant.smarterplaylists.program.blocks.ProgramBlock
import co.sidhant.smarterplaylists.spotify.SpotifyEntity
import co.sidhant.smarterplaylists.spotify.SpotifyRequest
import co.sidhant.smarterplaylists.spotify.SpotifySong

/**
 * Gets an artist's top tracks
 * Created by sid on 8/14/17.
 */
class ArtistTopTracksBlock(artist: SpotifyEntity) : ProgramBlock
{
    override var name: String = artist.name
        get() = field
        set(value)
        {
            field = value
        }
    override val hasInput: Boolean = false
        get() = field

    var artist: SpotifyEntity = artist
        get() = field
        set(value)
        {
            field = value
            this.name = value.name
        }

    override fun output(): ArrayList<SpotifySong>
    {
        return SpotifyRequest.getArtistTopTracks(artist)
    }

}