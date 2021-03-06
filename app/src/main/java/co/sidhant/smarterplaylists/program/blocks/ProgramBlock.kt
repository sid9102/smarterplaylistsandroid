package co.sidhant.smarterplaylists.program.blocks

import co.sidhant.smarterplaylists.spotify.SpotifySong

/**
 * Interface for ProgramBlocks
 * Created by sid on 7/25/17.
 */
interface ProgramBlock
{
    var name : String
    val hasInput : Boolean

    fun output() : ArrayList<SpotifySong>
    fun input(input: java.util.ArrayList<SpotifySong>?){}
}