package co.sidhant.smarterplaylists.program

import co.sidhant.smarterplaylists.SpotifyRequests
import co.sidhant.smarterplaylists.program.blocks.ProgramBlock

/**
 * Represents one row in the program view
 * Created by sid on 7/25/17.
 */
class ProgramRow
{
    val blocks: ArrayList<ProgramBlock> = ArrayList()
    // Key is the index of the block (in the next row) to output to, value is a set of banned blocks
    val bannedOutputs: HashMap<Int, HashSet<Int>> = HashMap()
    // Same as above but for outputs to include
    val outputs: HashMap<Int, HashSet<Int>> = HashMap()

    fun addBlock(block : ProgramBlock)
    {
        blocks.add(block)
    }

    fun toggleBannedOutput(index : Int, outputIndex: Int) : Boolean
    {
        val curOutput = outputs[outputIndex]
        if (curOutput != null)
        {
            // We can't add to banned output if this is also a regular output
            if(curOutput.contains(index))
                return false
        }
        var curBanned = bannedOutputs[outputIndex]
        if (curBanned == null)
        {
            curBanned = HashSet<Int>()
            bannedOutputs[outputIndex] = curBanned
        }
        if (curBanned.contains(index))
            curBanned.remove(index)
        else
            curBanned.add(index)
        return true
    }

    fun toggleOutput(index : Int, outputIndex: Int) : Boolean
    {
        val curBanned = bannedOutputs[outputIndex]
        if (curBanned != null)
        {
            // We can't add to banned output if this is also a regular output
            if(curBanned.contains(index))
                return false
        }
        var curOutput = outputs[outputIndex]
        if (curOutput == null)
        {
            curOutput = HashSet<Int>()
            outputs[outputIndex] = curOutput
        }
        if (curOutput.contains(index))
            curOutput.remove(index)
        else
            curOutput.add(index)
        return true
    }

    /**
     * Feed input to the blocks in this row
     */
    fun input(input: HashMap<Int, ArrayList<SpotifyRequests.SpotifyEntity>>)
    {
        for (index in input.keys)
        {
            blocks[index].input(input[index])
        }
    }
    /**
     * Creates a list of outputs, where key is the index of the block in
     * the next row to output to, and value is the list of songs to output.
     */
    fun output() : HashMap<Int, ArrayList<SpotifyRequests.SpotifyEntity>>
    {
        val result = HashMap<Int, ArrayList<SpotifyRequests.SpotifyEntity>>()
        // Get all recommended tracks from input blocks
        for (index in outputs.keys)
        {
            val curResults = ArrayList<SpotifyRequests.SpotifyEntity>()
            for(i in outputs[index]!!)
            {
                curResults.addAll(blocks[i].output())
            }
            val curBanned = HashSet<SpotifyRequests.SpotifyEntity>()
            if(bannedOutputs[index] != null)
            {
                for(i in bannedOutputs[index]!!)
                {
                    curBanned.addAll(blocks[i].output())
                }
                curResults.filter { entity -> !curBanned.contains(entity) }
            }
            result[index] = curResults
        }
        return result
    }
}