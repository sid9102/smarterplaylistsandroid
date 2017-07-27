package co.sidhant.smarterplaylists.program.blocks.filters

import co.sidhant.smarterplaylists.program.blocks.ProgramBlock

/**
 * A filter block
 * Created by sid on 7/26/17.
 */
interface FilterBlock : ProgramBlock
{
    fun setFilterParams(acousticness: ClosedRange<Float>, danceability: ClosedRange<Float>,
                        duration_ms : IntRange, energy: ClosedRange<Float>,
                        instrumentalness: ClosedRange<Float>, key : IntRange,
                        liveness: ClosedRange<Float>, loudness: ClosedRange<Float>,
                        mode: IntRange, popularity: IntRange, speechiness: ClosedRange<Float>,
                        tempo: ClosedRange<Float>, time_signature : IntRange,
                        valence : ClosedRange<Float>) : String
    {
        var params = ""

        return params
    }
}