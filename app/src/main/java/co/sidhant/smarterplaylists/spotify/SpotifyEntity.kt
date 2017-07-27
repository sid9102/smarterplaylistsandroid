package co.sidhant.smarterplaylists.spotify

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Spotify entities
 * Created by sid on 7/26/17.
 */
open class SpotifyEntity(open var name: String, open var uri: String)

class SpotifySong(override var name: String, override var uri: String, var artist: String): SpotifyEntity(name, uri)
{
    companion object
    {
        fun dumpSongsToJson(songs: ArrayList<SpotifySong>): String
        {
            val gson = Gson()
            return gson.toJson(songs)
        }

        fun importSongsFromJson(json: String): ArrayList<SpotifySong>
        {
            val gson = Gson()
            val returnType = object : TypeToken<Collection<SpotifySong>>(){}.type
            val songs: ArrayList<SpotifySong> = gson.fromJson(json, returnType)
            return songs
        }
    }
}