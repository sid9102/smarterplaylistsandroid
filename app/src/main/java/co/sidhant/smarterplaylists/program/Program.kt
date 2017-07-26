package co.sidhant.smarterplaylists.program

import co.sidhant.smarterplaylists.SpotifyRequests

/**
 * Program class
 * Created by sid on 7/25/17.
 */

import org.jetbrains.anko.toast

class Program(name: String)
{
    val rows : ArrayList<ProgramRow> = ArrayList()

    /**
     * Connect one block to another.
     * This will connect the block at fromBlockIndex in row toRowIndex - 1
     * to the block at toBlockIndex in row toRowIndex.
     *
     * @param toRowIndex: the index of the row to connect to
     * @param fromBlockIndex: the index of the block to connect from
     * @param toBlockIndex: the index of the block to connect to
     * @param banned: whether this connection is banned
     *
     * @return true if this is a legal operation, false if not
     */
    fun connectBlocks(toRowIndex: Int, fromBlockIndex: Int, toBlockIndex: Int, banned: Boolean) : Boolean
    {
        if (rows[toRowIndex].blocks[toBlockIndex].hasInput)
        {
            if (banned)
                return rows[toRowIndex - 1].toggleBannedOutput(fromBlockIndex, toBlockIndex)
            else
                return rows[toRowIndex - 1].toggleOutput(fromBlockIndex, toBlockIndex)
        }
        return false
    }

    fun runProgram()
    {
        var curOutput = HashMap<Int, ArrayList<SpotifyRequests.SpotifyEntity>>()
        for (row in rows)
        {
            row.input(curOutput)
            curOutput = row.output()
        }
        val result = curOutput[0]
    }

}