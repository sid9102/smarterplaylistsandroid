package co.sidhant.smarterplaylists.spotify

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.string
import com.google.gson.Gson

/**
 * Spotify entities
 * Created by sid on 7/26/17.
 */
open class SpotifyEntity(open var name: String, open var uri: String)
{
    open var playing : Boolean = false
}

class SpotifySong(@Transient override var name: String, @Transient override var uri: String, var artist: String): SpotifyEntity(name, uri)
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
            fun parse(body: String) : JsonArray<JsonObject>
            {
                val parser: Parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(body)
                return parser.parse(stringBuilder) as JsonArray<JsonObject>
            }

            val songs = ArrayList<SpotifySong>()
            parse(json).mapTo(songs) { SpotifySong(it.string("name") as String, it.string("uri") as String, it.string("artist") as String) }
            return songs
        }
    }
}