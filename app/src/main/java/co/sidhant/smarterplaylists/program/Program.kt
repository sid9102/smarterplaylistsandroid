package co.sidhant.smarterplaylists.program

import co.sidhant.smarterplaylists.program.blocks.ProgramBlock
import co.sidhant.smarterplaylists.spotify.SpotifySong
import com.google.gson.Gson

/**
 * Program class
 * Created by sid on 7/25/17.
 */


class Program(var name: String)
{
    val rows : ArrayList<ProgramRow> = ArrayList()

    fun addRow()
    {
        rows[rows.size - 1].isLastRow = false
        rows.add(ProgramRow(true))
    }
    fun addBlock(block: ProgramBlock, rowIndex: Int)
    {
        if (rows.size > rowIndex)
            rows[rowIndex].addBlock(block)
    }

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
    fun connectBlocks(toRowIndex: Int, fromBlockIndex: Int, toBlockIndex: Int, banned: Boolean = false) : Boolean
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

    fun runProgram(): ArrayList<SpotifySong>?
    {
        var curOutput = HashMap<Int, ArrayList<SpotifySong>>()
        for (row in rows)
        {
            row.input(curOutput)
            curOutput = row.output()
        }
        val result = curOutput[0]
        return result
    }

    fun toJSON() : String
    {
        val gson = Gson()
        return gson.toJson(this).toString()
    }

    init
    {
        val firstRow = ProgramRow(true)
        rows.add(firstRow)
    }
}