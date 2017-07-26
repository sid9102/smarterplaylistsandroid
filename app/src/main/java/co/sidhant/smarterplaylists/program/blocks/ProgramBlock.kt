package co.sidhant.smarterplaylists.program.blocks

import co.sidhant.smarterplaylists.SpotifyRequests

/**
 * Interface for ProgramBlocks
 * Created by sid on 7/25/17.
 */
interface ProgramBlock
{
    var name : String
    val hasInput : Boolean
    val requests : SpotifyRequests

    fun output() : ArrayList<SpotifyRequests.SpotifyEntity>
    fun input(input: java.util.ArrayList<SpotifyRequests.SpotifyEntity>?){}
}